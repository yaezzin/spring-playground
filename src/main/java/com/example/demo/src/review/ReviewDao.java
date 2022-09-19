package com.example.demo.src.review;

import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.PostReviewReq;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createReview(PostReviewReq postReviewReq) {
        String createReviewQuery = "insert into Review(title, description, startPoint, repImage, userIdx, productIdx) values(?,?,?,?,?,?)";
        Object[] createReviewParams = new Object[]{
                postReviewReq.getTitle(),
                postReviewReq.getDescription(),
                postReviewReq.getStarPoint(),
                postReviewReq.getRepImage(),
                postReviewReq.getUserIdx(),
                postReviewReq.getProductIdx(),
        };
        this.jdbcTemplate.update(createReviewQuery, createReviewParams);

        if (postReviewReq.getReviewImageUrl() != null) {
            for (String s : postReviewReq.getReviewImageUrl()) {
                String createReviewImageQuery = "insert into ReviewImage(reviewImageUrl, reviewIdx) values(?,?)";
                Object[] createReviewImageParams = new Object[]{
                        s,
                        postReviewReq.getReviewIdx()
                };
                this.jdbcTemplate.update(createReviewImageQuery, createReviewImageParams);
            }
        }

        String createReviewKeywordQuery = "insert into ReviewKeyword(satisfaction, quality, delivery, reviewIdx) values(?,?,?,?)";
        Object[] createReviewKeywordParams = new Object[] {
                postReviewReq.getSatisfaction(),
                postReviewReq.getQuality(),
                postReviewReq.getDelivery(),
                postReviewReq.getReviewIdx()
        };
        this.jdbcTemplate.update(createReviewKeywordQuery, createReviewKeywordParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public List<GetReviewRes> getReviews(int productIdx) {

        String getReviewsQuery =
                    "select R.reviewIdx, U.userName, U.profileImage, P.productName, R.title, R.description, R.updatedAt, R.starPoint, \n" +
                            "R.repImage, RK.satisfaction, RK.delivery, RK.quality\n" +
                            "from Review R\n" +
                            "    left join User U            on U.userIdx = R.userIdx\n" +
                            "    left join Product P         on P.productIdx = R.productIdx\n" +
                            "    left join ReviewKeyword RK  on RK.reviewIdx = R.reviewIdx\n" +
                            "where R.productIdx = ?";

            Object[] params = new Object[] {productIdx};
            return this.jdbcTemplate.query(getReviewsQuery,
                    (rs, rowNum) -> new GetReviewRes(
                            rs.getInt("reviewIdx"),
                            rs.getString("userName"),
                            rs.getString("profileImage"),
                            rs.getString("productName"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("updatedAt"),
                            rs.getInt("starPoint"),
                            rs.getString("repImage"),
                            rs.getInt("satisfaction"),
                            rs.getInt("delivery"),
                            rs.getInt("quality")),
                    params);
    }

    public List<String> getReviewImages(int reviewIdx) {
        String getReviewImageQuery = "select reviewImageUrl from ReviewImage where reviewIdx =? and status = 'Y'";
        int getReviewImagesParams = reviewIdx;

        return this.jdbcTemplate.query(getReviewImageQuery,
                (rs, rowNum) -> new String(rs.getString("reviewImageUrl")), getReviewImagesParams);
    }

}

