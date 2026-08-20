package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "地址簿相关接口")
@Slf4j
public class addressBookController {
    @Autowired
    private AddressBookService addressBookSerivce;

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result saveAddressBook(@RequestBody AddressBook addressBook){
        log.info("新增地址:{}",addressBook);
        addressBookSerivce.saveAddressBook(addressBook);
        return Result.success();
    }

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list(){
        log.info("查询当前登录用户的所有地址信息:");
        List<AddressBook> list=addressBookSerivce.list();
        return Result.success(list);
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getAddressDefault(){
        log.info("查询默认地址");
        //查询isDefault,显示通过用户拿到数据表，检查isDefault是否为1或0，
        //如果为1则返回的是默认地址，如果为0返回的是普通地址
        AddressBook addressBook=addressBookSerivce.getAddressDefault();
        return Result.success(addressBook);

    }

    /**
     * 根据id修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result updateAddress(@RequestBody AddressBook addressBook){
        log.info("修改地址:{}",addressBook);
        addressBookSerivce.updateAddress(addressBook);
        return Result.success();
    }

    /**
     * 根据id删除地址
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public  Result deleteAddress(@RequestParam Long id){
        log.info("根据id删除地址:{}",id);
        addressBookSerivce.deleteAddressById(id);
        return Result.success();
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public  Result<AddressBook> queryAddress(@PathVariable Long id){
        log.info("根据id查询地址,id为：{}",id);
        AddressBook  addressBook=addressBookSerivce.queryAddress(id);
        return Result.success(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public  Result setDefaultAddress(@RequestBody AddressBook addressBook){
        log.info("设置默认地址:{}",addressBook);
        addressBookSerivce.setDefaultAddress(addressBook);
        return Result.success();
    }
}
