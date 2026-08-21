package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
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

//    /**
//     * 完成订单
//     * @param id
//     * @return
//     */
//    @Select("select * from orders where id =#{id}")
//    Orders queryCompleteOrder(Long id);

    /**
     * 用户下单
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 跳过微信支付，直接设置状态
     * @param orderStatus
     * @param orderPaidStatus
     * @param checkOutTime
     * @param orderNumber
     */
    @Update("update  orders set status=#{orderStatus},pay_status=#{orderPaidStatus},checkout_time=#{checkOutTime} where number=#{orderNumber}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime checkOutTime, String orderNumber);

    /**
     * 再下一单，获得订单数据
     * @param orderId
     */
    @Select("select * from orders where id = #{orderId}")
    Orders getByIdOrder(Long orderId);

}
