package com.sky.service;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;

import java.util.List;

public interface OrderService {
    /**
     * 催单
     * @param id
     */
    void reminder(Long id);

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult pageQueryOrder(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     *
     * @return
     */
    OrderStatisticsVO getOrdersStatisticsStatus();

    /**
     * 完成订单
     * @param ordersDTO
     * @param id
     */
    void completeOrder(OrdersDTO ordersDTO, Long id);

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
