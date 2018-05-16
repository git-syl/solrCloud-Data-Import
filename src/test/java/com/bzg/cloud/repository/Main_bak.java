package com.bzg.cloud.repository;

import com.bzg.cloud.entity.BrandInfo;
import com.bzg.cloud.utils.BasicAuthSolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.ConfigSetAdminRequest;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: syl  Date: 2018/4/19 Email:nerosyl@live.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Main_bak {

 //   @Autowired
    //private SolrClient solrClient;

   @Autowired
   BasicAuthSolrClient solrClient;

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
//        SolrInputDocument document = new SolrInputDocument();
//        document.addField("id", (int)(Math.random()*1000));
//        document.addField("end_status", "测试天气预报今天有雨"+Math.random());
//      //  document.addField("upassword", "测试" + Math.random());
//        solrClient.add(document);
//        solrClient.commit();
//        System.err.println("solr");
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "545454");
        document.addField("end_status","1000" );
        solrClient.add(document);
        solrClient.commit();
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @Test
    public void addDetalDocument() throws IOException, SolrServerException {
        //查询数据库,saveList对象,对比update

        List<BrandInfo> brandInfoData = repository.findAll();
        System.err.println(brandInfoData.size());
        SolrQuery query = new SolrQuery();

        query.set("q", "*:*");
        QueryResponse response = solrClient.query(query);
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

        for (BrandInfo brandInfo : brandInfoSolrData) {
            BrandInfo infoOfDBData = repository.findBrandInfoByRegisterNumAndCategoryId(brandInfo.getRegisterNum(), brandInfo.getCategoryId());
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

        }
        solrClient.commit();

    }


    @Test
    public void addMutilData() throws IOException, SolrServerException {
        System.out.println("==========================Memory=========================");
        MemoryMXBean memoryMBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage usage = memoryMBean.getHeapMemoryUsage();
        System.out.println("Init Heap: " + (usage.getInit()/1024/1024) + "mb");
        System.out.println("Max Heap: " + (usage.getMax()/1024/1024) + "mb");

        List<BrandInfo> brandInfoData = repository.findAll(new PageRequest(0,500000)).getContent();

        //delete....old
        for (BrandInfo dataDb : brandInfoData) {
            solrClient.deleteByQuery(String.format("category_id:%s AND registr_num:%s",dataDb.getCategoryId(),dataDb.getRegisterNum()));
        }

        //add ...new
        List<SolrInputDocument> solrInputDocumentList = new ArrayList<>();
        for (BrandInfo infoOfDBData : brandInfoData) {
            //BrandInfo infoOfDBData = repository.findBrandInfoByRegisterNumAndCategoryId(brandInfo.getRegisterNum(), brandInfo.getCategoryId());
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
       //     solrClient.add(document);
            solrInputDocumentList.add(document);
        }
        solrClient.add(solrInputDocumentList);

        solrClient.commit();
    }

    @Test
    public void queryByCategoryAndRegisterNum() throws IOException, SolrServerException {

        SolrQuery query = new SolrQuery();
        query.set("q", "*:*");
        query.addFilterQuery("category_id:3 AND registr_num:4437734");
      //  query.addFilterQuery("category_id:3");
      //  query.addFilterQuery("registr_num:4437734");
//        SolrClient proxy = (SolrClient) new ProxyFactory(solrClient).getProxyInstance();
        QueryResponse response = solrClient.query(query);
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
        solrClient.deleteByQuery("category_id:3 AND registr_num:4437734");
        solrClient.commit();
    }

    @Test
    public void deleteDocumentByQuery2() throws IOException, SolrServerException {

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
    //创建一个核心
    @Test
    public void test1() throws IOException, SolrServerException {
        CollectionAdminRequest.Create create = CollectionAdminRequest.createCollection("dgaga","element_all_new",1,1);
        create.setBasicAuthCredentials(USERNAME, PASSWORD);
        NamedList<Object> result =solrClient.request(create);
    }
    @Value("${solr.cloud.security.username}")
    private String USERNAME;
    @Value("${solr.cloud.security.password}")
    private String PASSWORD ;
}
