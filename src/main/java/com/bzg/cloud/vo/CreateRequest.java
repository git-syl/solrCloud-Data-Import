package com.bzg.cloud.vo;

/**
 * @author: syl  Date: 2018/5/15 Email:nerosyl@live.com
 */
public class CreateRequest {
    private String collection;
    private String config;
    private int numShards;
    private int numReplicas;

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public int getNumShards() {
        return numShards;
    }

    public void setNumShards(int numShards) {
        this.numShards = numShards;
    }

    public int getNumReplicas() {
        return numReplicas;
    }

    public void setNumReplicas(int numReplicas) {
        this.numReplicas = numReplicas;
    }
}
