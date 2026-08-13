package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 插入员工数据
     * @param employee
     */

    //@Select("insert into employee ")  错误方法
    //还有错误sql输入错误，id_number写成id_numder,漏泄status
    //注意这个#{}是需要和实体类的属性名一样，不能用_自动驼峰命名
    //还有#{}对应的是实体类，也就是前端传到后端数据，后端对应sql语句的占位符，所以占位符的名字需要和后端实体类对象属性里的名字一致

    @Insert("INSERT INTO employee (name,username,password,phone,sex,id_number,status,create_time,update_time,create_user,update_user)" +
            "values " +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Employee employee);
}
