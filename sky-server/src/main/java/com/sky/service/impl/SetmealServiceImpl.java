package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.Autofill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
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

    /**
     * 修改套餐
     * @param setmealDTO
     */
    public void updateSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmeal.setUpdateUser(BaseContext.getCurrentId());
        setmealMapper.update(setmeal);
        // 4. 处理套餐关联的菜品（先删后增）
        // 4.1 删除旧的关联关系
//        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
//        // 4.2 插入新的关联关系
//        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
//        if (setmealDishes != null && !setmealDishes.isEmpty()) {
//            for (SetmealDish dish : setmealDishes) {
//                dish.setSetmealId(setmealDTO.getId());
//                dish.setDishId(dish.getDishId());
//            }
//            setmealDishMapper.insertBatch(setmealDishes);
//        }
//
//        log.info("修改套餐成功：{}", setmealDTO.getId());
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult psgeQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        /*
       首先想到的是在是实现类里，需要查询说明
        1  查询什么表  2  这个是什么实现类  3  这个实现类需要干什么  4  干完了下一步是什么
         */
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page=setmealMapper.pageQuery(setmealPageQueryDTO);
        //注意返回值
        return new PageResult(page.getTotal(),page.getResult()) ;
    }

    /**
     * 新增套餐
     * @param setmealDTO
     */
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(!setmealDishes.isEmpty()){
            setmealMapper.insert(setmeal);
            Long id = setmeal.getId();
            log.info("插入后生成的ID：{}", setmeal.getId());
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(id);
                setmealDishMapper.insertSetmealDishes(setmealDish);
            }
        }
    }

    /**
     * 套餐起售、停售
     * @param status
     */
    public void statusSetmeal(Integer status,Long setmealId) {
        Setmeal setmeal=new Setmeal().builder()
                .id(setmealId)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    public SetmealDTO getByIdQuery(Long id) {
        SetmealDTO setmealDTO=new SetmealDTO();
        Setmeal setmeal=setmealMapper.getById(id);
        if(setmeal==null){
            return null;
        }
        //注意需要检查是否已经把数据全部赋值返回了。我这里就漏了
        List<SetmealDish> setealDishItemById = setmealDishMapper.getSetealDishItemById(setmeal.getId());
        setmealDTO.setSetmealDishes(setealDishItemById);
        BeanUtils.copyProperties(setmeal,setmealDTO);
        return setmealDTO;
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    public void deleteSetmeal(List<Long> ids) {
        for (Long id : ids) {
            Setmeal setmealId = setmealMapper.getById(id);
            if (setmealId.getStatus() == StatusConstant.ENABLE) {
                throw  new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        setmealMapper.deleteById(ids);
    }
}
