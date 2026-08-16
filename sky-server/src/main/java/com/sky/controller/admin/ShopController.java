package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
//防止与user的类名一样冲突，分别取别名
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    private static final  String KEY="SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 设置营业状态
     * @param status
     * @return
     */
    //设置营业状态
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result  setStatus(@PathVariable Integer status){
        log.info("设置营业状态：{}",status == 1?"营业中":"打烊中");
        //用redis
        redisTemplate.opsForValue().set(KEY, status);

        return Result.success();
    }

    /**
     * 获取店铺营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public  Result<Integer> getStatus(){
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺营业状态:{}",shopStatus == 1?"营业中":"打烊中");
        return Result.success(shopStatus);
    }
}
