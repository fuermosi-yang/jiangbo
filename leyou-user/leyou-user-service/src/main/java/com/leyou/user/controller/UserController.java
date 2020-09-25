package com.leyou.user.controller;

import com.leyou.common.utils.CodecUtils;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@Controller
public class UserController {

    @Autowired
    private UserService userService;


    //验证手机或用户名是否存在
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkPhoneOrUsername(@PathVariable("data") String data,
                                                        @PathVariable("type")Integer type){

        Boolean flag =false;

     //进行判断
      switch(type){
          case 1:  //用户名
            User userName =  userService.queryByUsername(data);
              if(userName==null){
                  flag =true;
              }
              break;
          case 2: //手机号
              User Phone =  userService.queryByPhone(data);
              if(Phone==null){
               flag =true;
              }
              break;
      }
        return ResponseEntity.ok(flag);
    }


    /**
     * 根据用户输入的手机号，生成随机验证码，长度为6位，纯数字。
     * 并且调用短信服务，发送验证码到用户手机
     */

     @PostMapping("code")
    public ResponseEntity<String> sendVerifyCode(String phone){
         userService.sendVerifyCode(phone);
         return ResponseEntity.status(HttpStatus.CREATED).build();
     }

     @Autowired
  private   RedisTemplate redisTemplate;

     @PostMapping("register")
    public  ResponseEntity<Void> register(User user,String code){
         userService.addUser(user,code);
         return new ResponseEntity<>(HttpStatus.CREATED);
     }

     //登录
    @PostMapping("query")
    public @ResponseBody  ResponseEntity<User>   queryUser( String username,String password){
      User user =   userService.queryByUsernameAndPassword(username,password);
      //if(user==null){
      //    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      //}
         return ResponseEntity.ok(user);
    }



}