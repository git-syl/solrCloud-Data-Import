package com.bzg.cloud.repository;

import com.bzg.cloud.vo.GoldCoinRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static org.junit.Assert.*;

/**
 * @author: syl  Date: 2018/4/11 Email:nerosyl@live.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoldCoinFlowRepositoryTest {




    @Test
    public void test() {
        CyclicBarrier barrier = new CyclicBarrier(5, new Runnable() {
            @Override
            public void run() {
                GoldCoinRequest goldCoinRequest = new GoldCoinRequest();
                goldCoinRequest.setCount(-1000);
                goldCoinRequest.setToken("4028815562a4afcb0162a4e8f8ec0000");
                //coinService.updateGoldCoins(goldCoinRequest);
                System.out.println("go on together!");
            }
        });

        for (int i = 1; i <= 5; i++) {
            new Thread(new CyclicBarrierWorker(i, barrier)).start();
        }

    }
    @Test
    public void test2() {

                GoldCoinRequest goldCoinRequest = new GoldCoinRequest();
                goldCoinRequest.setCount(9999);
                goldCoinRequest.setToken("4028815562a4afcb0162a4e8f8ec0000");
             //   coinService.updateGoldCoins(goldCoinRequest);


    }


    class CyclicBarrierWorker implements Runnable {
        private int id;
        private CyclicBarrier barrier;

        public CyclicBarrierWorker(int id, final CyclicBarrier barrier) {
            this.id = id;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(id + "wait");
                barrier.await(); // 大家等待最后一个线程到达
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

//    public class TestCyclicBarrier {
//        public static void main(String[] args) {
//            int num = 10;
//            CyclicBarrier barrier = new CyclicBarrier(num, new Runnable() {
//                @Override
//                public void run() {
//                    // TODO Auto-generated method stub
//                    System.out.println("go on together!");
//                }
//            });
//            for (int i = 1; i <= num; i++) {
//                new Thread(new CyclicBarrierWorker(i, barrier)).start();
//            }
//        }
//    }
}