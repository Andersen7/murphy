package top.lishaobo.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.lishaobo.example.dao.AccountMapper;
import top.lishaobo.example.dao.UserMapper;
import top.lishaobo.example.entity.Account;
import top.lishaobo.example.entity.User;
import top.lishaobo.framework.util.CommonUtil;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void register(String accountNumber, String passWord, String name, Integer age, Date birthday) {

        //添加账号信息
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        //生成盐
        String salt = CommonUtil.createSalt();
        account.setSalt(salt);
        account.setPassword(CommonUtil.MD5_32(passWord + salt));
        account.setAccountUuid(uuid);
        accountMapper.updateByPrimaryKeySelective(account);

        //添加用户
        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setBirthday(birthday);
        user.setUuid(uuid);
        userMapper.updateByPrimaryKeySelective(user);

    }
}
