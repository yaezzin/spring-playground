package com.example.demo.src.mypage;

import com.example.demo.config.BaseException;
import com.example.demo.src.mypage.model.GetMyProdRes;
import com.example.demo.src.mypage.model.GetWishRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.GetBadgeRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class MyPageProvider {

    private final MyPageDao myPageDao;
    private final JwtService jwtService;

    @Autowired
    public MyPageProvider(MyPageDao myPageDao, JwtService jwtService) {
        this.myPageDao = myPageDao;
        this.jwtService = jwtService;
    }

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<GetWishRes> getUserWish(int userIdx) throws BaseException {
        try {
            List<GetWishRes> getWishRes = myPageDao.getUserWish(userIdx);
            return getWishRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetMyProdRes> getUserProducts(int userIdx) throws BaseException {
        try {
            List<GetMyProdRes> getMyProdRes = myPageDao.getUserProducts(userIdx);
            return getMyProdRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}


