package com.sky.service.impl;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private  OrderDetailMapper orderDetailMapper;
    private OrderMapper orderMapper;
    /**
     * 查询订单详情
     * @param id
     * @return
     */
    public List<OrderDetail> getByIdOrderDetail(Long id) {
        return orderDetailMapper.getByIdOrderDetail(id);
    }
}
