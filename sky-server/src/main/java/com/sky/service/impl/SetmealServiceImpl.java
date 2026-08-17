package com.sky.service.impl;

import com.sky.entity.Setmeal;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 根据分类id查询套餐
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list=setmealDishMapper.list(setmeal);
        return list;
    }

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */

    public List<DishItemVO> getDishItemById(Long id) {
        List<DishItemVO> dishItemVO=setmealDishMapper.getDishItemById(id);
        return dishItemVO;
    }
}
