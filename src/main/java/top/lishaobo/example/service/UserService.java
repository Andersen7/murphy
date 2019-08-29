package top.lishaobo.example.service;

import java.util.Date;

public interface UserService {

    void register(String accountNumber, String passWord, String name, Integer age, Date birthday);


}
