package com.leyou.cart.service;

import com.leyou.auth.entity.UserInfo;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.api.GoodsApi;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {

    //注入redis
    @Autowired
    private RedisTemplate redisTemplate;
    //@Autowired
    //private GoodsClient goodsClient;
    @Autowired
    private GoodsApi goodsApi;

    /**
     * @param cart
     * @return boolean
     */
    public Boolean addCart(Cart cart) {


        /**
         * 逻辑：
         *首先获取用户id，用到了redis中的hash
         *然后就是：
         *    1：获取 reids数据
         *    2：判断是否有该商品
         *      2.1：如果有，就将数据num更新
         *      2.2：如果没有，就直接添加进去
         *
         *
         *
         *
         *
         *
         */


        try {

            //获取登录中用户id
            UserInfo loginUser = LoginInterceptor.getLoginUser();

            //取出后作为hash的key
            Long userid = loginUser.getId();

            //根据id获取redis的数据(操作对象中有)
            BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(userid);

            //获取skuid
            Long skuId = cart.getSkuId();

            //获取num值
            Integer num = cart.getNum();

            //根据id查询是否存在
            Boolean carBoolean = hashOperations.hasKey(skuId.toString());

            //判断是否存在
            if (carBoolean) {

                //存在，则获取json格式对象，并转为java对象方便操作
                String json = hashOperations.get(skuId.toString()).toString();
                Cart c = JsonUtils.parse(json, Cart.class);

                //修改数量
                c.setNum(c.getNum() + num);

            } else {

                //不存在,则新增购物车，需要根据id查询出来该商品
                Sku sku = goodsApi.queryBySkuId(skuId);

                //，并且赋值给cart
                cart.setUserId(sku.getId());
                cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
                cart.setPrice(sku.getPrice());
                cart.setTitle(sku.getTitle());
                cart.setOwnSpec(sku.getOwnSpec());

            }

            //最后再将数据写入redis中
            hashOperations.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));

        } catch (Exception e) {
            return false;
        }
        return true;
    }



    public List<Cart> queryCartList() {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();

        // 判断是否存在购物车
        String key = user.getId().toString();
        if(redisTemplate.hasKey(key)){
            // 不存在，直接返回
            return null;
        }
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        List<Object> carts = hashOps.values();
        // 判断是否有数据
        if(CollectionUtils.isEmpty(carts)){
            return null;
        }
        // 查询购物车数据
        return carts.stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());
    }
}
