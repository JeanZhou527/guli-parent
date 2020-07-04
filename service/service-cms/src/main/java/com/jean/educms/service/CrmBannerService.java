package com.jean.educms.service;

import com.jean.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-13
 */
public interface CrmBannerService extends IService<CrmBanner> {

    //、查询所有banner
    List<CrmBanner> selectAllBanner();
}
