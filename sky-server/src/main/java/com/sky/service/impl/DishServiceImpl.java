package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Transactional
    public void save(DishDTO dishDTO) {

        Dish dish = new Dish();
        //拷贝相关属性，名字要对应
        BeanUtils.copyProperties(dishDTO, dish);
        //向菜品表插入1数据
        dishMapper.insert(dish);
        Long id = dish.getId();
        //向口味表插入n数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //判断数据是否提交
        //然后在添加n条数据口味
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor ->
                    dishFlavor.setDishId(id));
            dishFlavorMapper.insertBatch(flavors);//批量插入
        }
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */

    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
     /*
       首先想到的是在是实现类里，需要查询说明
        1  查询什么表  2  这个是什么实现类  3  这个实现类需要干什么  4  干完了下一步是什么
         */
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());

    }
}
