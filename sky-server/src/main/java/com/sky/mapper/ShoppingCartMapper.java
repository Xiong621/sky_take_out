package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 动态条件查询
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     *
     * @param shoppingCart
     */
    @Update("update  shopping_cart set number=#{number} where id = #{id}")
     void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插入购物车数据
     * @param shoppingCart
     */
    void insertShoppingCart(ShoppingCart shoppingCart);

    /**
     * 根据id查看购物车
     * @param userId
     * @return
     */
    @Select("select * from  shopping_cart where user_id=#{userId}")
    List<ShoppingCart> getChoppingCartById(Long userId);

    /**
     * 删除的是购物车里的菜品
     * @param dishId
     * @param userId
     */
   // @Delete("delete  from  shopping_cart where user_id=#{userId} and dish_id=#{dishId}")
    //void deleteDishById(Long dishId, Long userId);

    /**
     * 删除的是购物车里的菜品
     * @param setmealId
     * @param userId
     */
   //@Delete("delete  from shopping_cart where user_id=#{userId} and setmeal_id=#{setmealId}")
    //void deleteSetmealById(Long setmealId, Long userId);

    /**
     * 根据dishid查找number还有多少
     * @param dishId
     * @param userId
     * @return
     */
    //@Select("select number  from shopping_cart  where user_id=#{userId} and dish_id=#{dishId}")
    //Long getDishtById(Long dishId, Long userId);

    /**
     * 根据setmealtid查找number还有多少
     * @param setmealtId
     * @param userId
     * @return
     */
    //@Select("select number  from shopping_cart  where user_id=#{userId} and setmeal_id=#{setmealtId}")
    //Long getSetmealtById(Long setmealtId, Long userId);

    /**
     * 动态代理
     * @param dishAndSetmealtId
     * @param userId
     * @return
     */
    @Select("select number from  shopping_cart where (setmeal_id=#{dishAndSetmealtId} or dish_id=#{dishAndSetmealtId} and user_id=#{userId})" )
    Long getShoppingCartById(Long dishAndSetmealtId, Long userId);

    /**
     *
     * @param dishAndSetmealtId
     * @param userId
     * @return
     */
    @Delete("delete from shopping_cart where (setmeal_id=#{dishAndSetmealtId} or dish_id=#{dishAndSetmealtId} and user_id=#{userId})")
    void deleteShoppingCartById(Long dishAndSetmealtId, Long userId);

    /**
     * 修改购物车数据
     * @param dishAndSetmealtId
     * @param userId
     */
    @Update("update shopping_cart set  number=#{number} where (setmeal_id=#{dishAndSetmealtId} or dish_id=#{dishAndSetmealtId} and user_id=#{userId})")
    void update(Long dishAndSetmealtId, Long userId,Long number);

    /**
     * 清空购物车
     * @param userId
     */
    @Delete("delete from  shopping_cart where user_id=#{userId}")
    void deleteShoppingCart(Long userId);
}
