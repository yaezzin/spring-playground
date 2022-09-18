package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.product.ProductProvider;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.PostReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.CREATRE_FAIL_REVIEW;
import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

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
                throw new BaseException(CREATRE_FAIL_REVIEW);
            }
            return new PostReviewRes(postReviewRes);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
