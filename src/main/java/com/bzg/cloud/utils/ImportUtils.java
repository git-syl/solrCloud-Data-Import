package com.bzg.cloud.utils;

import com.bzg.cloud.utils.thread.ThreadPoolHelp;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * @author syl  Date: 2018/5/16 Email:nerosyl@live.com
 */
public abstract class ImportUtils<T> {

    private static final Logger log = LoggerFactory.getLogger(ImportUtils.class);

    /**
     * @param type 0:io密集型任务 1:cpu密集型任务
     * @throws InterruptedException
     */
    public void importData(int type) throws InterruptedException {

        int processors = Runtime.getRuntime().availableProcessors();

        int nio = 2 * processors + 1;
        int cpu = processors + 1;
        int threadCount = type == 0 ? nio : cpu;

        final int size;
        final int per;

        long startTime = System.currentTimeMillis();
        final Semaphore semaphore = new Semaphore(-threadCount + 1);
        final CountDownLatch latch;
        log.info("==========================START TO IMPORT=========================");
        List<T> brandInfoList = findData();
        log.info("==========================FINISH QUERY DB. Use Time:" + (System.currentTimeMillis() - startTime));
        size = brandInfoList.size();
        per = size / threadCount;

        if (size < threadCount) {
            latch = new CountDownLatch(size);
            for (int i = 0; i < size; i++) {
                int finalI = i;
                ThreadPoolHelp.Builder
                        .cached()
                        .builder()
                        .execute(() -> {
                                    importDataNotSegment(brandInfoList, finalI);
                                    latch.countDown();
                                }
                        );
            }
            latch.await();
            log.info("=========FINISH Total Time " + (System.currentTimeMillis() - startTime) + "ms");
            return;
        }


        for (int count = 0; count < threadCount; count++) {
            int finalCount = count;
            ThreadPoolHelp.Builder
                    .cached()
                    .builder()
                    .execute(() -> {
                        List<SolrInputDocument> solrInputDocumentList = new ArrayList<>();
                        for (int i = per * finalCount; i < calculateEndValue(size, threadCount, finalCount, per); i++) {
                            importDataSegment(brandInfoList, solrInputDocumentList, i);
                        }
                        finishSegment(solrInputDocumentList);
                        semaphore.release();
                    });
        }//end for
        semaphore.acquire();
        log.info("=========FINISH Total Time " + (System.currentTimeMillis() - startTime) + "ms");

    }

    /**
     * 从数据库查询的数据
     *
     * @return List<T>
     */
    public abstract List<T> findData();

    /**
     * 线程数量大于数据量，每个线程处理一个数据的情况
     *
     * @param datafromDb form findData()
     * @param index      datafromDb index
     */
    public abstract void importDataNotSegment(List<T> datafromDb, int index);

    /**
     * 线程数量小于数据量，每个线程，处理n条数据。
     *
     * @param dataFromDb            form findData()
     * @param solrInputDocumentList 向solr提交的数据集合
     * @param index                 每个线程，处理n条数据 的数据index
     */
    public abstract void importDataSegment(List<T> dataFromDb, List<SolrInputDocument> solrInputDocumentList, int index);

    /**
     * 每个线程执行完成之后。（线程数量小于数据量，每个线程，处理n条数据。）
     *
     * @param solrInputDocumentList 向solr提交的数据集合
     */
    public abstract void finishSegment(List<SolrInputDocument> solrInputDocumentList);

    private int calculateEndValue(int size, int threadCount, int count, int per) {
        int end = size / threadCount * count + per;
        //最后一次遍历处理余数
        return (size % threadCount != 0 && threadCount - count < 2) ? size : end;
    }
}
