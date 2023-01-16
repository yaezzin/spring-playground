package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.GetProdDetailRes;
import com.example.demo.src.product.model.GetProdRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ProductProvider {

    @Autowired private final ProductDao productDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductProvider(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public List<GetProdRes> getProducts() throws BaseException {
        try {
            List<GetProdRes> getProdRes = productDao.getProducts();
            return getProdRes;
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<GetProdRes> getProductsByTitle(String title) throws BaseException {
        try {
            List<GetProdRes> getProdRes = productDao.getProductsByTitle(title);
            return getProdRes;
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<GetProdRes> getProductsByCategory(int categoryIdx) throws BaseException {
        try {
            List<GetProdRes> getProdRes = productDao.getProductsByCategory(categoryIdx);
            return getProdRes;
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<GetProdDetailRes> getProduct(int productIdx) throws BaseException {
        List<GetProdDetailRes> getProdDetailRes = productDao.getProduct(productIdx);
        return getProdDetailRes;
    }

}
