package com.bzg.cloud.vo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author syl
 */
@Component
public class BaseRequest implements Serializable {

    /**
     * // '用户唯一标识',
     */
    private String token;

    /**
     * 当前页
     */
    private Integer currentPage;
    /**
     * 每页显示条数
     */

    private Integer pageSize;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public BaseRequest() {
    }
}
