package com.bzg.cloud.service;

import com.bzg.cloud.entity.BrandInfo;
import com.bzg.cloud.vo.CreateRequest;
import com.bzg.cloud.vo.QueryRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.List;

/**
 * @author: syl  Date: 2018/5/11 Email:nerosyl@live.com
 */
public interface SolrService {

    void fullImport(Integer startPosition, Integer maxResult) throws InterruptedException;
  //  void deltaImport() throws IOException, SolrServerException;
    void deltaImport(Integer startPosition, Integer maxResult) throws IOException, SolrServerException, InterruptedException;

    void clearAllData() throws IOException, SolrServerException;

    List<BrandInfo> getBrandInfoListByCriteria(Integer maxResult, Integer startPosition);

    SolrDocumentList query(QueryRequest request) throws IOException, SolrServerException;

    NamedList<Object> createCollection(CreateRequest request) throws IOException, SolrServerException;
}
