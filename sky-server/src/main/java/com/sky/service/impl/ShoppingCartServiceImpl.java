package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入到购物车中的商品是否已经存在  判断用户名是否存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //shoppingCart.setUserId(2L);//因为我没有用户端前端，所以不方便拿到token与id,我选择在拦截器里加入接口文档的放行代码
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list=shoppingCartMapper.list(shoppingCart);
        //如果已经存在，只需要将数量加一
        if(!list.isEmpty() && list != null){
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber()+1);//需要更改，使用update语句
            shoppingCartMapper.updateNumberById(cart);
        }else {
            //如果不存在，需要插入一条购物车数据
            //不知道是套餐还是菜品
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();
            if(dishId != null){
                //菜品id不为空，添加到购物车的是菜品
                Dish byIdDish = dishMapper.getByID(dishId);
                shoppingCart.setName(byIdDish.getName());//得到的是名字
                shoppingCart.setImage(byIdDish.getImage());//得到的是图片
                shoppingCart.setAmount(byIdDish.getPrice());//得到的是价格
                //还有两个没赋值number、createTime
                //shoppingCart.setNumber(1);
                //shoppingCart.setCreateTime(LocalDateTime.now());
                //最后插入数据库
            }else if (setmealId != null){
                //套餐id不为空，添加到购物车的是套餐
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
                //shoppingCart.setNumber(1);
                //shoppingCart.setCreateTime(LocalDateTime.now());
            }else {
                throw  new  RuntimeException("添加购物车失败：未指定菜品或套餐");
            }

            //这两个是一样的不受菜品套餐影响
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insertShoppingCart(shoppingCart);
        }
    }

    /**
     * 查看购物车
     * @return
     */
    public List<ShoppingCart> list() {
        return shoppingCartMapper.getChoppingCartById(BaseContext.getCurrentId());
    }

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     * @return
     */
    public void deleteNoeShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        Long dishAndSetmealtId;
        Long userId = BaseContext.getCurrentId();
        //判断是套餐还是菜品，如果是菜品或者套餐，根据用户id与菜品id或者套餐id去删除对应的数据
        if(shoppingCartDTO.getDishId() != null){
            dishAndSetmealtId=shoppingCartDTO.getDishId();
        }else {
            dishAndSetmealtId=shoppingCartDTO.getSetmealId();
        }

        Long number=shoppingCartMapper.getShoppingCartById(dishAndSetmealtId,userId);
        if(number==1) {
            shoppingCartMapper.deleteShoppingCartById(dishAndSetmealtId,userId);
        }else {
            number--;
            shoppingCartMapper.update(dishAndSetmealtId,userId,number);
        }
    }

    /**
     * 清空购物车
     * @param
     */
    public void deleteShoppingCart() {
        shoppingCartMapper.deleteShoppingCart(BaseContext.getCurrentId());
    }
}
