package com.bzg.cloud.utils;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author: syl  Date: 2018/4/27 Email:nerosyl@live.com
 */
@Component
public/* abstract */  class BasicAuthSolrClient extends SolrClient {

    //  @Autowired
    //  @Qualifier(value = "solrClient2")
    //  @Resource(name="solrClient2")
    private SolrClient solrClient;

    @Value("${solr.cloud.security.username}")
    private String USERNAME;
    @Value("${solr.cloud.security.password}")
    private String PASSWORD;

    private static final Logger log = LoggerFactory.getLogger(BasicAuthSolrClient.class);

    @Autowired
    // @Qualifier(value = "solrClient2")
    public BasicAuthSolrClient(SolrClient solrClient) {
        this.solrClient = solrClient;
    }

    @Override
    public QueryResponse query(SolrParams params) throws SolrServerException, IOException {
        QueryRequest req = new QueryRequest(params);
        req.setBasicAuthCredentials(USERNAME, PASSWORD);
        return req.process(solrClient);
    }

    @Override
    public QueryResponse query(String collection, SolrParams params) throws SolrServerException, IOException {
        QueryRequest req = new QueryRequest(params);
        req.setBasicAuthCredentials(USERNAME, PASSWORD);
        return req.process(solrClient, collection);
    }

    @Override
    public NamedList<Object> request(SolrRequest request, String collection) throws SolrServerException, IOException {
        return solrClient.request(request, collection);
    }


    public void add(List<SolrInputDocument> documentList) {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setBasicAuthCredentials(USERNAME, PASSWORD);

        updateRequest.add(documentList);
        // updateRequest.setAction(AbstractUpdateRequest.ACTION.COMMIT, false, false);
        // updateRequest.setCommitWithin(30000);
        try {
            updateRequest.process(solrClient);
            log.info(documentList.size() + "-->Batch add index success!");
        } catch (SolrServerException | IOException e) {
            log.error("Batch add index failed : ", e);
            updateRequest.rollback();
        }
    }

    @Override
    public UpdateResponse add(SolrInputDocument doc) {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setBasicAuthCredentials(USERNAME, PASSWORD);
        updateRequest.add(doc);
        //   updateRequest.setCommitWithin(30000);
        // updateRequest.setAction(AbstractUpdateRequest.ACTION.COMMIT, true,false,true);
        try {
            log.info("add Index success!");
            return updateRequest.process(solrClient);
        } catch (SolrServerException | IOException e) {
            log.error("add Index failed!", e.getMessage());
            updateRequest.rollback();
            return null;
        }
    }

    @Override
    public UpdateResponse deleteByQuery(String query) throws SolrServerException, IOException {
        UpdateRequest req = new UpdateRequest();
        req.setBasicAuthCredentials(USERNAME, PASSWORD);
        req.deleteByQuery(query);
        //  req.setCommitWithin(30000);
        return req.process(this);
    }

    @Override
    public UpdateResponse commit() throws SolrServerException, IOException {
        return null; //super.commit();
    }


    public NamedList<Object> createCollection(String collection, String config, int numShards, int numReplicas) throws IOException, SolrServerException {
        CollectionAdminRequest.Create create = CollectionAdminRequest.createCollection(collection, config, numShards, numReplicas);
        create.setBasicAuthCredentials(USERNAME, PASSWORD);
        return solrClient.request(create);
    }

    @Override
    public void close() throws IOException {
        solrClient.close();
    }
}
