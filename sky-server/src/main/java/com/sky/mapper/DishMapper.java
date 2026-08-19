package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.annotation.Autofill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//别忘记业务注解
@Mapper
public interface DishMapper {
    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select  count(id) from  dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品数据
     * @param dishDTO
     */
    @Autofill(value = OperationType.INSERT)
    void insert(Dish dishDTO);



    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据主键查询 菜品批量删除
     * @param id
     * @return
     */
    @Select("select * from dish where  id = #{id} ")
    Dish getByID(Long id);

    /**
     *根据主键删除菜品数据
     * @param id
     */
    @Delete("delete from  dish where id=#{id}")
    void deleteById(Long id);

    /**
     * 根据菜品集合批量删除菜品
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据id动态修改菜品
     * @param dish
     */
    @Autofill(value = OperationType.UPDATE)
    //自动填充
    void update(Dish dish);

    /**
     * 根据分类id查询菜品
     * @param dish
     * @return
     */
    //@Select("select * from dish where  category_id = #{categoryId}")
    //传过来一个对象，需要动态查询，不要写死
    List<Dish> list(Dish dish);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Select("select * from dish where  category_id = #{categoryId}")
    List<DishVO> getCategory(Long categoryId);
}
