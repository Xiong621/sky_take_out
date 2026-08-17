package com.sky.service;


import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品
     * @param dishDTO
     */
    void save(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    DishVO getByIdWhitFlavor(Long id);

    /**
     * 根据id修改菜品与口味信息
     * @param dishDTO
     */
    void updateWhitFlavor(DishDTO dishDTO);

    /**
     * 条件查询菜品和口味
     * @param vo
     * @return
     */
    List<DishVO> listWhitFlavor(Dish vo);
}
