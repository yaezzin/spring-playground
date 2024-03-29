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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.example.demo.config.BaseResponseStatus.*;

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
        // 핸드폰 중복 여부 확인 -> 1 이면 있다는 것
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
        //try{
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        //} catch (Exception exception) {
        //    throw new BaseException(DATABASE_ERROR);
        //}
    }

    public void modifyUser(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUser(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER);
            }
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteUser(DeleteUserReq deleteUserReq) throws BaseException {
        try {
            int result = userDao.deleteUser(deleteUserReq);
            if (result == 0) {
                throw new BaseException(DELETE_FAIL_USER);
            }
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createUserBadge(PostBadgeReq postBadgeReq) throws BaseException{
        try {
            int result = userDao.createUserBadge(postBadgeReq);
            if (result == 0) {
                throw new BaseException(CREATE_FAIL_BADGE);
            }
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
