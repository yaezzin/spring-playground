package com.example.demo.src.mypage;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.mypage.model.GetMyProdRes;
import com.example.demo.src.mypage.model.GetWishRes;
import com.example.demo.src.user.model.GetBadgeRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/mypage")
public class MyPageController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired private final MyPageProvider myPageProvider;
    @Autowired private final MyPageService myPageService;
    @Autowired private final JwtService jwtService;

    public MyPageController(MyPageProvider myPageProvider,MyPageService myPageService, JwtService jwtService) {
        this.myPageProvider = myPageProvider;
        this.myPageService = myPageService;
        this.jwtService = jwtService;
    }

    /* 마이페이지 - 유저의 찜목록 조회 */
    @ResponseBody
    @RequestMapping("/{userIdx}/wish")
    public BaseResponse<List<GetWishRes>> getUserWish(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetWishRes> getWishRes = myPageProvider.getUserWish(userIdx);
            return new BaseResponse<>(getWishRes);

        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 마이페이지 - 유저가 작성한 상품 게시물 조회 */
    @ResponseBody
    @GetMapping("/{userIdx}/products")
    public BaseResponse<List<GetMyProdRes>> getUserProducts(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetMyProdRes> getMyProdRes = myPageProvider.getUserProducts(userIdx);
            return new BaseResponse<>(getMyProdRes);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
