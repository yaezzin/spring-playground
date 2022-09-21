package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;
import static com.example.demo.utils.ValidationRegex.isRegexPassword;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        /* validation
         * 1. 핸드폰 번호, 이름, 패스워드, 이메일이 null이면 예외
         * 2. 패스워드가 형식에 (영문, 숫자, 특수문자 중 2가지이상 조합 // 8~20자 // 이메일제외)
         */
        if (postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        if (postUserReq.getPhoneNumber() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER);
        }
        if (postUserReq.getPassword() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        if (postUserReq.getUserName() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }
        if (!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        if (!isRegexPassword(postUserReq.getPassword())) {
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
        if (postUserReq.getPassword() == postUserReq.getEmail()) {
            return new BaseResponse<>(POST_USERS_EMAIL_IN_PASSWORD); // 패스워드는 이메일일 수 없음
        }

        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 로그인 */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq postLoginReq){
        try{
            if (userProvider.checkUserEmail(postLoginReq.getEmail()) == 0) {
                return new BaseResponse<>(USERS_EMPTY_EMAIl);
            }
            PostLoginRes postLoginRes = userProvider.login(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /* userIdx로 회원 단건 조회 */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/{userIdx}/profile")
    public BaseResponse<String> modifyUserProfile(@PathVariable("userIdx") int userIdx, @RequestBody PatchUserReq patchUserReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.modifyUserProfile(patchUserReq);
            return new BaseResponse<>(SUCCESS_MODIFY_USER_PROFILE);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 유저가 찜한 상품 목록 조회 */
    @ResponseBody
    @GetMapping("/{userIdx}/wish")
    public BaseResponse<List<GetUserProdWishRes>> getUserProductWish(@PathVariable("userIdx") int userIdx) {
       try {
           int userIdxByJwt = jwtService.getUserIdx();
           if (userIdx != userIdxByJwt) {
               return new BaseResponse<>(INVALID_USER_JWT);
           }
           List<GetUserProdWishRes> userProductWish = userProvider.getUserProductWish(userIdx);
           return new BaseResponse<>(userProductWish);
       } catch (BaseException exception) {
           exception.printStackTrace();
           return new BaseResponse<>((exception.getStatus()));
       }
    }

    /* 유저의 장바구니 조회 - 배송타입에 따른 분류 */
    @ResponseBody
    @GetMapping("/{userIdx}/cart")
    public BaseResponse<List<GetUserCartRes>> getUserCart(@PathVariable("userIdx") int userIdx,
                                                          @RequestParam("deliveryType") int deliveryType) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetUserCartRes> userProductWish = userProvider.getUserCart(userIdx, deliveryType);
            return new BaseResponse<>(userProductWish);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 장바구니 담기 */


}
