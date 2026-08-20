package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
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

    @ApiOperation("取消订单")
    @PutMapping("/cancel")
    public Result  cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO,@RequestParam Long orderId){
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
    public  Result  completeOrder(@RequestBody OrdersDTO ordersDTO,@PathVariable Long id){
        log.info("完成订单");
        orderService.completeOrder(ordersDTO,id);
        return Result.success();
    }

}
