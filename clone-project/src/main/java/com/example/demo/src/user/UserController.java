package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;


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

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /* 닉네임으로 회원 전체 조회 API - (프로필 사진, 닉네임, userIdx, 주소 필요) */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String nickname) {
        try {
            if (nickname == null) {
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            List<GetUserRes> getUsersRes = userProvider.getUsersByNickname(nickname);
            return new BaseResponse<>(getUsersRes);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* userIdx로 회원 단건 조회 API */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();     // 1. dx를 사용하려면 jwt에서 idx를 추출
            if (userIdx != userIdxByJwt) {                  // 2. userIdx 와 접근 유저가 같은지 확인
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // 같으면 조회
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 회원 가입 API */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        /* < 예외처리 >
         * 1. 핸드폰 번호, 닉네임, 패스워드가 null이면 예외
         * 2. 핸드폰 번호, 패스워드가 형식에 맞지 않으면 예외
         */

        if (postUserReq.getPhoneNumber() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER);
        }
        if (!isRegexPhoneNumber(postUserReq.getPhoneNumber())) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
        }
        if (postUserReq.getNickname() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
        }
        if (!isRegexPassword(postUserReq.getPassword())) {
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
        try {
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 로그인 API */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {
        try {
            PostLoginRes postLoginRes = userProvider.login(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /* 유저 정보 수정 API - 프로필 사진, 닉네임 */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> modifyUser(@PathVariable("userIdx") int userIdx, @RequestBody PatchUserReq patchUserReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // 유저 네임 + 프로필 사진 변경 부분
            patchUserReq = new PatchUserReq(userIdx, patchUserReq.getNickname(), patchUserReq.getProfileImage());
            userService.modifyUser(patchUserReq);
            return new BaseResponse<>(SUCCESS_MODIFY_USER);

        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 유저 탈퇴 API */
    @ResponseBody
    @DeleteMapping("/{userIdx}")
    public BaseResponse<String> deleteUser(@PathVariable("userIdx") int userIdx, @RequestBody DeleteUserReq deleteUserReq) {
        if (deleteUserReq.getDeleteReason() == null) {
            return new BaseResponse<>(EMPTY_DELETE_ACCOUNT_REASON);
        }
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // 유저 네임 + 프로필 사진 변경 부분
            deleteUserReq = new DeleteUserReq(userIdx, deleteUserReq.getDeleteReason());
            userService.deleteUser(deleteUserReq);

            return new BaseResponse<>(SUCCESS_DELETE_USER);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 유저 활동 배지 생성*/
    @ResponseBody
    @PostMapping("/{userIdx}/badges")
    public BaseResponse<String> createUserBadge(@PathVariable("userIdx") int userIdx, @RequestBody PostBadgeReq postBadgeReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.createUserBadge(postBadgeReq);
            return new BaseResponse<>(SUCCESS_CREATE_BADGE);

        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 유저 활동 배지 조회*/
    @ResponseBody
    @GetMapping("/{userIdx}/badges")
    public BaseResponse<List<GetBadgeRes>> getUserBadges(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetBadgeRes> getBadgeRes = userProvider.getUserBadges(userIdx);
            return new BaseResponse<>(getBadgeRes);

        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 소셜 로그인 */
    @ResponseBody
    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam String code, HttpSession session) throws BaseException {
        System.out.println(code);
        String access_Token = userProvider.getAccessToken(code);
        System.out.println("controller access_token : " + access_Token);

        HashMap<String, Object> userInfo = userProvider.getUserInfo(access_Token);
        System.out.println("login Controller : " + userInfo);

        //    클라이언트의 이메일이 존재할 때 세션에 해당 이메일과 토큰 등록
        if (userInfo.get("email") != null) {
            session.setAttribute("userId", userInfo.get("email"));
            session.setAttribute("access_Token", access_Token);
        }
    }
}
