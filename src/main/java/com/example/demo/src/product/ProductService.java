package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.PostProdReq;
import com.example.demo.src.product.model.PostProdRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ProductService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;
    private final ProductProvider productProvider;

    @Autowired
    public ProductService(ProductDao productDao, ProductProvider productProvider) {
        this.productDao = productDao;
        this.productProvider = productProvider;
    }

    @Transactional
    public PostProdRes createProduct(PostProdReq postProdReq) throws BaseException {
         try {
             PostProdRes result = productDao.createProduct(postProdReq);
             return result;
         } catch (Exception exception) {
           throw new BaseException(DATABASE_ERROR);
        }
    }
}
