package com.xiangxue.jack.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.util.HTTPRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.commons.httpclient.DefaultApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.DefaultApacheHttpClientFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriTemplate;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.REQUEST_ENTITY_KEY;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.REQUEST_URI_KEY;

@Slf4j
//@Component
public class RoutingFilter extends ZuulFilter {

    private ApacheHttpClientConnectionManagerFactory connectionManagerFactory;

    private ApacheHttpClientFactory httpClientFactory;

    private CloseableHttpClient httpClient;

    private HttpClientConnectionManager connectionManager;

    private boolean urlDecoded = true;

    private boolean addHostHeader = false;

    public static final String IGNORED_HEADERS = "ignoredHeaders";

    public static final Pattern FORM_FEED_PATTERN = Pattern.compile("\f");

    public static final Pattern COLON_PATTERN = Pattern.compile(":");

    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @PostConstruct
    public void init() {
        connectionManagerFactory = new DefaultApacheHttpClientConnectionManagerFactory();
        this.connectionManager = newConnectionManager();
        httpClientFactory = new DefaultApacheHttpClientFactory(HttpClientBuilder.create());
        httpClient = newClient();
    }

    protected HttpClientConnectionManager newConnectionManager() {
        return connectionManagerFactory.newConnectionManager(
                false,
                200,
                20,
                -1, TimeUnit.MILLISECONDS,
                null);
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().sendZuulResponse();
    }

    protected CloseableHttpClient newClient() {
        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(
                        -1)
                .setSocketTimeout(10000)
                .setConnectTimeout(2000)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        return httpClientFactory.createBuilder().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(this.connectionManager).disableRedirectHandling()
                .build();
    }

    public String buildZuulRequestURI(HttpServletRequest request) {
        RequestContext context = RequestContext.getCurrentContext();
        String uri = request.getRequestURI();
        String contextURI = (String) context.get(REQUEST_URI_KEY);
        if (contextURI != null) {
            try {
                uri = contextURI;
                if (this.urlDecoded) {
                    uri = UriUtils.encodePath(contextURI, characterEncoding(request));
                }
            } catch (Exception e) {
                log.debug(
                        "unable to encode uri path from context, falling back to uri from request",
                        e);
            }
        }
        return uri;
    }

    private String characterEncoding(HttpServletRequest request) {
        return request.getCharacterEncoding() != null ? request.getCharacterEncoding()
                : WebUtils.DEFAULT_CHARACTER_ENCODING;
    }

    private String getVerb(HttpServletRequest request) {
        String sMethod = request.getMethod();
        return sMethod.toUpperCase();
    }

