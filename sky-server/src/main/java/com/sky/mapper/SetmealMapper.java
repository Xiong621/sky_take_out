package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.Autofill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {
    /**
     *
     * @param id
     * @return
     */
    @Select("select  count(id) from setmeal where category_id=#{categoryId} ")
    Integer countByCategoryId(Long id);

    /**
     * 修改套餐
     * @param setmeal
     */
    @Autofill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     * @param setmeal
     */
    @Autofill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    //根据id查询套餐
    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Select("select * FROM  setmeal  where  id= #{id}")
    Setmeal getById(Long id);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteById(List<Long> ids);

}
