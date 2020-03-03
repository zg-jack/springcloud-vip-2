package com.xiangxue.jack.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.commons.httpclient.DefaultApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.DefaultApacheHttpClientFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Component
public class RoutingFilter extends ZuulFilter {

    private ApacheHttpClientConnectionManagerFactory connectionManagerFactory;

    private ApacheHttpClientFactory httpClientFactory;

    private CloseableHttpClient httpClient;

    private HttpClientConnectionManager connectionManager;

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

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        String uri = buildZuulRequestURI(request);
        return true;
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

    private String buildZuulRequestURI(HttpServletRequest request) {
        return null;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        StringBuilder sb = currentContext.getFilterExecutionSummary();
        return null;
    }
}
