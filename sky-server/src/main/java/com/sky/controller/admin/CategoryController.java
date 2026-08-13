package com.sky.controller.admin;


import com.github.pagehelper.Page;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     *修改分类
     * @param categoryDTO
     * @return
     */
    //修改分类 put请求
    @PutMapping
    @ApiOperation("修改分类")
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类:{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    //关于页，包装成pageRResult
    @ApiOperation("分类分页查询")
    public  Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {

        log.info("分类分页查询:{}", categoryPageQueryDTO);
        //分页访问
        PageResult pageResult=categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result  save(@RequestBody CategoryDTO categoryDTO) {
        /*
         */
        log.info("新增分类:{}", categoryDTO);
        categoryService.insert(categoryDTO);
        return  Result.success();
    }
    //4
    /**
     * 启用、禁用分类
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用分类")
    public  Result  status(@PathVariable Integer status,Long id) {
        log.info("启用、禁用分类:{},{}",status,id);
        categoryService.startOrStop(status,id);
        return  Result.success();
    }

    //5根据id删除分类
    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @ApiOperation("5根据id删除分类")
    public  Result  delete(@PathVariable Long id) {
        log.info(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        categoryService.deleteById(id);
        return Result.success();
    }
    //6 根据类型查询分类

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    public  Result<List<Category>> list(Integer type) {
        List<Category> list=categoryService.list(type);
        return Result.success(list);
    }
}
