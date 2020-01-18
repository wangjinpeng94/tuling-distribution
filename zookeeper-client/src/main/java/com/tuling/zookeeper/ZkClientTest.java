package com.tuling.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.junit.Before;

/**
 * @author wangjinpeng
 * created by wangjinpeng 20200118
 */
public class ZkClientTest {
    ZkClient zkClient;
    @Before
    public void init(){
         zkClient = new ZkClient("192.168.2.11:2181", 5000, 5000);
    }
    public void createTest(){

    }


}
