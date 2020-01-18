package com.tuling.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author wangjinpeng
 * created by wangjinpeng 20200118
 */
public class DataTest {
    ZooKeeper zooKeeper;
    @Before
    public void init() throws IOException {
        String conn="192.168.2.11:2181";
        //sessionTimeout 太小会出现
        //一般是由于连接还未完成就执行zookeeper的get/create/exsit操作引起的.
        //org.apache.zookeeper.KeeperException$ConnectionLossException: KeeperErrorCode = ConnectionLoss for /tuling
       //解决方法:
        //
        //利用"CountDownLatch 类 + zookeeper的watcher + zookeeper的getStat" 实现连接完成后再调用.
        //
        //可防止此错误发生.

        zooKeeper=new ZooKeeper(conn, 400000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.getPath());
                System.out.println(watchedEvent);
            }
        });
    }
    @Test
    public void getData() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData("/tuling", false, null);
        //System.out.println(data);  //会出现 [B@5ba23b66
        System.out.println(new String(data));  //正常值 tulingcolleage
    }

}