    public boolean isIncludedHeader(String headerName) {
        String name = headerName.toLowerCase();
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.containsKey(IGNORED_HEADERS)) {
            Object object = ctx.get(IGNORED_HEADERS);
            if (object instanceof Collection && ((Collection<?>) object).contains(name)) {
                return false;
            }
        }
        switch (name) {
            case "host":
                if (addHostHeader) {
                    return true;
                }
            case "connection":
            case "content-length":
            case "server":
            case "transfer-encoding":
            case "x-application-context":
                return false;
            default:
                return true;
        }
    }

    public MultiValueMap<String, String> buildZuulRequestHeaders(
            HttpServletRequest request) {
        RequestContext context = RequestContext.getCurrentContext();
        MultiValueMap<String, String> headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                if (isIncludedHeader(name)) {
                    Enumeration<String> values = request.getHeaders(name);
                    while (values.hasMoreElements()) {
                        String value = values.nextElement();
                        headers.add(name, value);
                    }
                }
            }
        }
        Map<String, String> zuulRequestHeaders = context.getZuulRequestHeaders();
        for (String header : zuulRequestHeaders.keySet()) {
            if (isIncludedHeader(header)) {
                headers.set(header, zuulRequestHeaders.get(header));
            }
        }
        if (!headers.containsKey(HttpHeaders.ACCEPT_ENCODING)) {
            headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip");
        }
        return headers;
    }

    public MultiValueMap<String, String> buildZuulRequestQueryParams(
            HttpServletRequest request) {
        Map<String, List<String>> map = HTTPRequestUtils.getInstance().getQueryParams();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (map == null) {
            return params;
        }
        for (String key : map.keySet()) {
            for (String value : map.get(key)) {
                params.add(key, value);
            }
        }
        return params;
    }

    protected InputStream getRequestBody(HttpServletRequest request) {
        InputStream requestEntity = null;
        try {
            requestEntity = (InputStream) RequestContext.getCurrentContext()
                    .get(REQUEST_ENTITY_KEY);
            if (requestEntity == null) {
                requestEntity = request.getInputStream();
            }
        } catch (IOException ex) {
            log.error("error during getRequestBody", ex);
        }
        return requestEntity;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        StringBuilder sb = currentContext.getFilterExecutionSummary();
        HttpServletRequest request = currentContext.getRequest();
        MultiValueMap<String, String> headers = this
                .buildZuulRequestHeaders(request);
        MultiValueMap<String, String> params = this
                .buildZuulRequestQueryParams(request);
        String verb = getVerb(request);
        InputStream requestEntity = getRequestBody(request);
        String uri = this.buildZuulRequestURI(request);
        try {
            CloseableHttpResponse response = forward(this.httpClient, verb, uri, request,
                    headers, params, requestEntity);
            setResponse(response);
        } catch (Exception ex) {
            throw new ZuulRuntimeException(ex);
        }
        return null;
    }

    private void setResponse(HttpResponse response) throws IOException {
        RequestContext.getCurrentContext().set("zuulResponse", response);
        this.setResponse(response.getStatusLine().getStatusCode(),
                response.getEntity() == null ? null : response.getEntity().getContent(),
                revertHeaders(response.getAllHeaders()));
    }

    private MultiValueMap<String, String> revertHeaders(Header[] headers) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (Header header : headers) {
            String name = header.getName();
            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<String>());
            }
            map.get(name).add(header.getValue());
        }
        return map;
    }

    public void setResponse(int status, InputStream entity,
                            MultiValueMap<String, String> headers) throws IOException {
        RequestContext context = RequestContext.getCurrentContext();
        context.setResponseStatusCode(status);
        if (entity != null) {
            context.setResponseDataStream(entity);
        }

        boolean isOriginResponseGzipped = false;
        for (Map.Entry<String, List<String>> header : headers.entrySet()) {
            String name = header.getKey();
            for (String value : header.getValue()) {
                context.addOriginResponseHeader(name, value);

                if (name.equalsIgnoreCase(HttpHeaders.CONTENT_ENCODING)
                        && HTTPRequestUtils.getInstance().isGzipped(value)) {
                    isOriginResponseGzipped = true;
                }
                if (name.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH)) {
                    context.setOriginContentLength(value);
                }
                if (isIncludedHeader(name)) {
                    context.addZuulResponseHeader(name, value);
                }
            }
        }
        context.setResponseGZipped(isOriginResponseGzipped);
    }

    private HttpHost getHttpHost() {
        HttpHost httpHost = new HttpHost("127.0.0.1", 8083);
        return httpHost;
    }

    private CloseableHttpResponse forward(CloseableHttpClient httpclient, String verb,
                                          String uri, HttpServletRequest request, MultiValueMap<String, String> headers,
                                          MultiValueMap<String, String> params, InputStream requestEntity)
            throws Exception {

        ContentType contentType = null;
//        URL host = RequestContext.getCurrentContext().getRouteHost();
        HttpHost httpHost = getHttpHost();
        if (request.getContentType() != null) {
            contentType = ContentType.parse(request.getContentType());
        }

        InputStreamEntity entity = new InputStreamEntity(requestEntity, request.getContentLength(),
                contentType);

        HttpRequest httpRequest = buildHttpRequest(verb, uri, entity, headers, params,
                request);
        try {
            log.debug(httpHost.getHostName() + " " + httpHost.getPort() + " "
                    + httpHost.getSchemeName());
            CloseableHttpResponse zuulResponse = forwardRequest(httpclient, httpHost,
                    httpRequest);
            return zuulResponse;
        } finally {

        }
    }

    private CloseableHttpResponse forwardRequest(CloseableHttpClient httpclient,
                                                 HttpHost httpHost, HttpRequest httpRequest) throws IOException {
        return httpclient.execute(httpHost, httpRequest);
    }

    private String getEncodedQueryString(HttpServletRequest request) {
        String query = request.getQueryString();
        return (query != null) ? "?" + query : "";
    }

    public String getQueryString(MultiValueMap<String, String> params) {
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder query = new StringBuilder();
        Map<String, Object> singles = new HashMap<>();
        for (String param : params.keySet()) {
            int i = 0;
            for (String value : params.get(param)) {
                query.append("&");
                query.append(param);
                if (!"".equals(value)) { // don't add =, if original is ?wsdl, output is
                    // not ?wsdl=
                    String key = param;
                    // if form feed is already part of param name double
                    // since form feed is used as the colon replacement below
                    if (key.contains("\f")) {
                        key = (FORM_FEED_PATTERN.matcher(key).replaceAll("\f\f"));
                    }
                    // colon is special to UriTemplate
                    if (key.contains(":")) {
                        key = COLON_PATTERN.matcher(key).replaceAll("\f");
                    }
                    key = key + i;
                    singles.put(key, value);
                    query.append("={");
                    query.append(key);
                    query.append("}");
                }
                i++;
            }
        }

        UriTemplate template = new UriTemplate("?" + query.toString().substring(1));
        return template.expand(singles).toString();
    }

    protected HttpRequest buildHttpRequest(String verb, String uri,
                                           InputStreamEntity entity, MultiValueMap<String, String> headers,
                                           MultiValueMap<String, String> params, HttpServletRequest request) {
        HttpRequest httpRequest;
        String uriWithQueryString = uri + (false
                ? getEncodedQueryString(request) : this.getQueryString(params));

        switch (verb.toUpperCase()) {
            case "POST":
                HttpPost httpPost = new HttpPost(uriWithQueryString);
                httpRequest = httpPost;
                httpPost.setEntity(entity);
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut(uriWithQueryString);
                httpRequest = httpPut;
                httpPut.setEntity(entity);
                break;
            case "PATCH":
                HttpPatch httpPatch = new HttpPatch(uriWithQueryString);
                httpRequest = httpPatch;
                httpPatch.setEntity(entity);
                break;
            case "DELETE":
                BasicHttpEntityEnclosingRequest entityRequest = new BasicHttpEntityEnclosingRequest(
                        verb, uriWithQueryString);
                httpRequest = entityRequest;
                entityRequest.setEntity(entity);
                break;
            default:
                httpRequest = new BasicHttpRequest(verb, uriWithQueryString);
                log.debug(uriWithQueryString);
        }

        httpRequest.setHeaders(convertHeaders(headers));
        return httpRequest;
    }

    private Header[] convertHeaders(MultiValueMap<String, String> headers) {
        List<Header> list = new ArrayList<>();
        for (String name : headers.keySet()) {
            for (String value : headers.get(name)) {
                list.add(new BasicHeader(name, value));
            }
        }
        return list.toArray(new BasicHeader[0]);
    }
}
