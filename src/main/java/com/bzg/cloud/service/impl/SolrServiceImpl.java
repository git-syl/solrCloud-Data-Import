package com.bzg.cloud.service.impl;

import com.bzg.cloud.entity.BrandInfo;
import com.bzg.cloud.repository.SolrRepository;
import com.bzg.cloud.service.SolrService;
import com.bzg.cloud.utils.BasicAuthSolrClient;
import com.bzg.cloud.utils.ImportUtils;
import com.bzg.cloud.utils.thread.ThreadPoolHelp;
import com.bzg.cloud.vo.CreateRequest;
import com.bzg.cloud.vo.QueryRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author syl  Date: 2018/5/11 Email:nerosyl@live.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SolrServiceImpl implements SolrService {

    private static final Logger log = LoggerFactory.getLogger(SolrServiceImpl.class);

    @Autowired
    BasicAuthSolrClient solrClient;
    @Autowired
    EntityManager entityManager;

    @Autowired
    SolrRepository solrRepository;

    @Override
    public void fullImport(Integer startPosition, Integer maxResult) throws InterruptedException {

        new ImportUtils<BrandInfo>() {
            @Override
            public List<BrandInfo> findData() {
                return getBrandInfoListByCriteria(startPosition,maxResult);
            }

            @Override
            public void importDataNotSegment(List<BrandInfo> datafromDb, int index) {
                List<SolrInputDocument> solrInputDocumentList = new ArrayList<>();
                prepareData(datafromDb, solrInputDocumentList, index);
                solrClient.add(solrInputDocumentList);
            }

            @Override
            public void importDataSegment(List<BrandInfo> dataFromDb, List<SolrInputDocument> solrInputDocumentList, int index) {
                prepareData(dataFromDb, solrInputDocumentList, index);
            }

            @Override
            public void finishSegment(List<SolrInputDocument> solrInputDocumentList) {
                solrClient.add(solrInputDocumentList);
            }
        }.importData(0);


    }

    @Override
    public void deltaImport(Integer startPosition, Integer maxResult) throws InterruptedException {
        int processors = Runtime.getRuntime().availableProcessors();
        //io密集型任务
        int nio = 2 * processors + 1;
        //cpu密集型任务
        int cpu = processors + 1;

        final int size;
        final int per;

        long startTime = System.currentTimeMillis();
        final Semaphore semaphore = new Semaphore(-nio + 1);
        final CountDownLatch latch;
        log.info("==========================START TO IMPORT=========================");
        List<BrandInfo> brandInfoList = getBrandInfoListByCriteria(startPosition, maxResult);
        log.info("==========================FINISH QUERY DB. Use Time:" + (System.currentTimeMillis() - startTime));
        size = brandInfoList.size();
        per = size / nio;

        //线程数量大于数据量
        if (size < nio) {
            latch = new CountDownLatch(size);
            for (int i = 0; i < size; i++) {
                int finalI = i;
                ThreadPoolHelp.Builder
                        .cached()
                        .builder()
                        .execute(() -> {
                                    List<SolrInputDocument> solrInputDocumentList = new ArrayList<>();
                                    deleteOldData(brandInfoList, finalI);
                                    prepareData(brandInfoList, solrInputDocumentList, finalI);
                                    solrClient.add(solrInputDocumentList);

                                    latch.countDown();
                                }
                        );
            }
            latch.await();
            log.info("=========FINISH Total Time " + (System.currentTimeMillis() - startTime) + "ms");
            return;
        }


        for (int count = 0; count < nio; count++) {
            int finalCount = count;
            ThreadPoolHelp.Builder
                    .cached()
                    .builder()
                    .execute(() -> {
                        List<SolrInputDocument> solrInputDocumentList = new ArrayList<>();
                        //每个线程，处理n条数据。
                        for (int i = per * finalCount; i < SolrServiceImpl.this.calculateEndValue(size, nio, finalCount, per); i++) {
                            deleteOldData(brandInfoList, i);
                            prepareData(brandInfoList, solrInputDocumentList, i);
                        }
                        solrClient.add(solrInputDocumentList);

                        semaphore.release();
                    });
        }//end for
        semaphore.acquire();
        log.info("=========FINISH Total Time " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private void deleteOldData(List<BrandInfo> brandInfoData, int i) {
        try {
            log.info("Delete Number [" + i + "] Data...");
            solrClient.deleteByQuery(String.format("category_id:%s AND registr_num:%s", brandInfoData.get(i).getCategoryId(), brandInfoData.get(i).getRegisterNum()));
        } catch (SolrServerException | IOException e) {
            log.info("delete Number[" + i + "]old error", e);
        }
    }

    private void prepareData(List<BrandInfo> brandInfoData, List<SolrInputDocument> solrInputDocumentList, int i) {
        log.info("Prepare Number [" + i + "] Data...");

        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", brandInfoData.get(i).getId());
        document.addField("end_status", brandInfoData.get(i).getEndStatus());
        document.addField("registr_num", brandInfoData.get(i).getRegisterNum());
        document.addField("agent_name", brandInfoData.get(i).getAgentName());
        document.addField("proposer", brandInfoData.get(i).getProposer());
        document.addField("reg_annc_date", brandInfoData.get(i).getRegAnncDate());
        document.addField("proposer_addr", brandInfoData.get(i).getProposerAddr());
        document.addField("tm_name", brandInfoData.get(i).getTmName());
        document.addField("property_end_date", brandInfoData.get(i).getPropertyEndDate());
        document.addField("apply_date", brandInfoData.get(i).getApplyDate());
        document.addField("category_id", brandInfoData.get(i).getCategoryId());
        document.addField("first_annc_date", brandInfoData.get(i).getFirstAnncDate());
        document.addField("status", brandInfoData.get(i).getStatus());
        solrInputDocumentList.add(document);

    }


    @Override
    public void clearAllData() throws IOException, SolrServerException {
        solrClient.deleteByQuery("*:*");
    }

    @Override
    public List<BrandInfo> getBrandInfoListByCriteria(Integer startPosition, Integer maxResult) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BrandInfo> criteriaQuery = criteriaBuilder.createQuery(BrandInfo.class);
        Root<BrandInfo> from = criteriaQuery.from(BrandInfo.class);
        TypedQuery<BrandInfo> query = entityManager.createQuery(criteriaQuery);
        if (maxResult != null && startPosition != null) {
            query.setMaxResults(maxResult);
            query.setFirstResult(startPosition);
        }
        return query.getResultList();
    }

    @Override
    public SolrDocumentList query(QueryRequest request) throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        if (StringUtils.isNotBlank(request.getqValue())) {
            query.set("q", request.getqValue());
        }
        if (StringUtils.isNotBlank(request.getFqValue())) {
            query.addFilterQuery(request.getFqValue());
        }
        if (StringUtils.isNotBlank(request.getDfValue())) {
            query.set("df", request.getDfValue());
        }
        if (StringUtils.isNotBlank(request.getStart()) && StringUtils.isNotBlank(request.getRows())) {
            query.setStart(Integer.parseInt(request.getStart()));
            query.setRows(Integer.parseInt(request.getRows()));
        }

        if (StringUtils.isNotBlank(request.getHighlightField()) && StringUtils.isNotBlank(request.getSimplePost()) && StringUtils.isNotBlank(request.getSimplePre())) {
            query.setHighlight(true);//高亮
            query.addHighlightField(request.getHighlightField());
            query.setHighlightSimplePre(request.getSimplePre());
            query.setHighlightSimplePost(request.getSimplePost());
        }

        if (StringUtils.isNotBlank(request.getSortField()) && StringUtils.isNotBlank(request.getSortOrder())) {
            String[] sortFieldStrings = request.getSortField().split(",");
            String[] sortOrderStrings = request.getSortOrder().split(",");
            for (int i = 0, len = sortFieldStrings.length; i < len; i++) {
                String sortFieldString = sortFieldStrings[i];
                String sortOrderString = sortOrderStrings[i];
                SolrQuery.SortClause sortClause = new SolrQuery.SortClause(sortFieldString, SolrQuery.ORDER.valueOf(sortOrderString));
                query.addOrUpdateSort(sortClause);
            }
        }
        return StringUtils.isBlank(request.getCollectionName()) ? solrClient.query(query).getResults() : solrClient.query(request.getCollectionName(), query).getResults();
    }

    @Override
    public NamedList<Object> createCollection(CreateRequest request) throws IOException, SolrServerException {
        return solrClient.createCollection(request.getCollection(), request.getConfig(), request.getNumShards(), request.getNumReplicas());
    }

    private int calculateEndValue(int size, int nio, int count, int per) {
        int end = size / nio * count + per;
        //最后一次遍历处理余数
        return (size % nio != 0 && nio - count < 2) ? size : end;
    }


}
