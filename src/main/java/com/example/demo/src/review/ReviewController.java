package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

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

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService){
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
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
    @GetMapping("/{productIdx}")
    public BaseResponse<List<GetReviewRes>> getReviews(@PathVariable("productIdx") int productIdx) {
        try {
            List<GetReviewRes> getReviewRes = reviewProvider.getReviews(productIdx);
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

    /* 리뷰 프리뷰 조회 - 3개 */


    /* 리뷰 필터링 - 키워드 (품질, 배송, 만족도) */
    /* 상품별 리뷰 사진만 전체 조회 */
    /* 리뷰 수정 */
    /* 리뷰 삭제 */
    /* 유저가 작성한 리뷰 조회 */

}
