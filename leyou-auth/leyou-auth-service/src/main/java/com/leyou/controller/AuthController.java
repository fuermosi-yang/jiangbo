package com.leyou.controller;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.properties.JwtProperties;
import com.leyou.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties prop;

    /**
     * 登录授权
     *
     * @param username
     * @param password
     * @return
     */

    String cookie = null;


    @PostMapping("accredit")
    public ResponseEntity<Void> authentication(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) {
        // 登录校验
        String token = null;
        try {
            token = this.authService.authentication(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isBlank(token)) {
          return  ResponseEntity.status(403).build();
        }
        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
        CookieUtils.setCookie(request, response, prop.getCookieName(),
                token, prop.getCookieMaxAge(), null, true);
        cookie = token;
        return ResponseEntity.ok().build();
    }

    //@GetMapping("getTooken")
    //public String TookenWill(){
    //    return cookie;
    //}

    /**
     * 后台实现用户登录cookie验证
     */

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("LY_TOKEN") String token, HttpServletRequest request ,  HttpServletResponse response ) {


        try {
            System.out.println("获取用户名！！！！！");
            //从token中解析token数据
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());


            // 解析成功要重新刷新token
            token = JwtUtils.generateToken(userInfo, this.prop.getPrivateKey(), this.prop.getExpire());
            // 更新cookie中的token
            CookieUtils.setCookie(request, response, this.prop.getCookieName(), token, this.prop.getCookieMaxAge());


            //解析成功后返回用户信息
            return ResponseEntity.ok(userInfo);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }
}