package com.xiangxue.jack.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class CuratorUtil implements ApplicationContextAware {

    private static String connnectStr = "192.168.67.139:2181";

    private static CuratorFramework client;

    private static String path = "/config";

    @Value(("${zookeeper.config.enable:false}"))
    private boolean enbale;

    @Autowired
    Environment environment;

    private static ConfigurableApplicationContext applicationContext;

    private ConcurrentHashMap map = new ConcurrentHashMap();

    @PostConstruct
    public void init() {
        if(!enbale) return;
        client = CuratorFrameworkFactory.
                builder().
                connectString(connnectStr).
                sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000, 3)).
                build();

        client.start();
        try {
            Stat stat = client.checkExists().forPath(path);
//            client.delete().withVersion(-1).forPath(path);
            if(stat == null) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).
                        forPath(path,"zookeeper config".getBytes());
                TimeUnit.SECONDS.sleep(1);
            }

            nodeCache(client,path);
            childNodeCache(client,path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void childNodeCache(CuratorFramework client, String path) {
        try {
            final PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, false);
            pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                    switch (event.getType()) {
                        case CHILD_ADDED:
                            System.out.println("增加了节点");
                            Iterator<ChildData> iterator = pathChildrenCache.getCurrentData().iterator();
                            addEnv(iterator,client);
                            break;
                        case CHILD_REMOVED:
                            System.out.println("删除了节点");
                            break;
                        case CHILD_UPDATED:
                            System.out.println("更新了节点");
                            break;
                        default:
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEnv(Iterator<ChildData> iterator,CuratorFramework client) {
        while(iterator.hasNext()) {
            ChildData next = iterator.next();
            String path = next.getPath();
            String data = null;
            try {
                data = new String(client.getData().forPath(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
            MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
            for (PropertySource<?> propertySource : propertySources) {
                if("zookeeper source".equals(propertySource.getName())) {
                    OriginTrackedMapPropertySource ps = (OriginTrackedMapPropertySource)propertySource;
                    ConcurrentHashMap chm = (ConcurrentHashMap)ps.getSource();
                    chm.put(path.replace("/", ""),data);
                }
            }
        }
    }

    private void nodeCache(final CuratorFramework client, final String path) {

        try {
            //第三个参数是是否压缩
            //就是对path节点进行监控，是一个事件模板
            final NodeCache nodeCache = new NodeCache(client, path, false);
            nodeCache.start();

            //这个就是事件注册
            nodeCache.getListenable().addListener(new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    byte[] data = nodeCache.getCurrentData().getData();
                    String path1 = nodeCache.getCurrentData().getPath();

                    Object put = map.put(path1.replace("/", ""),new String(data));
                    MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
                    OriginTrackedMapPropertySource zookeeperSource = new OriginTrackedMapPropertySource("zookeeper source", map);
                    propertySources.addLast(zookeeperSource);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        CuratorUtil.applicationContext = (ConfigurableApplicationContext)context;
    }
}
