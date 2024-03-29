package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPassword;

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

    public void modifyUserPassword(int userIdx, PatchUserPasswordReq patchUserPasswordReq) throws BaseException {
        // 현재 비밀번호 입력창에 입력한 비밀번호가 DB에 있는 비밀번호와 다르면 에러 발생
        String encryptPwd;
        encryptPwd = new SHA256().encrypt(patchUserPasswordReq.getCurrentPassword());
        System.out.println(encryptPwd);
        System.out.println(userDao.getUser(userIdx).getPassword());
        if (!encryptPwd.equals(userDao.getUser(userIdx).getPassword())) {
            throw new BaseException(USER_CURRENT_PASSWORD_NOT_CORRECT);
        }

        // 변경하려는 비밀번호가 정규식에 맞는지 확인
        if (!isRegexPassword(patchUserPasswordReq.getModPassword())) {
            throw new BaseException(POST_USERS_INVALID_PASSWORD);
        }

        // 변경하려는 비밀번호와 새비밀번호가 일치하는지 확인
        if (!patchUserPasswordReq.getModPassword().equals(patchUserPasswordReq.getReModPassword())) {
            throw new BaseException(USER_NEW_PASSWORD_NOT_CORRECT);
        }

        try{
            int result = userDao.modifyUserPassword(userIdx, patchUserPasswordReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_PASSWORD);
            }
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void certifyPhoneNumber(String phoneNumber, String cerNum) {
        String api_key = "NCSLRZXAVGPNLAHP";
        String api_secret = "OEEGT5TXRHY3ZPXWJGYM3CISCUXMMFXF";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", "발송할 번호 입력");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "핫띵크 휴대폰인증 테스트 메시지 : 인증번호는" + "["+ cerNum +"]" + "입니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

    public void certifyPhoneNumberSave(String phoneNumber, String numStr) throws BaseException {
        try {
            int result = userDao.certifyPhoneNumberSave(phoneNumber, numStr);
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
