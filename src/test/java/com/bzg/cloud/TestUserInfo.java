package com.bzg.cloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: syl  Date: 2018/4/8 Email:nerosyl@live.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserInfo {
  //  @Autowired
    ///UserRepository userRepository;

  //  @Autowired
   // EntityManager entityManager;


    @Test
    @Transactional
    public void test2() {

    //    List<UserInfoSummyClass> toUserInfoSummyClassN1 = userRepository.findToUserInfoSummyClassN();

//        for (UserInfoSummyClass aClass : toUserInfoSummyClassN1) {
//            System.out.println(aClass.getMobile());
//            System.out.println(aClass.getHeadurl());
//        }


    }


    @Test
    public void test1() {

//        List<UserInfoSummy> userList = userRepository.findUserAndUserInfo();
//        for (UserInfoSummy userInfoSummy : userList) {
//            System.out.println(userInfoSummy);
//        }

    }

    @Test
    @SuppressWarnings("all")
    public void test3() {
//        List<Object[]> resultList = entityManager.createNativeQuery("SELECT i.token AS utoken,u.token,i.company_type FROM  t_user_regist u LEFT JOIN t_user_info i ON  u.token = i.token")
//                .setFirstResult(0).setMaxResults(5).getResultList();
//        for (Object[] objects : resultList) {
//            System.out.println(objects[0]);
//            System.out.println(objects[1]);
//            System.out.println(objects[2]);
//        }

    }

    @Test
    public void test4() throws ParseException {
        String string = "2016-10-24";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        System.out.println(sdf.parse(string));
      //  ChronoUnit.DAYS.between(sdf.parse(string), sdf.parse(string));
        long convert = TimeUnit.DAYS.convert(System.currentTimeMillis() - sdf.parse(string).getTime(), TimeUnit.MILLISECONDS);
        System.out.println(convert);
    }



}
