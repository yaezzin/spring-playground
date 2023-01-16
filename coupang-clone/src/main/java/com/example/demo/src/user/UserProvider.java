package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPhoneNumber(String phoneNumber) throws BaseException {
        try {
            return userDao.checkPhoneNumber(phoneNumber);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException{
        User user = userDao.getPwd(postLoginReq);
        String encryptPwd;
        try {
            encryptPwd=new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            int userIdx = user.getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    public GetUserRes getUser(int userIdx) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUser(int userIdx) throws BaseException {
        try {
            return userDao.checkUser(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserEmail(String email) throws BaseException {
        try {
            return userDao.checkUserEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserProdWishRes> getUserProductWish(int userIdx) throws BaseException {
        try {
            return userDao.getUserProductWish(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserCartRes> getUserCart(int userIdx, int deliveryType) throws BaseException {
        try {
            return userDao.getUserCart(userIdx, deliveryType);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPassword(String email, String encryptPwd) throws BaseException {
        try {
            return userDao.checkPassword(email, encryptPwd);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserAddress> getUserAddress(int userIdx) throws BaseException {
        try {
            return userDao.getUserAddress(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkDefaultAddressExist(int userIdx) throws BaseException {
        try {
            int i = userDao.checkDefaultAddressExist(userIdx);
            return i;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkCertificationPhone(String phoneNumber) throws BaseException {
        try {
            return userDao.checkCertificationPhone(phoneNumber);
        } catch (Exception exception){
            System.out.println("checkCertificationPhone"+exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean checkCertificationNum(String phoneNumber, String certificationNum) throws BaseException {
        try {
            return userDao.checkCertificationNum(phoneNumber, certificationNum);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public int checkCertificationTime(String phoneNumber) throws BaseException {
        try {
            return userDao.checkCertificationTime(phoneNumber);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
