package com.leyou.service;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.client.UserClient;
import com.leyou.properties.JwtProperties;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties properties;
    //@Autowired
    //private UserService userService;

    public String authentication(String username, String password) throws Exception {


        // 调用微服务，执行查询
        //User user = userService.queryByUsernameAndPassword(username, password);
        User user = userClient.queryUser(username, password);
        System.out.println();

        // 如果查询结果为null，则直接返回null
        if (user == null) {
            return null;
        }

        // 如果有查询结果，则生成token
        String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()),
                properties.getPrivateKey(), properties.getExpire());
        return token;

    }
}