package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    /**
     * 根据菜品id查询对应地方套餐id
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishId(List<Long> dishIds);

    /**
     * 根据分类id查询套餐
     * @param setmeal
     * @return
     */
    //@Select("select * FROM  setmeal  where  category_id = #{categoryId}")
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    @Select("select * FROM  dish  where  id= #{id}")
    List<DishItemVO> getDishItemById(Long id);
}
