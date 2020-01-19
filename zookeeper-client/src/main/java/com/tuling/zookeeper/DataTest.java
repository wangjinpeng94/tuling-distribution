package com.tuling.zookeeper;

import org.apache.jute.CsvOutputArchive;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    @Test
    public void getData2() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData("/tuling", true, null);
        System.out.println(new String(data));
        Thread.sleep(Long.MAX_VALUE);


    }
    @Test
    public void getData3() throws KeeperException, InterruptedException {
        Stat stat =new Stat();
        zooKeeper.getData("/tuling", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    zooKeeper.getData(watchedEvent.getPath(),this,null);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(watchedEvent.getPath());
            }
        },stat);
        System.out.println(stat);
        Thread.sleep(Long.MAX_VALUE);
    }
    @Test
    public void getData4() throws InterruptedException {
        zooKeeper.getData("/tuling", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
                System.out.println(stat);
            }
        },"");
        Thread.sleep(Long.MAX_VALUE);
    }
    @Test
    public void getChild() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren("/tuling", false);
        children.stream().forEach(System.out::println);
    }
    @Test
    public void createData() throws KeeperException, InterruptedException {
        ArrayList<ACL> list = new ArrayList<ACL>();
        int perm = ZooDefs.Perms.ADMIN | ZooDefs.Perms.READ;
        ACL acl=new ACL(perm,new Id("world","anyone"));
        ACL acl2=new ACL(perm,new Id("ip","192.168.2.11"));
        ACL acl3=new ACL(perm,new Id("ip","127.0.0.1"));
        list.add(acl);
        list.add(acl2);
        list.add(acl3);
        zooKeeper.create("/tuling/lu","hello".getBytes(),list,CreateMode.PERSISTENT);

    }
    @Test
    public void getChild2() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren("/tuling", watchedEvent -> {
            System.out.println(watchedEvent.getPath());
            try {
                zooKeeper.getChildren(watchedEvent.getPath(), false);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        children.stream().forEach(System.out::println);
        Thread.sleep(Long.MAX_VALUE);

    }





}
