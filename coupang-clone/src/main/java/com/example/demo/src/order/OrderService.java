package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.CreateOrderCartReq;
import com.example.demo.src.order.model.DeleteOrderCartReq;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.product.ProductProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class OrderService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderDao orderDao;
    private final OrderProvider orderProvider;
    private final JwtService jwtService;

    @Autowired
    public OrderService(OrderDao orderDao, OrderProvider orderProvider, JwtService jwtService) {
        this.orderDao = orderDao;
        this.orderProvider = orderProvider;
        this.jwtService = jwtService;
    }


    public void createCart(CreateOrderCartReq createOrderCartReq) throws BaseException {
        try {
            int result = orderDao.createCart(createOrderCartReq);
            if (result == 0) {
                throw new BaseException(CREATE_FAIL_CART);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteCart(DeleteOrderCartReq deleteOrderCartReq) throws BaseException {
        try {
            int result = orderDao.deleteCart(deleteOrderCartReq);
            if (result == 0) {
                throw new BaseException(DELETE_FAIL_CART);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
