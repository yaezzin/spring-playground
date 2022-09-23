package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPassword;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }

    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        if(userProvider.checkEmail(postUserReq.getEmail()) == 1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        if(userProvider.checkPhoneNumber(postUserReq.getPhoneNumber()) == 1){
            throw new BaseException(POST_USERS_EXISTS_PHONE_NUMBER);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserProfile(PatchUserReq patchUserReq) throws BaseException {
        try {
            int result = userDao.modifyUserProfile(patchUserReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER_PROFILE);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteUser(int userIdx, PatchUserStatusReq patchUserStatusReq) throws BaseException {
        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(patchUserStatusReq.getPassword());
            patchUserStatusReq.setPassword(encryptPwd);
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if (userProvider.checkPassword(patchUserStatusReq.getEmail(), encryptPwd) == 0) {
            throw new BaseException(FAILED_TO_LOGIN);
        }

        try {
            int result = userDao.deleteUser(userIdx);
            if (result == 0) {
                throw new BaseException(DELETE_FAIL_USER);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createAddress(PostUserAddressReq postUserAddressRequest) throws BaseException {
        try {
            int result = userDao.createAddress(postUserAddressRequest);
            if (result == 0) {
                throw new BaseException(CREATE_FAIL_ADDRESS);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyAddress(PatchUserAddressReq patchUserAddressReq) throws BaseException {
        try {
            int result = userDao.updateAddress(patchUserAddressReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_ADDRESS);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyDefaultAddress(int userAddressIdx) throws BaseException {
        try {
            int result = userDao.modifyDefaultAddress(userAddressIdx);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_DEFAULT_ADDRESS);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public UserAddressIdxRes getDefaultAddressIdx(int idx) throws BaseException {
        try {
            return userDao.getDefaultAddressIdx(idx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
       }
    }

    public void deleteAddress(int userAddressIdx) throws BaseException {
        try {
            int result = userDao.deleteAddress(userAddressIdx);
            if (result == 0) {
                throw new BaseException(DELETE_FAIL_DEFAULT_ADDRESS);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);

        }
    }
}
