package com.xiangxue.jack.service;

import com.alibaba.fastjson.JSONObject;
import com.xiangxue.jack.bean.ConsultContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class UserServiceImpl implements UserService {

    private AtomicInteger s = new AtomicInteger();
    private AtomicInteger f = new AtomicInteger();

    public static String SERVIER_NAME = "micro-order";

    @Autowired
    private RestTemplate restTemplate;

    /**
     *  Command属性
     *  execution.isolation.strategy  执行的隔离策略
     *  THREAD 线程池隔离策略  独立线程接收请求
     *  SEMAPHORE 信号量隔离策略 在调用线程上执行
     *
     *  execution.isolation.thread.timeoutInMilliseconds  设置HystrixCommand执行的超时时间，单位毫秒
     *  execution.timeout.enabled  是否启动超时时间，true，false
     *  execution.isolation.semaphore.maxConcurrentRequests  隔离策略为信号量的时候，该属性来配置信号量的大小，最大并发达到信号量时，后续请求被拒绝
     *
     *  circuitBreaker.enabled   是否开启断路器功能
     *  circuitBreaker.requestVolumeThreshold  该属性设置在滚动时间窗口中，断路器的最小请求数。默认20，如果在窗口时间内请求次数19，即使19个全部失败，断路器也不会打开
     *  circuitBreaker.sleepWindowInMilliseconds    改属性用来设置当断路器打开之后的休眠时间，休眠时间结束后断路器为半开状态，断路器能接受请求，如果请求失败又重新回到打开状态，如果请求成功又回到关闭状态
     *  circuitBreaker.errorThresholdPercentage  该属性设置断路器打开的错误百分比。在滚动时间内，在请求数量超过circuitBreaker.requestVolumeThreshold,如果错误请求数的百分比超过这个比例，断路器就为打开状态
     *  circuitBreaker.forceOpen   true表示强制打开断路器，拒绝所有请求
     *  circuitBreaker.forceClosed  true表示强制进入关闭状态，接收所有请求
     *
     *  metrics.rollingStats.timeInMilliseconds   设置滚动时间窗的长度，单位毫秒。这个时间窗口就是断路器收集信息的持续时间。断路器在收集指标信息的时会根据这个时间窗口把这个窗口拆分成多个桶，每个桶代表一段时间的指标，默认10000
     *  metrics.rollingStats.numBuckets   滚动时间窗统计指标信息划分的桶的数量，但是滚动时间必须能够整除这个桶的个数，要不然抛异常
     *
     *  requestCache.enabled   是否开启请求缓存，默认为true
     *  requestLog.enabled 是否打印日志到HystrixRequestLog中，默认true
     *
     *  @HystrixCollapser   请求合并
     *  maxRequestsInBatch  设置一次请求合并批处理中允许的最大请求数
     *  timerDelayInMilliseconds  设置批处理过程中每个命令延迟时间
     *  requestCache.enabled   批处理过程中是否开启请求缓存，默认true
     *
     *  threadPoolProperties
     *  threadPoolProperties 属性
     *  coreSize   执行命令线程池的最大线程数，也就是命令执行的最大并发数，默认10
     *
     */
/*    @HystrixCommand(fallbackMethod = "queryContentsFallback",
            commandKey = "queryContents",
            groupKey = "querygroup-one",
            commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1000000000")
            },
            threadPoolKey = "queryContentshystrixJackpool",threadPoolProperties = {
            @HystrixProperty(name = "coreSize",value = "100")
    })*/
    @Override
    public List<ConsultContent> queryContents() {
        log.info("========queryContents=========");
        s.incrementAndGet();
        List<ConsultContent> results = restTemplate.getForObject("http://"
                + SERVIER_NAME + "/user/queryContent", List.class);
        return results;
    }

//    @Retryable
    @Override
    public List<ConsultContent> queryContent() {
        log.info("========queryContent=========");
        List<ConsultContent> results = restTemplate.getForObject("http://"
                + SERVIER_NAME + "/user/queryContent", List.class);
        return results;
    }

    public List<ConsultContent> queryContentsFallback() {
        f.incrementAndGet();
        log.info("===============queryContentsFallback=================");
        return null;
    }

    @Override
    public String queryMonitor() {
        JSONObject jo = new JSONObject();
        jo.put("成功数",s.get());
        jo.put("失败数",f.get());
        return jo.toJSONString();
    }
}
