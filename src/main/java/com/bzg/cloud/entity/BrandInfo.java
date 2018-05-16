package com.bzg.cloud.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author: syl  Date: 2018/4/26 Email:nerosyl@live.com
 */
@Entity
@Table(name = "tm_information")
public class BrandInfo {

    @Id
    private String id;

    @Column(name = "registr_num")
    private String registerNum;

    @Column(name = "tm_name")
    private String tmName;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "proposer")
    private String proposer;

    @Column(name = "proposer_addr")
    private String proposerAddr;

    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "apply_date")
    private String applyDate;

    @Column(name = "first_annc_date")
    private String firstAnncDate;

    @Column(name = "reg_annc_date")
    private String regAnncDate;

    @Column(name = "property_end_date")
    private String propertyEndDate;

    @Column(name = "status")
    private String status;

    @Column(name = "end_status")
    private String endStatus;
    /**
     * SELECT info.,info.,info.tm_name,info.category_id,info.proposer,info.proposer_addr,
     * info.agent_name,info.apply_date,info.first_annc_date,
     * info.reg_annc_date,info.property_end_date,info.status,info.end_status
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(String registerNum) {
        this.registerNum = registerNum;
    }

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getProposerAddr() {
        return proposerAddr;
    }

    public void setProposerAddr(String proposerAddr) {
        this.proposerAddr = proposerAddr;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getFirstAnncDate() {
        return firstAnncDate;
    }

    public void setFirstAnncDate(String firstAnncDate) {
        this.firstAnncDate = firstAnncDate;
    }

    public String getRegAnncDate() {
        return regAnncDate;
    }

    public void setRegAnncDate(String regAnncDate) {
        this.regAnncDate = regAnncDate;
    }

    public String getPropertyEndDate() {
        return propertyEndDate;
    }

    public void setPropertyEndDate(String propertyEndDate) {
        this.propertyEndDate = propertyEndDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndStatus() {
        return endStatus;
    }

    public void setEndStatus(String endStatus) {
        this.endStatus = endStatus;
    }

    //排除了一些属性和solr为空的值
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrandInfo info = (BrandInfo) o;
        return Objects.equals(registerNum, info.registerNum) &&
                Objects.equals(tmName, info.tmName) &&
                Objects.equals(categoryId, info.categoryId) &&
                Objects.equals(proposer, info.proposer) &&
                Objects.equals(proposerAddr, info.proposerAddr) &&
                Objects.equals(agentName, info.agentName) &&
                Objects.equals(applyDate, info.applyDate) &&
                Objects.equals(firstAnncDate, info.firstAnncDate) &&
                Objects.equals(regAnncDate, info.regAnncDate) &&
                Objects.equals(propertyEndDate, info.propertyEndDate) &&
                Objects.equals(status, info.status) &&
                Objects.equals(endStatus, info.endStatus);
    }

    @Override
    public int hashCode() {

        return Objects.hash(registerNum, tmName, categoryId, proposer, proposerAddr, agentName, applyDate, firstAnncDate, regAnncDate, propertyEndDate, status, endStatus);
    }
}
