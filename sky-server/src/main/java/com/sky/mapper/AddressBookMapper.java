package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    /**
     * 新增地址
     * @param addressBook
     */
    void save(AddressBook addressBook);

    /**
     * 查询当前登录用户的所有地址信息
     * @param addressBook
     * @return
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 查询得到默认地址
     * @param userId
     * @return
     */
    @Select("select is_default from address_book where user_id=#{userId}")
    List<Integer>  getAddressDefault(Long userId);

    /**
     * 根据id修改地址
     * @param addressBook
     */

    @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);

    /**
     * 通过id得到地址
     * @param id
     */
    @Select("select * from address_book where  id=#{id}")
    AddressBook getByIdAddress(Long id);

    /**
     * 根据id删除地址
     * @param id
     */
    @Delete("delete from address_book where id=#{id}")
    void deleteAddressById(Long id);

    /**
     *
     * @param id
     * @return
     */
    @Select("select * from address_book where id=#{id}")
    AddressBook queryAddress(Long id);

    /**
     * 根据id修改
     * @param addressBook
     */
    void update(AddressBook addressBook);
}
