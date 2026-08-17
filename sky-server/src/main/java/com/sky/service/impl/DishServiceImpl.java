package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

@Service
@Slf4j

public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;


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

    /**
     * 菜品批量删除
     * @param ids
     */
    @Transactional
    //涉及多个数据查询的，事务注解
    public void deleteBatch(List<Long> ids) {

        //判断当前菜品是否可以删除？--是否存在起售中的菜品？
        for (Long id : ids) {
            Dish dish=dishMapper.getByID(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                //当前菜品正在起售中，不能删除
                throw  new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);

            }
        }

        //判断当前菜品是否能够删除？---是否有相关套餐关联？

        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishId(ids);
        if(setmealIds != null && setmealIds.size() > 0){
            //证明还存在
            throw  new  DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        //删除菜品表中的菜品数据
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            //删除菜品关联的口味数据
//            dishFlavorMapper.deleteByDishId(id);
//        }
        //根据菜品id集合批量删除菜品
        dishMapper.deleteByIds(ids);
        //根据菜品id集合批量删除关联的口味数据
        dishFlavorMapper.deleteByDishIds(ids);

    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    public DishVO getByIdWhitFlavor(Long id) {
        //根据id查菜品数据
        Dish dish=dishMapper.getByID(id);
        //根据菜品id查口味数据
        List<DishFlavor> dishFlavors=dishFlavorMapper.getByDishId(id);
        //把查询的数据封装成DishVO
        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish,dishVO);//这里只是dish基本数据
        dishVO.setFlavors(dishFlavors);//口味数据
        return dishVO;
    }

    /**
     * 根据id修改菜品与口味信息
     * @param dishDTO
     */
    public void updateWhitFlavor(DishDTO dishDTO) {
        //根据id修改菜品与口味信息
        //修改菜品表的基本信息
        Dish dish = new Dish();//空对象
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
        //删除原有的口味数据  根据DishDto 拿id
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        // 然后在插入口味数据
        List<DishFlavor> flavors=dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor ->
                    dishFlavor.setDishId(dishDTO.getId()));
            dishFlavorMapper.insertBatch(flavors);//批量插入
        }
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWhitFlavor(Dish dish) {
        List<Dish> dishList=dishMapper.list(dish);
        List<DishVO> dishVOList=new ArrayList<>();
        if(dishList != null && dishList.isEmpty()){
            // 如果查询结果为空，直接返回空列表，而不是继续处理
            return new ArrayList<>();
        }
        for (Dish d : dishList) {
            DishVO dishVO=new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors=dishFlavorMapper.getByDishId(d.getId());
            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }
}
