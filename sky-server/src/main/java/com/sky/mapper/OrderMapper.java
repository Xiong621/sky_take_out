package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 催单
     */
    @Select("")
    void reminder();

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQueryOrder(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     *各个状态的订单数量统计
     * @return
     */
    @Select("select status from orders")
    List<Integer> getOrdersStatisticsStatus();

    /**
     * 完成订单
     * @param id
     * @return
     */
    @Select("select * from orders where id =#{id}")
    Orders queryCompleteOrder(Long id);

    /**
     * 用户下单
     * @param orders
     */
    void insert(Orders orders);
}
