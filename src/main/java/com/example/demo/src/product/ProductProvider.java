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

    public List<GetProdRes> getProductsByHighPrice(Integer categoryIdx) throws BaseException {
        //try {
            List<GetProdRes> productsByKeyword = productDao.getProductsByHighPrice(categoryIdx);
            return productsByKeyword;
        //} catch (Exception exception) {
        //    throw new BaseException(DATABASE_ERROR);
       // }
    }

    public List<GetProdRes> getProductsByLowPrice(Integer categoryIdx) throws BaseException {
        //try {
            List<GetProdRes> productsByKeyword = productDao.getProductsByLowPrice(categoryIdx);
            return productsByKeyword;
        //} catch (Exception exception) {
        //    throw new BaseException(DATABASE_ERROR);
        //}
    }

    public List<GetProdRes> getProductsByKeywordAndHighPrice(String keyword) throws BaseException {
        try {
            List<GetProdRes> productsByKeyword = productDao.getProductsByKeywordAndHighPrice(keyword);
            return productsByKeyword;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProdRes> getProductsByKeywordAndLowPrice(String keyword) throws BaseException {
        try {
            List<GetProdRes> productsByKeyword = productDao.getProductsByKeywordAndLowPrice(keyword);
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

    public int checkProductExist(int productIdx) throws BaseException {
        try {
            return productDao.checkProductExist(productIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkProductWishExist(int productIdx, int userIdx) throws BaseException {
        try {
            return productDao.checkProductWishExist(productIdx, userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
