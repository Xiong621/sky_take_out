package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     *新增地址
     * @param addressBook
     */
    void saveAddressBook(AddressBook addressBook);

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    List<AddressBook> list();

    /**
     * 查询默认地址
     */
    AddressBook getAddressDefault();

    /**
     * 根据id修改地址
     * @param addressBook
     */
    void updateAddress(AddressBook addressBook);

    /**
     * 根据id删除地址
     * @param id
     */
    void deleteAddressById(Long id);

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    AddressBook queryAddress(Long id);

    /**
     * 设置默认地址
     * @param addressBook
     */
    void setDefaultAddress(AddressBook addressBook);
}
