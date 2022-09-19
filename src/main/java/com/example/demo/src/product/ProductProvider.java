package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.GetProdByKeywordRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

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

    public List<GetProdByKeywordRes> getProductsByKeyword(String keyword) throws BaseException {
        //try {
            List<GetProdByKeywordRes> productsByKeyword = productDao.getProductsByKeyword(keyword);
            return productsByKeyword;
        //} catch (Exception exception) {
        //    throw new BaseException(DATABASE_ERROR);
        //}
    }
}
