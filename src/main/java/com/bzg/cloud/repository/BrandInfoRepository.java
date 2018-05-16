package com.bzg.cloud.repository;

import com.bzg.cloud.entity.BrandInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.io.Serializable;
import java.util.List;

/**
 * @author: syl  Date: 2018/4/26 Email:nerosyl@live.com
 */
public interface BrandInfoRepository extends JpaRepository<BrandInfo, Serializable> {

    BrandInfo findBrandInfoByRegisterNumAndCategoryId(String registerId,String categoryId);
}
