package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.product.ProductProvider;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.PatchReviewReq;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.PostReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ReviewService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewDao reviewDao;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;

    @Autowired
    public ReviewService(ReviewDao reviewDao, ReviewProvider reviewProvider, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.reviewProvider = reviewProvider;
        this.jwtService = jwtService;
    }

    public PostReviewRes createReview(PostReviewReq postReviewReq) throws BaseException {
        try {
            int postReviewRes = reviewDao.createReview(postReviewReq);
            if (postReviewRes == 0) {
                throw new BaseException(CREATE_FAIL_REVIEW);
            }
            return new PostReviewRes(postReviewRes);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createReviewHelp(int userIdx, int reviewIdx, String isHelp) throws BaseException {
        try {
            int result = reviewDao.createReviewHelp(userIdx, reviewIdx, isHelp);
            if (result == 0){
                throw new BaseException(CREATE_FAIL_REVIEW_HELP);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public void modifyReview(PatchReviewReq patchReviewReq) throws BaseException {
        // 1. 유저 존재 검증
        // 2. 리뷰 존재 검증
        // 3. 리뷰 == 유저 검증

        try {
            // 리뷰 수정
            reviewDao.modifyReview(patchReviewReq);
            // 리뷰 이미지가 수정된 기록이 있으면 수정하기
            if (patchReviewReq.getModifiedFlag().equalsIgnoreCase("Y")) {
                reviewDao.updateStatusReviewImages(patchReviewReq.getReviewIdx()); // 이미지 삭제 후 다시 저장
                if (patchReviewReq.getReviewImageUrl() != null) {
                    for (String image : patchReviewReq.getReviewImageUrl()){
                        reviewDao.insertReviewImage(patchReviewReq.getReviewIdx(), image);
                    }
                }
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createReviewHelpDelete(int userIdx, int reviewIdx, String isHelp) throws BaseException {
        try {
            int deleteSuccess = reviewDao.deleteExistsReviewHelp(userIdx, reviewIdx);
            if (deleteSuccess == 0){
                throw new BaseException(DELETE_FAIL_EXISTS_REVIEW_HELP);
            }
        
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
