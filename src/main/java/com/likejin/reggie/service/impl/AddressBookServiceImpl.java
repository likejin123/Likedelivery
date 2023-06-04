package com.likejin.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.likejin.reggie.entity.AddressBook;
import com.likejin.reggie.mapper.AddressBookMapper;
import com.likejin.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @Author 李柯锦
 * @Date 2023/6/3 20:26
 * @Description
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper,AddressBook> implements AddressBookService {
}
