package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.CreateOrderCartReq;
import com.example.demo.src.order.model.DeleteOrderCartReq;
import com.example.demo.src.product.ProductProvider;
import com.example.demo.src.product.ProductService;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/orders")
public class OrderController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final OrderProvider orderProvider;
    @Autowired
    private final OrderService orderService;
    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final JwtService jwtService;

    public OrderController(OrderProvider orderProvider, OrderService orderService, ProductProvider productProvider, JwtService jwtService){
        this.orderProvider = orderProvider;
        this.orderService = orderService;
        this.productProvider = productProvider;
        this.jwtService = jwtService;
    }

    /* 장바구니에 담기 */
    @ResponseBody
    @PostMapping("/cart")
    public BaseResponse<String> createCart(@RequestBody CreateOrderCartReq createOrderCartReq){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (createOrderCartReq.getUserIdx() != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if (productProvider.checkProductExist(createOrderCartReq.getProductIdx()) == 0) {
                return new BaseResponse<>(EMPTY_PRODUCT);
            }
            if (orderProvider.checkCartExist(createOrderCartReq.getUserIdx(), createOrderCartReq.getProductIdx()) != 0) {
                return new BaseResponse<>(POST_CART_NOT_EXIST);
            }
            orderService.createCart(createOrderCartReq);
            return new BaseResponse<>(SUCCESS_CREATE_CART);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 장바구니에서 지우기 */
    @ResponseBody
    @PatchMapping("/cart")
    public BaseResponse<String> deleteCart(@RequestBody DeleteOrderCartReq deleteOrderCartReq){
        try {
            // 유저 권한 확인
            int userIdxByJwt = jwtService.getUserIdx();
            if (deleteOrderCartReq.getUserIdx() != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // 상품 있는지 확인
            if (productProvider.checkProductExist(deleteOrderCartReq.getProductIdx()) == 0) {
                return new BaseResponse<>(EMPTY_PRODUCT);
            }
            // 이미 삭제했는데 또 요청하는 경우 에러발생
            if (orderProvider.checkCartExist(deleteOrderCartReq.getUserIdx(), deleteOrderCartReq.getProductIdx()) == 0) {
                return new BaseResponse<>(PATCH_CART_NOT_EXIST);
            }
            orderService.deleteCart(deleteOrderCartReq);
            return new BaseResponse<>(SUCCESS_DELETE_CART);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 주문하기 */



}
