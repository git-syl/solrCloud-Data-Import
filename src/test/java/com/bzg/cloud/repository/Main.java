package com.bzg.cloud.repository;

import com.bzg.cloud.entity.BrandInfo;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: syl  Date: 2018/4/19 Email:nerosyl@live.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Main {

    @Resource(name = "cloudSolrClient")
    private SolrClient solrClient;

    @Autowired
    EntityManager entityManager;

    @Autowired
    BrandInfoRepository repository;


//    @Before
//    public  void setup() throws IOException, SolrServerException {
//        UpdateRequest req = new UpdateRequest();
//        req.setBasicAuthCredentials("tom", "TomIsCool");
//
//    }

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        List<Object[]> resultList =
                entityManager.createNativeQuery("SELECT i.token AS utoken,u.token,i.company_type FROM  t_user_regist u LEFT JOIN t_user_info i ON  u.token = i.token")
                        .setFirstResult(0).setMaxResults(5).getResultList();
    }

    @Test
    public void addDocument() throws IOException, SolrServerException {

        UpdateRequest req = new UpdateRequest();
        req.setBasicAuthCredentials("tom", "TomIsCool");
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", Math.random());
        document.addField("end_status", "test");
        SolrInputDocument document2 = new SolrInputDocument();
        document2.addField("id", Math.random());
        document2.addField("end_status", "test");
        req.add(document);
        req.add(document2);
        req.setCommitWithin(10000);
        req.process(solrClient);
        // solrClient.commit();
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @Test
    public void addDetalDocument() throws IOException, SolrServerException {
        //查询数据库,saveList对象,对比update
        List<BrandInfo> brandInfoData = repository.findAll();
        System.err.println(brandInfoData.size());

        SolrQuery query = new SolrQuery();
        QueryRequest req = new QueryRequest(query);
        req.setBasicAuthCredentials("tom", "TomIsCool");

        query.set("q", "*:*");
        QueryResponse response = req.process(solrClient);//solrClient.query(query);
        SolrDocumentList solrDocumentList = response.getResults();
        List<BrandInfo> brandInfoSolrData = new ArrayList<>();
        for (SolrDocument entries : solrDocumentList) {
            BrandInfo info = new BrandInfo();
            info.setId(String.valueOf(entries.get("id")));
            info.setEndStatus(String.valueOf(entries.get("end_status")));
            info.setRegisterNum(String.valueOf(entries.get("registr_num")));
            info.setAgentName(String.valueOf(entries.get("agent_name")));
            info.setProposer(String.valueOf(entries.get("proposer")));
            info.setRegAnncDate(String.valueOf(entries.get("reg_annc_date")));
            info.setProposerAddr(String.valueOf(entries.get("proposer_addr")));
            info.setTmName(String.valueOf(entries.get("tm_name")));
            info.setPropertyEndDate(String.valueOf(entries.get("property_end_date")));
            info.setApplyDate(String.valueOf(entries.get("apply_date")));
            info.setCategoryId(String.valueOf(entries.get("category_id")));
            info.setFirstAnncDate(String.valueOf(entries.get("first_annc_date")));
            info.setStatus(String.valueOf(entries.get("status")));
            brandInfoSolrData.add(info);
        }

        System.err.println("for-end");
        brandInfoSolrData.removeAll(brandInfoData);

        System.err.println(String.format("===================有%d条数据变化================", brandInfoSolrData.size()));

        UpdateRequest requp = new UpdateRequest();
        requp.setBasicAuthCredentials("tom", "TomIsCool");
        boolean hasChangingData = false;
        for (BrandInfo brandInfo : brandInfoSolrData) {
            BrandInfo infoOfDBData = repository.findBrandInfoByRegisterNumAndCategoryId(brandInfo.getRegisterNum(), brandInfo.getCategoryId());
            if (infoOfDBData == null) {
                System.err.println("infoOfDBData null");
                continue;
            }
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", infoOfDBData.getId());
            document.addField("end_status", infoOfDBData.getEndStatus());
            document.addField("registr_num", infoOfDBData.getRegisterNum());
            document.addField("agent_name", infoOfDBData.getAgentName());
            document.addField("proposer", infoOfDBData.getProposer());
            document.addField("reg_annc_date", infoOfDBData.getRegAnncDate());
            document.addField("proposer_addr", infoOfDBData.getProposerAddr());
            document.addField("tm_name", infoOfDBData.getTmName());
            document.addField("property_end_date", infoOfDBData.getPropertyEndDate());
            document.addField("apply_date", infoOfDBData.getApplyDate());
            document.addField("category_id", infoOfDBData.getCategoryId());
            document.addField("first_annc_date", infoOfDBData.getFirstAnncDate());
            document.addField("status", infoOfDBData.getStatus());
            solrClient.add(document);
            requp.add(document);
            hasChangingData = true;
        }
        if (hasChangingData) {
            requp.setCommitWithin(10000);
            requp.process(solrClient);
        }

        //solrClient.commit();
//          UpdateRequest  req = new UpdateRequest();
//        req.setBasicAuthCredentials("tom", "TomIsCool");
//        SolrInputDocument document=new SolrInputDocument();
//        document.addField("id", Math.random()*100);
//        document.addField("end_status", "test");
//        req.add(document);
//        req.setCommitWithin(10000);
//        req.process(solrClient);

    }

    @Test
    public void queryByCategoryAndRegisterNum() throws IOException, SolrServerException {

        SolrQuery query = new SolrQuery();
        QueryRequest req = new QueryRequest(query);
        req.setBasicAuthCredentials("tom", "TomIsCool");

        query.set("q", "*:*");
        query.addFilterQuery("category_id:3");
        query.addFilterQuery("registr_num:4437734");

        QueryResponse response = req.process(solrClient);
        SolrDocumentList solrDocumentList = response.getResults();
    }


    @Test
    public void testDeleteDocumentById() throws IOException, SolrServerException {
        solrClient.deleteById("0.8178647560629303");
        solrClient.commit();
    }

    @Test
    public void deleteDocumentByQuery() throws IOException, SolrServerException {

        //警告根据分词的名称删除会导致删除多个包含这个文字的商品!
        //solrClient.deleteByQuery("username:测试");
        solrClient.deleteByQuery("*:*");
        solrClient.commit();
    }

    @Test
    public void searchDocument() throws SolrServerException, IOException {

        SolrQuery query = new SolrQuery();
        query.set("q", "*:*");
        // query.setQuery("测试");
        //分页条件
        query.setStart(0);
        query.setRows(10);
        //设置默认搜索余
        query.set("df", "end_status");
        query.setHighlight(true);//高亮
        query.addHighlightField("end_status");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");

        QueryResponse response = solrClient.query(query);
        SolrDocumentList solrDocumentList = response.getResults();

        System.out.println("总记录数目" + solrDocumentList.getNumFound());

        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        String id = "";
        for (SolrDocument solrDocument : solrDocumentList) {
            id = String.valueOf(solrDocument.get("id"));

            List<String> list = highlighting.get(id).get("end_status");
            String username = "";
            if (list != null && list.size() > 0) {
                username = list.get(0);
            } else {
                username = (String) solrDocument.get("end_status");
                System.out.println("list is null");
            }

            System.out.println(username);
            System.out.println(solrDocument.get("id"));
            System.out.println("********************");
        }
    }
}
