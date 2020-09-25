package com.leyou.user.service;

import com.leyou.common.utils.CodecUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    //用户名
    public User queryByUsername(String data) {
        User user = new User();
        user.setUsername(data);
        User user1 = userMapper.selectOne(user);
        return user1;
    }

    //手机号
    public User queryByPhone(String data) {
        User user = new User();
        user.setPhone(data);
        User user1 = userMapper.selectOne(user);
        return user1;
    }


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;
    //根据手机号生成验证码并发送服务，参数（手机号，验证码）
    public void sendVerifyCode(String phone) {

        //首先生成验证码：
        String code = NumberUtils.generateCode(6);

        //其次要把验证码存在redis里,但是存值的时候使用的要携带有效时间
          redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

        //然后就是发送微服务，也就是发送短信,但是发送结构为map，谨记！！！
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("phone",phone);
        map.put("code",code);
        //调用convertandsend
        //第一个参数为交换机名，第二个名称为路由键，第三个名称为发送给的数据  sms.verify.code
        try {

            amqpTemplate.convertAndSend("leyou.sms.exchange","sms.verify.code",map);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void addUser(User user, String code) {
        //首先判断验证码是否正确
        String redisCode = (String)redisTemplate.opsForValue().get(user.getPhone());
        if(!redisCode.equals(code)){
            return ;
        }

        //密码加密
        //首先获取盐
        String salt = CodecUtils.generateSalt();
        String md5Hex = CodecUtils.md5Hex(user.getPassword(), salt);

        //将新的加密密码加入进去
        user.setPassword(md5Hex);
        user.setCreated(new Date());
        user.setSalt(salt);
        //执行注册方法
       userMapper.insert(user);
    }

    public User queryByUsernameAndPassword(String username, String password) {

        //查询。。根据username（username是唯一的）
        User oldUser = new User();
        oldUser.setUsername(username);
        User user = userMapper.selectOne(oldUser);
        if(user == null){
            return null;
        }

        //先将password转换为加密密码，并且是相同的盐
        String newPassword = CodecUtils.md5Hex(password, user.getSalt());


        //判断加密后两次密码是否一致,不相等进入判断
        if(!(user.getPassword().equals(newPassword))){
            return null;
        }
        oldUser.setCreated(user.getCreated());
        oldUser.setPassword(password);
        oldUser.setId(user.getId());
        return oldUser;
    }
}
