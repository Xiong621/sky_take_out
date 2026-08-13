package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    /**
     * 修改分类
     * @param categoryDTO
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        //设置修改时间，修改人
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.update(category);
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        /*
        首先想到的是在是实现类里，需要查询说明
        1  查询什么表  2  这个是什么实现类  3  这个实现类需要干什么  4  干完了下一步是什么
         */
        //select*from category limtt 0  ,   10
        //注意在前端查看的getpagesize数
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        //注意还有泛型
        Page<Category> page =categoryMapper.pageQuery(categoryPageQueryDTO);

        return new  PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 新增分类
     * @param categoryDTO
     */
    public void insert(CategoryDTO categoryDTO) {
        /*
        添加东西的时候
        1  创建完整的菜属性  2 在把新建的属性赋值到新建菜属性里，然后把没有菜的其他属性给赋值
        3  回到业务类
         */
        //创建菜
        Category category = new Category();
        //赋值
        BeanUtils.copyProperties(categoryDTO, category);
        //分类状态 0标识禁用 1表示启用   默认禁用（如果直接启用无意义）
        category.setStatus(StatusConstant.DISABLE);
        //创建时间与创建人
        category.setCreateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        //修改时间
        category.setUpdateTime(LocalDateTime.now());
        //前面的拦截器已经拿到id信息并存了ThreadLocal
        category.setUpdateUser(BaseContext.getCurrentId());
        //是把完整的菜系放到数据库不是categoryDTO,
        categoryMapper.insert(category);
    }

    /**
     * 启用、禁用分类
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        // update employee set status = ? where id = ?
        //直接添加status与id？
        //两种方法，传统方法与Bulid方法
        Category category=Category.builder()
                .id(id)
                .status(status)
                .build();
        categoryMapper.update(category);

    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        //查询当前分类是否关联了菜品，如果关联了就抛出业务异常
        Integer count=dishMapper.countByCategoryId(id);
        if(count>0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        //查询当前分类是否关联了套餐，如果关联了就抛出业务异常
        count = setmealMapper.countByCategoryId(id);
        if(count>0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        //删除分类数据
        categoryMapper.deleteById(id);
    }

    @Override
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }
}
