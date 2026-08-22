package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;
    /**
     * 新增地址
     * @param addressBook
     */
    public void saveAddressBook(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);//添加为0，不是默认地址
        addressBookMapper.save(addressBook);
    }

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    public List<AddressBook> list(AddressBook addressBook) {
        List<AddressBook> list=addressBookMapper.list(addressBook);
        return list;
    }


    /**
     * 根据id修改地址
     * @param addressBook
     */
    public void updateAddress(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }
    /**
     * 根据id删除地址
     * @param id
     */
    @Override
    public void deleteAddressById(Long id) {
        addressBookMapper.deleteAddressById(id);
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    public AddressBook queryAddress(Long id) {
        AddressBook address = addressBookMapper.queryAddress(id);
        return address;
    }

    /**
     * 设置默认地址
     * @param addressBook
     */
    public void setDefaultAddress(AddressBook addressBook) {
//        List<AddressBook> list=addressBookMapper.list(BaseContext.getCurrentId());
//        for (AddressBook addressBookIn : list) {
//                //需要把默认的改为不默认，这个是我漏补的
//            addressBookIn.setIsDefault(0);
//            addressBookMapper.updateAddress(addressBookIn);
//        }
//        AddressBook Address = addressBookMapper.getByIdAddress(addressBook.getId());
//        Address.setIsDefault(1);
//        addressBookMapper.updateAddress(Address);

        //1、将当前用户的所有地址修改为非默认地址 update address_book set is_default = ? where user_id = ?
        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.updateIsDefaultByUserId(addressBook);

        //2、将当前地址改为默认地址 update address_book set is_default = ? where id = ?
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }
}
