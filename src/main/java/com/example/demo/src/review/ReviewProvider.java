package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.GetReviewPreRes;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.GetUserReviewRes;
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

    public List<String> getReviewPhotos(int productIdx) throws BaseException {
        try {
            List<String> photos = reviewDao.getReviewPhotos(productIdx);
            return photos;
        }  catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public GetReviewRes getReviewsByReviewIdx(int reviewIdx) throws BaseException {
        GetReviewRes review = reviewDao.getReviewByReviewIdx(reviewIdx);
        List<String> reviewImageUrl = reviewDao.getReviewImages(reviewIdx);
        review.setReviewImageUrl(reviewImageUrl);
        return review;
    }

    public List<GetReviewRes> getReviewsByStar(int productIdx, int star) throws BaseException {
        try {
            List<GetReviewRes> reviewsByStar = reviewDao.getReviewsByStar(productIdx, star);
            for (GetReviewRes review : reviewsByStar) {
                List<String> reviewImageUrl = reviewDao.getReviewImages(review.getReviewIdx());
                review.setReviewImageUrl(reviewImageUrl);
            }
            return reviewsByStar;
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public String checkAlreadyReviewHelp(int userIdx, int reviewIdx) throws BaseException {
        try {
            return reviewDao.checkAlreadyReviewHelp(userIdx, reviewIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetReviewPreRes> getReviewPreview(int productIdx) throws BaseException {
        try {
            return reviewDao.getReviewPreview(productIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserReviewRes> getUserReview(int userIdx) throws BaseException {
        try {
            return reviewDao.getUserReview(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkReviewExist(int reviewIdx) throws BaseException {
        try {
            return reviewDao.checkReviewExist(reviewIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkProductExist(int productIdx) throws BaseException {
        try {
            return reviewDao.checkProductExist(productIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
