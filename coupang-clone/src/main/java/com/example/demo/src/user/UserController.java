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
import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

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

        // 1. null 값 확인
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

        // 2. 이메일 확인
        if (!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        // 3. 비밀번호 확인
        if (!isRegexPassword(postUserReq.getPassword())) {
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
        if (!isRegexPasswordLength(postUserReq.getPassword())) {
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD_LENGTH);
        }

        if (postUserReq.getPassword().equals(postUserReq.getEmail())) {
            return new BaseResponse<>(POST_USERS_EMAIL_IN_PASSWORD);
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

    /* 비밀번호 수정 */
    @ResponseBody
    @PatchMapping("/{userIdx}/password")
    public BaseResponse<GetUserRes> modifyUserPassword(@PathVariable("userIdx") int userIdx, @RequestBody PatchUserPasswordReq patchUserPasswordReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.modifyUserPassword(userIdx, patchUserPasswordReq);
            return new BaseResponse<>(SUCCESS_MODIFY_USER_PASSWORD);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 회원 탈퇴 */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> deleteUser(@PathVariable("userIdx") int userIdx,
                                           @RequestBody PatchUserStatusReq patchUserStatusReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.deleteUser(userIdx, patchUserStatusReq);
            return new BaseResponse<>(SUCCESS_DELETE_USER);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
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

    /* 유저 프로필 사진 변경 */
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

    /* 배송지 추가 */
    @ResponseBody
    @PostMapping("/{userIdx}/address")
    public BaseResponse<String> createAddress(@PathVariable("userIdx") int userIdx, @RequestBody PostUserAddressReq postUserAddressReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if (postUserAddressReq.getPhoneNumber() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER);
            }
            if (postUserAddressReq.getRecipient() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_NAME);
            }
            if (!isRegexPhoneNumber(postUserAddressReq.getPhoneNumber())){
                return new BaseResponse<>(POST_USER_INVALID_PHONE_NUMBER);
            }
            if (!isRegexUserName(postUserAddressReq.getRecipient())) {
                return new BaseResponse<>(POST_USER_INVALID_RECIPIENT);
            }
            // 기본 배송지 설정이 되어있는데 Y로 요청하게 되면 -> 기존것은 n로 새로 추가하는 것을 y로 설정하기
            if (userProvider.checkDefaultAddressExist(userIdx) == 1) {
                if (postUserAddressReq.getIsDefaultAddress().equals("Y")) {
                    UserAddressIdxRes userAddressIdx = userService.getDefaultAddressIdx(userIdx);
                    userService.modifyDefaultAddress(userAddressIdx.getUserAddressIdx()); // 기존의 주소는 N으로 바꾸기
                    userService.createAddress(postUserAddressReq);
                } else { // 기존 배송지 설정이 되어있고 N으로 요청하는 경우
                    userService.createAddress(postUserAddressReq);
                }
            }
            else {
                userService.createAddress(postUserAddressReq);
            }
            return new BaseResponse<>(SUCCESS_CREATE_ADDRESS);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 배송지 변경 */
    @ResponseBody
    @PatchMapping("/{userIdx}/address")
    public BaseResponse<String> modifyAddress(@PathVariable("userIdx") int userIdx, @RequestBody PatchUserAddressReq patchUserAddressReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if (patchUserAddressReq.getPhoneNumber() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER);
            }
            if (patchUserAddressReq.getRecipient() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_NAME);
            }
            if (!isRegexPhoneNumber(patchUserAddressReq.getPhoneNumber())){
                return new BaseResponse<>(POST_USER_INVALID_PHONE_NUMBER);
            }
            if (!isRegexUserName(patchUserAddressReq.getRecipient())) {
                return new BaseResponse<>(POST_USER_INVALID_RECIPIENT);
            }

            if (userProvider.checkDefaultAddressExist(userIdx) == 1) {
                if (patchUserAddressReq.getIsDefaultAddress().equals("Y")) {
                    UserAddressIdxRes userAddressIdx = userService.getDefaultAddressIdx(userIdx);
                    userService.modifyDefaultAddress(userAddressIdx.getUserAddressIdx()); // 기존의 주소는 N으로 바꾸기
                    userService.modifyAddress(patchUserAddressReq);
                } else {
                    userService.modifyAddress(patchUserAddressReq);
                }
            }
            else {
                userService.modifyAddress(patchUserAddressReq);
            }
            return new BaseResponse<>(SUCCESS_MODIFY_ADDRESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 유저의 모든 배송지 조회 */
    @ResponseBody
    @GetMapping("/{userIdx}/address")
    public BaseResponse<List<GetUserAddress>> getAddress(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetUserAddress> userAddress = userProvider.getUserAddress(userIdx);
            return new BaseResponse<>(userAddress);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 배송지 삭제 */
    @ResponseBody
    @PatchMapping("/{userIdx}/address/{userAddressIdx}")
    public BaseResponse<String> deleteAddress(@PathVariable("userIdx") int userIdx,
                                              @PathVariable("userAddressIdx") int userAddressIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // 배송지가 있는지 체크
            userService.deleteAddress(userAddressIdx);
            return new BaseResponse<>(SUCCESS_DELETE_ADDRESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 휴대폰 인증문자 발송 */
    @ResponseBody
    @PostMapping("/message")
    public BaseResponse<String> sendSms(@RequestBody PostSmsReq postSmsReq) throws BaseException {

        String phoneNumber = postSmsReq.getPhoneNumber();

        if (postSmsReq.getPhoneNumber() != null) {
            if (! isRegexPhoneNumber(postSmsReq.getPhoneNumber())) {
                return new BaseResponse<>(POST_USER_INVALID_PHONE_NUMBER);
            }
        }
        // 난수 생성
        Random random = new Random();
        String numStr = "";
        for (int i=0; i < 4; i ++) {
            String ran = Integer.toString(random.nextInt(10));
            numStr += ran;
        }
        System.out.println("수신자 번호 : " + phoneNumber);
        System.out.println("인증번호 : " + numStr);

        userService.certifyPhoneNumber(phoneNumber, numStr); // 발송
        userService.certifyPhoneNumberSave(phoneNumber, numStr); // 저장
        return new BaseResponse<>(numStr);
    }

    /* 휴대폰 인증번호 확인 */
    @ResponseBody
    @PostMapping("/message/check")
    public BaseResponse<String> checkSms(@RequestBody PostSmsCheckReq postSmsCheckReq) throws BaseException {
        // 핸드폰 형식 확인
        if (postSmsCheckReq.getPhoneNumber() != null) {
            if (!isRegexPhoneNumber(postSmsCheckReq.getPhoneNumber())) {
                return new BaseResponse<>(POST_USER_INVALID_PHONE_NUMBER);
            }
        }

        // 번호 확인
        if (userProvider.checkCertificationPhone(postSmsCheckReq.getPhoneNumber()) == 0) {
            return new BaseResponse<>(EMPTY_CERTIFICATION_NUMBER);
        }

        int timeDiff = userProvider.checkCertificationTime(postSmsCheckReq.getPhoneNumber());
        if(timeDiff>=10000){
            return new BaseResponse<>(FAILED_TO_CERTIFICATION_TIME);
        }

        // 인증번호 확인
        if (!(userProvider.checkCertificationNum(postSmsCheckReq.getPhoneNumber(), postSmsCheckReq.getCertificationNum()))){
            return new BaseResponse<>(FAILED_TO_CERTIFICATION);
        }
        return new BaseResponse<>(SUCCESS_SMS_CHECK);
    }


}
