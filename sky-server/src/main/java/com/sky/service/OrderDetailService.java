package com.sky.service;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;

import java.util.List;

public interface OrderDetailService {
    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    List<OrderDetail> getByIdOrderDetail(Long id);
}
