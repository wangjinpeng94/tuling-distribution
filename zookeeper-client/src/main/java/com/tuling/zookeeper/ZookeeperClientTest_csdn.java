package com.tuling.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperClientTest_csdn {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        //由于连接zk需要时间，所以这里使用countDownLatch
        final CountDownLatch countDownLatch=new CountDownLatch(1);
        ZooKeeper client=new ZooKeeper("192.168.2.11:2181", 10000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (Event.KeeperState.SyncConnected.equals(watchedEvent.getState())) {
                    System.out.println("连接成功了"+watchedEvent);
                    countDownLatch.countDown();

                }

                System.out.println(watchedEvent);
            }
        });
        if (ZooKeeper.States.CONNECTING.equals(client.getState())) {
            System.out.println("连接中");
            countDownLatch.await();
        }
        Stat stat=new Stat();
        byte [] bytes=client.getData("/tuling", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (Event.EventType.NodeChildrenChanged.equals(watchedEvent.getType())) {
                    System.out.println("数据改变了");
                }
            }
        },stat);
        String s=new String(bytes);
        System.out.println(s);
        System.in.read();
    }

}
