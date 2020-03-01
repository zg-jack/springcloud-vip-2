package com.xiangxue.jack.test;

import com.netflix.client.ClientException;
import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.niws.client.http.RestClient;
import com.xiangxue.jack.MicroWebApplication;
import com.xiangxue.jack.service.UserService;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MicroWebApplication.class)
@WebAppConfiguration
public class MyTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Integer count = 11;

    private CountDownLatch cdl = new CountDownLatch(count);

    @Autowired
    UserService userService;

    @Rule
    public ContiPerfRule contiPerfRule = new ContiPerfRule();

    @Test
    public void hystrixTest() {

        for (Integer i = 0; i < count; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cdl.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    logger.info(Thread.currentThread().getName() + "==>" + userService.queryContents());
                }
            }).start();
            cdl.countDown();
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @PerfTest(invocations = 11,threads = 11)
    public void hystrixTest2() {
        logger.info(Thread.currentThread().getName() + "==>" + userService.queryContents());
    }

    /*
    * ribbon作为调用客户端，可以单独使用
    * */
    @Test
    public void test1() {
        try {
            ConfigurationManager.getConfigInstance().setProperty("myClients.ribbon.listOfServers","localhost:8001,localhost:8002");
            RestClient client = (RestClient)ClientFactory.getNamedClient("myClients");
            HttpRequest request = HttpRequest.newBuilder().uri(new URI("/user/queryContent")).build();

            for (int i = 0; i < 10; i++) {
                HttpResponse httpResponse = client.executeWithLoadBalancer(request);
                String entity = httpResponse.getEntity(String.class);
                System.out.println(entity);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
