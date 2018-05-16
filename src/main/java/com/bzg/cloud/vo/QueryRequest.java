package com.bzg.cloud.vo;

/**
 * @author: syl  Date: 2018/5/15 Email:nerosyl@live.com
 */
public class QueryRequest {
    private String qValue;
    private String fqValue;
    private String dfValue;
    private String start;
    private String rows;
    private String sortOrder;
    private String sortField;
    private String highlightField;
    private String simplePre;
    private String simplePost;
    private String collectionName;

    public String getSortOrder() {
        return sortOrder;
    }

    public String getHighlightField() {
        return highlightField;
    }

    public void setHighlightField(String highlightField) {
        this.highlightField = highlightField;
    }

    public String getSimplePre() {
        return simplePre;
    }

    public void setSimplePre(String simplePre) {
        this.simplePre = simplePre;
    }

    public String getSimplePost() {
        return simplePost;
    }

    public void setSimplePost(String simplePost) {
        this.simplePost = simplePost;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getqValue() {
        return qValue;
    }

    public void setqValue(String qValue) {
        this.qValue = qValue;
    }

    public String getFqValue() {
        return fqValue;
    }

    public void setFqValue(String fqValue) {
        this.fqValue = fqValue;
    }

    public String getDfValue() {
        return dfValue;
    }

    public void setDfValue(String dfValue) {
        this.dfValue = dfValue;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}
