package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.result.Result;
import com.sky.service.OrderDetailService;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "订单相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private  OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 催单
     * @param id
     * @return
     */
    @GetMapping("reminder/{id}")
    @ApiOperation("催单")
    public Result reminder(@PathVariable Long id){
        log.info("催单id为:{}",id);
        orderService.reminder(id);
        return Result.success();
    }

    /**
     *用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public  Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单,参数:{}",ordersSubmitDTO);
        OrderSubmitVO ordersSubmit=orderService.submitOrder(ordersSubmitDTO);
        return Result.success(ordersSubmit);
    }
    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    private  Result<Orders>  orderDetail(@PathVariable Long id){
        log.info("查询订单详情,id为:{}",id);
        Orders orders=orderService.getByIdOrder(id);
        return Result.success(orders);
    }


    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    private  Result  repetition(@PathVariable Long id){
        log.info("再来一单,id:{}",id);
        orderService.repetition(id);
        return Result.success();
    }


}
