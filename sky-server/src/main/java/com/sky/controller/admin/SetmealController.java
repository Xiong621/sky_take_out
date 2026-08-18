package com.sky.controller.admin;

import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {


    @Autowired
    private SetmealService setmealService;

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    //修改套餐
    @ApiOperation("修改套餐")
    @PutMapping
    @CacheEvict(cacheNames = "setmealCach",allEntries = true)//如果写ids是个集合，是个地址，算不出来
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐:{}", setmealDTO);
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }


    //分页查询

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public  Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询:{}", setmealPageQueryDTO);
        PageResult pageResult=setmealService.psgeQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    //新增套餐

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @ApiOperation("新增套餐")
    @PostMapping
    @CacheEvict(cacheNames = "setmealCach",key = "#setmealDTO")
    public  Result save(@RequestBody  SetmealDTO setmealDTO) {
        log.info("新增套餐:{}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐起售、停售
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售、停售")
    @CacheEvict(cacheNames = "setmealCach",allEntries = true)//如果写ids是个集合，是个地址，算不出来
    public  Result statusSetmeal(@PathVariable  Integer status,Long id) {
        log.info("套餐起售情况：{}",status==1?"起售":"停售");
        setmealService.statusSetmeal(status,id);
        return Result.success();
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public  Result<SetmealDTO> getById(@PathVariable Long id) {
        log.info("根据id查询套餐:{}",id);
        SetmealDTO setmealDTO=setmealService.getByIdQuery(id);
        return Result.success(setmealDTO);
    }

    //批量删除套餐
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    @CacheEvict(cacheNames = "setmealCach",allEntries = true)//如果写ids是个集合，是个地址，算不出来
    public  Result deleteSetmeal(@RequestParam List<Long> ids) {//注意拿到的ids是集合类型的，有多个id
        log.info("批量删除套餐：{}",ids);
        setmealService.deleteSetmeal(ids);
        return Result.success();
    }

}
