package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C端-套餐浏览接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    @ApiOperation("根据分类id查询套餐")
    @GetMapping("/list")
    @Cacheable(cacheNames = "setmealCach" ,key = "#categoryId")//会动态生成key setmealCach::categoryId
    public Result<List<Setmeal>> list(Long categoryId){
        log.info("根据分类id查询套餐:{}",categoryId);
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);//保证营业
        List<Setmeal> setmeals=setmealService.list(setmeal);
        return Result.success(setmeals);
    }

    //根据套餐id查询包含的菜品

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    @ApiOperation("根据套餐id查询包含的菜品")
    @GetMapping("/dish/{id}")
    public  Result<List<DishItemVO>> dishList(@PathVariable Long id){
        log.info("根据套餐id查询包含的菜品:{}",id);
        List<DishItemVO> dishItemVOS=setmealService.getDishItemById(id);
        return Result.success(dishItemVOS);
    }

}
