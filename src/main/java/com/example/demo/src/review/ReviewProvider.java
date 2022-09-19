package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ReviewProvider {

    private final ReviewDao reviewDao;
    private final JwtService jwtService;

    @Autowired
    public ReviewProvider(ReviewDao reviewDao, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
    }

    public List<GetReviewRes> getReviews(int productIdx) throws BaseException {
        try {
            // 리뷰 목록 가져오기
            List<GetReviewRes> reviews = reviewDao.getReviews(productIdx);
            // 각 리뷰의 이미지 배열
            for (GetReviewRes review : reviews) {
                List<String> reviewImageUrl = reviewDao.getReviewImages(review.getReviewIdx());
                review.setReviewImageUrl(reviewImageUrl);
            }
            return reviews;

        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
