package com.sky.controller.admin;

import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.OrderStatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "订单管理相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 取消订单
     * @param ordersCancelDTO
     * @return
     */
    @ApiOperation("取消订单")
    @PutMapping("/cancel")
    public Result  cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO){
        log.info("取消订单:{}",ordersCancelDTO);
        orderService.deleteCanceOrders(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    @ApiOperation("订单搜索")
    @GetMapping("/conditionSearch")
    public  Result<PageResult> pageQueryOrder(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单搜索:{}",ordersPageQueryDTO);
        PageResult pageResult=orderService.pageQueryOrder(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 各个状态的订单数量统计
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public  Result<OrderStatisticsVO> getOrdersStatistics(){
        log.info("各个状态的订单数量统计");
        OrderStatisticsVO list=orderService.getOrdersStatisticsStatus();
        return Result.success(list);
    }

    /**
     * 完成订单
     * @param ordersDTO
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public  Result  completeOrder(@PathVariable Long id){
        log.info("完成订单");
        orderService.completeOrder(id);
        return Result.success();
    }

    /**
     * 拒单
     * @param ordersRejectionDTO
     * @return
     */
    @ApiOperation("拒单")
    @PutMapping("/rejection")
    public  Result  rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        log.info("拒单原因为:{}",ordersRejectionDTO.getRejectionReason());
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 接单
     * @param ordersConfirmDTO
     * @return
     */
    @ApiOperation("接单")
    @PutMapping("/confirm")
    public  Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接单,id为;{}",ordersConfirmDTO.getId());
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @ApiOperation("查询订单详情")
    @GetMapping("/details/{id}")
    public  Result<Orders>  details(@PathVariable Long id){
        log.info("查询订单详情,id:{}",id);
        Orders orders=orderService.details(id);
        return Result.success(orders);
    }

    /**
     * 派送订单
     * @param id
     * @return
     */
    @ApiOperation("派送订单")
    @PutMapping("/delivery/{id}")
    public  Result  delivery(@PathVariable Long id){
        log.info("派送订单 id:{}",id);
        orderService.delivery(id);
        return  Result.success();
    }

}
