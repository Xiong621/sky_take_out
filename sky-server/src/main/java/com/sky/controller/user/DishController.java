package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "C端-菜品浏览接口")
@Slf4j
public class DishController {
    @Autowired
    private  DishService dishService;
    @Autowired
    private  RedisTemplate redisTemplate;
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    //@Cacheable(cacheNames = "dish",key = "#categoryId")
    public Result<List<DishVO>> list(Long categoryId) {
        //根据redis中是否存在菜品
        String key ="dish::"+categoryId;
        //查询redis中是否存在数据
        List<DishVO>  list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if(list != null && list.size()>0){
            //如果存在，直接返回，无需查询数据库
            return Result.success(list);
        }
        log.info("根据分类id查询菜品:{}", categoryId);
        Dish vo = new Dish();
        vo.setCategoryId(categoryId);
        vo.setStatus(StatusConstant.ENABLE);//拿到数据需要是营业
        //如果不存在，查询数据库，将查询的数据放入redis中
//        List<DishVO>
        list=dishService.listWhitFlavor(vo);
        redisTemplate.opsForValue().set(key,list);
        return Result.success(list);
    }
}
