package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping
public class CartController {

    @Autowired
    private CartService cartService;
    //
    //@Autowired
    //private AuthController authController;

    /**
     * type：post
     * return：void
     * param：Cart
     * @RequestBody
     */
    @PostMapping

    public  ResponseEntity<Void>  addCart(@RequestBody Cart cart,HttpServletResponse response, HttpServletRequest request){

        //进行调用方法添加

       Boolean flag =  cartService.addCart(cart);

       //添加失败返回false
       if(flag==false){
           return ResponseEntity.status(HttpStatus. INTERNAL_SERVER_ERROR).build();
       }


       ////String token = authController.TookenWill();
       // Cookie cookie = new Cookie("LY_TOKEN",token);
       // response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping
    public ResponseEntity<List<Cart>> queryCartList() {
        List<Cart> carts = this.cartService.queryCartList();
        if (carts == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(carts);
    }
}
