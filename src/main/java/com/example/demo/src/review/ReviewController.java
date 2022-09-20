package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/reviews")
public class ReviewController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReviewProvider reviewProvider;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, UserProvider userProvider, JwtService jwtService){
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }

    /* 리뷰작성 */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostReviewRes> createReview(@RequestBody PostReviewReq postReviewReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (postReviewReq.getUserIdx() != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PostReviewRes postReviewRes = reviewService.createReview(postReviewReq);
            return new BaseResponse<>(postReviewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 상품별 리뷰 조회 */
    @ResponseBody
    @GetMapping("/products/{productIdx}")
    public BaseResponse<List<GetReviewRes>> getReviews(@PathVariable("productIdx") int productIdx) {
        try {
            List<GetReviewRes> getReviewRes = reviewProvider.getReviews(productIdx);
            return new BaseResponse<>(getReviewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 리뷰 개별 조회 by reviewIdx */
    @ResponseBody
    @GetMapping("/{reviewIdx}")
    public BaseResponse<GetReviewRes> getReviewsByReviewIdx(@PathVariable("reviewIdx") int reviewIdx) {
        try {
            GetReviewRes getReviewRes = reviewProvider.getReviewsByReviewIdx(reviewIdx);
            return new BaseResponse<>(getReviewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 리뷰 필터링 - 별점 (1-5)*/
    @ResponseBody
    @GetMapping("/{productIdx}/filter")
    public BaseResponse<List<GetReviewRes>> getReviewsByStar(@PathVariable("productIdx") int productIdx, @RequestParam int star) {
        try {
            List<GetReviewRes> getReviewResByStar = reviewProvider.getReviewsByStar(productIdx, star);
            return new BaseResponse<>(getReviewResByStar);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 리뷰 수정 */
    @ResponseBody
    @PatchMapping("/{reviewIdx}/users/{userIdx}")
    public BaseResponse<String> modifyReview(@PathVariable("reviewIdx") int reviewIdx,
                                             @PathVariable("userIdx") int userIdx,
                                             @RequestBody PatchReviewReq patchReviewReq) {
        try {
            // 리뷰가 존재하는지 확인
            if (reviewProvider.checkReviewExist(reviewIdx) == 0) {
                return new BaseResponse<>(EMPTY_REVIEW);
            }

            // 유저가 작성한 글인지 권한 확인
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            reviewService.modifyReview(patchReviewReq);
            return new BaseResponse<>(SUCCESS_MODIFY_REVIEW);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 리뷰 도움이 되었어요 설정*/
    @ResponseBody
    @PostMapping("/helpful")
    public BaseResponse<String> postReviewHelp(@RequestBody PostReviewHelpReq postReviewHelpReq) {
        try {
            // 1. 사용자 존재 여부 확인
            int userIdxByJwt = jwtService.getUserIdx();
            if (userProvider.checkUser(userIdxByJwt) == 0) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // 3. 이미 도움이 돼요를 설정했는지 확인 -> N or Y 리턴
            String isHelp = reviewProvider.checkAlreadyReviewHelp(userIdxByJwt, postReviewHelpReq.getReviewIdx());
            if (isHelp.equals(postReviewHelpReq.getIsHelp())) {
                return new BaseResponse<>(POST_REVIEW_HELP_EXIST);
            }

            // 4. 도움이 돼요를 누른 기록이 없고 Y로 요청한 경우 -> 도움이 돼요 누르기 (Y, Y)
            if (isHelp.equals("N")) {
                reviewService.createReviewHelp(userIdxByJwt, postReviewHelpReq.getReviewIdx(), postReviewHelpReq.getIsHelp());
            }
            // 5. 도움이 돼요를 누른 기록이 있고 N로 요청한 경우 -> 도움이 돼요 해제 (N, Y)
            else if (isHelp.equals("Y")){
                reviewService.createReviewHelpDelete(userIdxByJwt, postReviewHelpReq.getReviewIdx(), postReviewHelpReq.getIsHelp());
            }
            return new BaseResponse<>(SUCCESS_REVIEW_HELP);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 리뷰 프리뷰 조회 - 3개 */
    @ResponseBody
    @GetMapping("/{productIdx}/preview")
    public BaseResponse<List<GetReviewPreRes>> getReviewPreview(@PathVariable("productIdx") int productIdx) {
        try {
            List<GetReviewPreRes> preview = reviewProvider.getReviewPreview(productIdx);
            return new BaseResponse<>(preview);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 상품별 리뷰 사진만 전체 조회 */
    /* 리뷰 삭제 */
    /* 유저가 작성한 리뷰 조회 */

}
