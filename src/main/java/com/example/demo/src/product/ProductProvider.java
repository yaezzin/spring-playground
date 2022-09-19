package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.GetProdRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.EMPTY_CATEGORY_IDX;

@Service
public class ProductProvider {

    private final ProductDao productDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductProvider(ProductDao productDao, JwtService jwtService) {
        this.productDao = productDao;
        this.jwtService = jwtService;
    }

    public List<GetProdRes> getProductsByKeyword(String keyword) throws BaseException {
        try {
            List<GetProdRes> productsByKeyword = productDao.getProductsByKeyword(keyword);
            return productsByKeyword;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProdRes> getProductsByCategory(int categoryIdx) throws BaseException {
        try {
            List<GetProdRes> productsByCategory = productDao.getProductsByCategory(categoryIdx);
            return productsByCategory;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
