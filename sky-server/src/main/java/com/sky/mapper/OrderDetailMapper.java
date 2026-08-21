package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     *批量加入明细表
     * @param orderDetailList
     */
    void insertBatch(List<OrderDetail> orderDetailList);

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @Select("select * from order_detail where  order_id=#{id}")
    List<OrderDetail> getByIdOrderDetail(Long id);
}
