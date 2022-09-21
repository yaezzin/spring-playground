package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.CreateOrderCartReq;
import com.example.demo.src.product.ProductProvider;
import com.example.demo.src.product.ProductService;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.example.demo.config.BaseResponseStatus.SUCCESS_CREATE_CART;

@RestController
@RequestMapping("/app/orders")
public class OrderController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final OrderProvider orderProvider;
    @Autowired
    private final OrderService orderService;
    @Autowired
    private final JwtService jwtService;

    public OrderController(OrderProvider orderProvider, OrderService orderService, UserProvider userProvider, JwtService jwtService){
        this.orderProvider = orderProvider;
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    /* 카드에 담기 */
    @ResponseBody
    @PostMapping("/cart")
    public BaseResponse<String> createCart(@RequestBody CreateOrderCartReq createOrderCartReq){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (createOrderCartReq.getUserIdx() != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            orderService.createCart(createOrderCartReq);
            return new BaseResponse<>(SUCCESS_CREATE_CART);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 카트에서 지우기 */

    /* 주문하기 */
}
