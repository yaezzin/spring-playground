package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

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

   @Transactional
   public void createWish(PostWishReq postWishReq) throws BaseException {
        try {
            productDao.createWish(postWishReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
   }

   @Transactional
   public void deleteWish(PostWishReq postWishReq) throws BaseException {
        try {
            productDao.deleteWish(postWishReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
   }

    @Transactional
    public void modifyProductInfo(PatchProdReq patchProdReq) throws BaseException {
        try {
            int result = productDao.modifyProductInfo(patchProdReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_PRODUCT);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void updateViewCount(int productIdx) throws BaseException {
        try {
            int result = productDao.updateViewCount(productIdx);
            if (result == 0) {
                throw new BaseException(UPDATE_FAIL_VIEW_COUNT);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void deleteProduct(int productIdx) throws BaseException {
        try {
            int result = productDao.deleteProduct(productIdx);
            if (result == 0) {
                throw new BaseException(DELETE_FAIL_PRODUCT);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void updatePulledAt(int productIdx) throws BaseException {
        try {
            int result = productDao.updatePulledAt(productIdx);
            if (result == 0) {
                throw new BaseException(UPDATE_FAIL_PULL);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
