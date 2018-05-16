package com.bzg.cloud.repository;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author: syl  Date: 2018/4/17 Email:nerosyl@live.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SolrCloudTest {



    //@Before
    public void getServer() {
        //clientPort=2182
//        final String zkHost = ":2181,:2182,:2183";
//        final String defaultCollection = "customer";
//        //  final String defaultCollection = "myconf";
//        final int zkClientTimeout = 20000;
//        final int zkConnectTimeout = 10000;
//        server = new CloudSolrClient(zkHost);
//        server.setDefaultCollection(defaultCollection);
//        server.setZkClientTimeout(zkClientTimeout);
//        server.setZkConnectTimeout(zkConnectTimeout);
//        server.connect();
    }

    @Test
    public void addIndex() throws IOException, SolrServerException {
        final String zkHost = "localhost:2181,localhost:2182,localhost:2183";
       //final String defaultCollection = "customer";
        final String defaultCollection = "customer";
        //final String defaultCollection = "new_core3";
      // final String defaultCollection = "customer_shard1_replica2";

        CloudSolrClient server;   server = new CloudSolrClient(zkHost);
        server.setDefaultCollection(defaultCollection);
      //  server.connect();
        SolrInputDocument doc1 = new SolrInputDocument();
        doc1.addField("id", new Date());
        doc1.addField("username,", new Date());
        server.add(doc1);
        server.commit();
    }

//    SolrInputDocument doc2 = new SolrInputDocument();
//        doc2.addField("id", "1002");
//        doc2.addField("name_s", "p2");
//        doc1.addField("username,", "ttest file");
//
//    SolrInputDocument doc3 = new SolrInputDocument();
//        doc3.addField("id", "1003");
//        doc3.addField("name_s", "p3");
//        doc1.addField("username,", "ttest file");
//           docs.add(doc2);
//        docs.add(doc3);

    @Test
    public void test1() throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        query.set("q", "*:*");
        final String zkHost = "localhost:2181,localhost:2182,localhost:2183";
        final String defaultCollection = "customer";
        CloudSolrClient    server = new CloudSolrClient(zkHost);
        server.setDefaultCollection(defaultCollection);
     //   server.connect();
        QueryResponse queryResponse = server.query(query);
        SolrDocumentList results = queryResponse.getResults();
        for (SolrDocument result : results) {
            System.err.println("id:"+result.get("id"));
            System.err.println("username:"+result.get("username"));
        }

    }
}
