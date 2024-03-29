package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.PostProdReq;
import com.example.demo.src.product.model.PostProdRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ProductService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;
    private final ProductProvider productProvider;
    private final JwtService jwtService;

    @Autowired
    public ProductService(ProductDao productDao, ProductProvider productProvider, JwtService jwtService) {
        this.productDao = productDao;
        this.productProvider = productProvider;
        this.jwtService = jwtService;
    }

    public PostProdRes createProduct(PostProdReq postProdReq) throws BaseException {
        //try {
            PostProdRes result = productDao.createProduct(postProdReq);
            return result;
        //} catch (Exception exception) {
        //    throw new BaseException(DATABASE_ERROR);
        //}
    }

    public void createProductWish(int userIdx, int productIdx) throws BaseException {
        try {
            int result = productDao.createProductWish(userIdx, productIdx);
            if (result == 0) {
                throw new BaseException(CREATE_FAIL_PRODUCT_WISH);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteProductWish(int userIdx, int productIdx) throws BaseException {
        //try {
            int result = productDao.deleteProductWish(userIdx, productIdx);
            if (result == 0) {
                throw new BaseException(PUT_FAIL_PRODUCT_WISH);
            }
        //} catch (Exception exception) {
        //    throw new BaseException(DATABASE_ERROR);
        //}
    }
}
