package com.bzg.cloud.repository;

import com.bzg.cloud.entity.BrandInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * @author: syl  Date: 2018/5/11 Email:nerosyl@live.com
 */
public interface SolrRepository extends JpaRepository<BrandInfo, Serializable> {

}
