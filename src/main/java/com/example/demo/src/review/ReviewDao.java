package com.example.demo.src.review;

import com.example.demo.src.review.model.PostReviewReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

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
                postReviewReq.getStartPoint(),
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
}
