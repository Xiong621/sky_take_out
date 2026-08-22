package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.JsonSerializer;

import javax.management.openmbean.OpenDataException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class OrderServiceImpl  implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 催单
     * @param id
     */
    public void reminder(Long id) {
        Orders ordersDB=orderMapper.getByIdOrder(id);
        if (ordersDB==null){
            throw  new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Map map=new HashMap();
        map.put("type",2);//1表示来单提醒，2表示客户催单
        map.put("orderId",id);
        map.put("content","订单号："+ordersDB.getNumber());
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult pageQueryOrder(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        Page<Orders> page=orderMapper.pageQueryOrder(ordersPageQueryDTO);
        return new  PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 各个状态的订单数量统计
     * @return
     */
    public OrderStatisticsVO getOrdersStatisticsStatus() {
        Integer toBeConfirmed = orderMapper.getOrdersStatusNumber(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.getOrdersStatusNumber(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.getOrdersStatusNumber(Orders.DELIVERY_IN_PROGRESS);
        OrderStatisticsVO orderStatisticsVO=new OrderStatisticsVO();
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        return orderStatisticsVO;
    }

    /**
     * 完成订单
     * @param id
     */
    public void completeOrder(Long id) {
        Orders ordersDB=orderMapper.getByIdOrder(id);
        if (ordersDB==null || ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)){
            throw  new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders=new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //处理各种业务异常（例如地址簿为空，购物车为空）
        AddressBook addressBook=addressBookMapper.getByIdAddress(ordersSubmitDTO.getAddressBookId());
        if (addressBook==null){
           throw  new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart=new  ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList==null|| shoppingCartList.isEmpty()){
            throw  new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //向订单表插入1条数据
        Orders orders=new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setUserId(userId);
        orderMapper.insert(orders);
        //向订单明细表插入n条数据
        List<OrderDetail> orderDetailList=new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail=new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);
        //清空当前用户购物车数据
        shoppingCartMapper.deleteShoppingCart(userId);
        //封装vo数据
        OrderSubmitVO orderSubmitVOBuild = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();
        return orderSubmitVOBuild;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        //User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("code", "ORDERPAID");
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

      //为替代微信支付成功后的数据库订单状态更新,多定义一个方法进行修改
        Integer OrderPaidStatus=Orders.PAID;//支付状态,已支付
        Integer OrderStatus=Orders.TO_BE_CONFIRMED; //订单状态,待接单

       //发现没有将支付时间 check_out属性赋值,所以在这里更新
        LocalDateTime check_out_time = LocalDateTime.now();

        //获取订单号码
        String orderNumber = ordersPaymentDTO.getOrderNumber();

        log.info("调用updateStatus,用于替换微信支付更新数据库状态的问题");
        orderMapper.updateStatus(OrderStatus, OrderPaidStatus, check_out_time, orderNumber);

//        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
//        vo.setPackageStr(jsonObject.getString("package"));
        Orders orders=orderMapper.getByIdOrder(userId);
        Map map=new HashMap();
        map.put("type",1);//1表示来单提醒，2表示客户催单
        map.put("orderId",orders.getId());
        map.put("content","订单号："+orderNumber);
        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * 再来一单
     * @param id
     */
    @Transactional
    public void repetition(Long id) {
        // 1. 获取当前用户 ID
        Long userId = BaseContext.getCurrentId();

        // 2. 查询历史订单
        Orders oldOrder = orderMapper.getByIdOrder(id);
        if (oldOrder == null || !oldOrder.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在或不属于当前用户");
        }

        List<OrderDetail> oldDetails = orderDetailMapper.getByIdOrderDetail(id);
        //先拿到原来的信息再进行下单
        Orders orders = orderMapper.getByIdOrder(id);
        BeanUtils.copyProperties(oldOrder, orders);
        orders.setId(null);  // 关键：ID 置为 null
        orders.setNumber(UUID.randomUUID().toString().replace("-", ""));  // 新订单号
        orders.setStatus(Orders.PENDING_PAYMENT);  // 待付款状态
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(null);  // 付款时间置空
        orders.setPayStatus(0);  // 未支付
        orders.setCancelReason(null);
        orders.setCancelTime(null);
        orders.setDeliveryStatus(0);
        orders.setDeliveryTime(null);

        // 5. 插入新订单
        orderMapper.insert(orders);
        Long newOrderId = orders.getId();

        // 6. 复制订单明细
        if (oldDetails != null && !oldDetails.isEmpty()) {
            List<OrderDetail> newDetails = new ArrayList<>();
            for (OrderDetail detail : oldDetails) {
                OrderDetail newDetail = new OrderDetail();
                BeanUtils.copyProperties(detail, newDetail);
                newDetail.setId(null);  // ID 置为 null
                newDetail.setOrderId(newOrderId);  // 关联新订单 ID
                newDetails.add(newDetail);
            }
            // 批量插入
            orderDetailMapper.insertBatch(newDetails);
        }
    }


    /**
     * 取消订单
     * @param ordersCancelDTO
     */
    public void deleteCanceOrders(OrdersCancelDTO ordersCancelDTO) {
        Orders byIdOrder = orderMapper.getByIdOrder(ordersCancelDTO.getId());
        byIdOrder.setCancelReason(ordersCancelDTO.getCancelReason());
        orderMapper.update(byIdOrder);
    }

    /**
     * 拒单
     * @param ordersRejectionDTO
     */
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        Orders byIdOrder = orderMapper.getByIdOrder(ordersRejectionDTO.getId());
        byIdOrder.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        byIdOrder.setStatus(Orders.CANCELLED);
        orderMapper.update(byIdOrder);
    }

    /**
     * 接单
     * @param ordersConfirmDTO
     */
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders byIdOrder = orderMapper.getByIdOrder(ordersConfirmDTO.getId());
        byIdOrder.setStatus(Orders.CONFIRMED);
        orderMapper.update(byIdOrder);
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    public OrderVO details(Long id) {

        // 根据id查询订单
        Orders orders = orderMapper.getByIdOrder(id);
        // 查询该订单对应的菜品/套餐明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getByIdOrderDetail(orders.getId());

        // 将该订单及其详情封装到OrderVO并返回
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * 派送订单
     * @param id
     */
    public void delivery(Long id) {
        Orders byIdOrder = orderMapper.getByIdOrder(id);
        byIdOrder.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orderMapper.update(byIdOrder);
    }

    /**
     * 用户端历史订单
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        
        Long userId = BaseContext.getCurrentId();
        ordersPageQueryDTO.setUserId(userId);
        Page<Orders> page=orderMapper.historyOrders(ordersPageQueryDTO);
        List<OrderVO> list = new ArrayList();
        // 查询出订单明细，并封装入OrderVO进行响应
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();// 订单id
                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByIdOrderDetail(orderId);
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);
                list.add(orderVO);
            }
        }

        return new PageResult(page.getTotal(), list);
    }

    /**
     * 取消订单
     * @param id
     */
    public void cancel(Long id) {
        Orders byIdOrder = orderMapper.getByIdOrder(id);
        byIdOrder.setStatus(Orders.CANCELLED);
        orderMapper.update(byIdOrder);
    }

}
