package com.bzg.cloud.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * @author: syl  Date: 2018/4/8 Email:nerosyl@live.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

  //  @Autowired
   // UserRepository userRepository;

    @Test
    public void findByToken() {
    }

    @Test
    public void findByMobile() {
    }

    @Test
    public void findByUserName() {
    }

    @Test
    public void findPasswordAndUserName() {
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void findByEmail() {
    }

    @Test
    public void findPasswordAndPhone() {
    }

    @Test
    public void findPasswordAndEmail() {
    }

    @Test
    public void findByQQOpenId() {
    }

    @Test
    public void findByWechatOpenId() {
    }

    @Test
    public void updatePassword() {
    }

    @Test
    @Transactional
    public void updateEmail() {
//        int i = userRepository.updateEmail("510806100@qq.com", "4028815562a4afcb0162a4e8f8ec0000");
//        System.out.println(i);
    }

    @Test
    public void findUserAndUserInfo() {
    }
}