package com.bzg.cloud.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: syl  Date: 2018/4/11 Email:nerosyl@live.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageRepositoryTest {

//    @Autowired
//    MessageRepository messageRepository;
//
//    @Test
//    public void findMessageByToUserTokenAndStatusLessThanEqualOrderBySendTimeDesc() {
//        Page<Message> message = messageRepository.findMessageByToUserTokenAndStatusLessThanEqualOrderBySendTimeDesc("4028815562a4afcb0162a4e8f8ec0000", 1, new PageRequest(0, 10));
//        System.out.println("总页数"+message.getTotalPages());
//        System.out.println("总记录数目"+message.getTotalElements());
//        System.out.println("当前页面"+(message.getNumber()+1));
//        System.out.println("当前页面的内容"+message.getContent());
//        System.out.println("当前页面的记录数目"+message.getNumberOfElements());
//    }
}