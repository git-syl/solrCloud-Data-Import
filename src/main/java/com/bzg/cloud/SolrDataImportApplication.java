package com.bzg.cloud;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.apache.solr.client.solrj.request.SolrPing;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.common.params.ModifiableSolrParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import java.io.IOException;



/**
 * @author  syl  Date: 2018/5/11 Email:nerosyl@live.com
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SolrDataImportApplication {

    private static final Logger log = LoggerFactory.getLogger(SolrDataImportApplication.class);

    @Value("${solr.cloud.zkHost}")
    private String zkHost;

    @Value("${solr.cloud.defaultCollection}")
    private String defaultCollection;

    @Value("${solr.cloud.security.username}")
    private String userName;
    @Value("${solr.cloud.security.password}")
    private String password;

    public static void main(String[] args) {
        SpringApplication.run(SolrDataImportApplication.class, args);
    }


    //@Bean 单机环境
//    public HttpSolrClient httpSolrClient() {
////		new HttpSolrClient.Builder(urlString).build();
//        //	return new HttpSolrClient("http://localhost:8081/solr/customer");
//        return new HttpSolrClient.Builder("http://localhost:8081/solr/element_all").build();
//
//    }

    //集群
    @Bean
    public CloudSolrClient cloudSolrClient() {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set(HttpClientUtil.PROP_BASIC_AUTH_USER, userName);
        params.set(HttpClientUtil.PROP_BASIC_AUTH_PASS, password);
        //        params.set(HttpClientUtil.PROP_MAX_CONNECTIONS, 128);
//        params.set(HttpClientUtil.PROP_MAX_CONNECTIONS_PER_HOST, 32);
//        params.set(HttpClientUtil.PROP_FOLLOW_REDIRECTS, false);
        //        params.set(HttpClientUtil.PROP_MAX_CONNECTIONS, 1000);
//        params.set(HttpClientUtil.PROP_ALLOW_COMPRESSION, true);
//        params.set(HttpClientUtil.PROP_MAX_CONNECTIONS_PER_HOST, 1000);

        CloseableHttpClient closeableHttpClient = HttpClientUtil.createClient(params);
        CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder().withZkHost(zkHost).withHttpClient(closeableHttpClient).build();
        cloudSolrClient.setDefaultCollection(defaultCollection);
        try {
            SolrPing ping = new SolrPing();
            ping.setBasicAuthCredentials(userName, password);
            SolrPingResponse pingResponse = ping.process(cloudSolrClient);
            if (pingResponse.getStatus() != 0) {
              log.info("Connection Solr-Server Error (Status-Code ist " + pingResponse.getStatus() + ")");
            } else {
                log.info("Connection success Ping  " + pingResponse.getQTime() + " ms");
            }
        } catch (SolrServerException | IOException e) {
           log.info("connection error",e);
        }
        return cloudSolrClient;
    }




}
