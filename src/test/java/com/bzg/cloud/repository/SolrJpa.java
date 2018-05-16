package com.bzg.cloud.repository;

import com.bzg.cloud.entity.BrandInfo;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: syl  Date: 2018/4/19 Email:nerosyl@live.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SolrJpa {

//    //  @Autowired
//    // @Qualifier(value = "solrClient")
////   @Resource(name = "solrClient")
//    @Autowired
//    private SolrClient solrClient;
//
//    // @Autowired
//    BasicAuthSolrClient solrClientAuth;

    @Autowired
    EntityManager entityManager;

    @Autowired
    BrandInfoRepository repository;

    @Test
    public void testAdd() throws IOException, SolrServerException {
//        BrandInfoSolr info = new BrandInfoSolr();
//        info.setId("userjpa");
//        info.setEndStatus("useriiiiin");
//        solrClient.addBean(info);
//        solrClient.commit();

    }

    @Test
    public void testJPa() throws IOException, SolrServerException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BrandInfo> criteriaQuery = criteriaBuilder.createQuery(BrandInfo.class);
          Root<BrandInfo> from = criteriaQuery.from(BrandInfo.class);
//         Predicate condition = criteriaBuilder.gt(from.get(Employee_.age), 24);
//            criteriaQuery.where(condition);
       // criteriaQuery.orderBy(criteriaBuilder.asc(from.get()));
        TypedQuery<BrandInfo> query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(100);
        query.setFirstResult(0);
        List<BrandInfo> result = query.getResultList();

    }
    @Test
    public void testThread(){
        int processors = Runtime.getRuntime().availableProcessors();
        int NIO = 2*Runtime.getRuntime().availableProcessors()+1;
        int CPU = Runtime.getRuntime().availableProcessors()+1;
        System.out.println(processors);
        List<BrandInfo> db = new ArrayList<>(16);
        ExecutorService executorService = Executors.newFixedThreadPool(NIO);

        for (int i=0;i<NIO;i++){
            int finalI = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    int per = db.size()/4;
                    for (int j=0;j<per;j++){
                        System.out.println(finalI);
                    }
                 }
            });
        }

    }


}
