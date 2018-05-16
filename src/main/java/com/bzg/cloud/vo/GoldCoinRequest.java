package com.bzg.cloud.vo;

import javax.persistence.Column;

/**
 * @author: syl  Date: 2018/4/11 Email:nerosyl@live.com
 */
public class GoldCoinRequest extends BaseRequest {

    /**
     * 变动金额，充值或者消费的金额
     */
   private Integer count;


    private String token;

    /**
     * 类型1消费 0充值
     */
    private Integer type;

    private Long amount;

    private String ip;

    private String ipParse;

    /**
     * 消费项目的名称
     */
   private String name ;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpParse() {
        return ipParse;
    }

    public void setIpParse(String ipParse) {
        this.ipParse = ipParse;
    }


}
