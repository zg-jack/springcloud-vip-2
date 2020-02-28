package com.xiangxue.jack.test;

import com.netflix.client.ClientException;
import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.niws.client.http.RestClient;
import com.xiangxue.jack.MicroWebApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MicroWebApplication.class)
@WebAppConfiguration
public class MyTest {

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
