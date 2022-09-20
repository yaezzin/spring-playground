package com.example.demo.src.review;

import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.PatchReviewReq;
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
                            "where R.productIdx = ? and R.status = 'Y' ";

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

    public GetReviewRes getReviewByReviewIdx(int reviewIdx) {
        String getReviewByIdxQuery =
                "select R.reviewIdx, U.userName, U.profileImage, P.productName, R.title, R.description, R.updatedAt, R.starPoint, \n" +
                        "R.repImage, RK.satisfaction, RK.delivery, RK.quality\n" +
                        "from Review R\n" +
                        "    left join User U            on U.userIdx = R.userIdx\n" +
                        "    left join Product P         on P.productIdx = R.productIdx\n" +
                        "    left join ReviewKeyword RK  on RK.reviewIdx = R.reviewIdx\n" +
                        "where R.reviewIdx = ? and R.status = 'Y'";

        Object[] getReviewByIdxParam = new Object[] {reviewIdx};

        return this.jdbcTemplate.queryForObject(getReviewByIdxQuery,
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
                getReviewByIdxParam);
    }

    public List<String> getReviewImages(int reviewIdx) {
        String getReviewImageQuery = "select reviewImageUrl from ReviewImage where reviewIdx =? and status = 'Y'";
        int getReviewImagesParams = reviewIdx;

        return this.jdbcTemplate.query(getReviewImageQuery,
                (rs, rowNum) -> new String(rs.getString("reviewImageUrl")), getReviewImagesParams);
    }

    public List<GetReviewRes> getReviewsByStar(int productIdx, int star) {
        String getReviewsByStarQuery =
                "select R.reviewIdx, U.userName, U.profileImage, P.productName, R.title, R.description, R.updatedAt, R.starPoint, \n" +
                        "R.repImage, RK.satisfaction, RK.delivery, RK.quality\n" +
                        "from Review R\n" +
                        "    left join User U            on U.userIdx = R.userIdx\n" +
                        "    left join Product P         on P.productIdx = R.productIdx\n" +
                        "    left join ReviewKeyword RK  on RK.reviewIdx = R.reviewIdx\n" +
                        "where R.productIdx = ? and R.starPoint = ? and R.status = 'Y'";

        Object[] params = new Object[] {productIdx, star};
        return this.jdbcTemplate.query(getReviewsByStarQuery,
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

    public int modifyReview(PatchReviewReq patchReviewReq) {
        String modifyReviewQuery = "update Review set starPoint =?, title =?, description =?, repImage =? where reviewIdx = ?";
        Object[] modifyReviewParam = new Object[] {
                patchReviewReq.getStarPoint(),
                patchReviewReq.getTitle(),
                patchReviewReq.getDescription(),
                patchReviewReq.getRepImage(),
                patchReviewReq.getReviewIdx()
        };
        int content = this.jdbcTemplate.update(modifyReviewQuery, modifyReviewParam);

        /*
        String modifyReviewImageQuery = "update ReviewImage set reviewImageUrl =? where reviewIdx =?";
        Object[] modifyReviewImageParam = new Object[] {
                patchReviewReq.getReviewImageUrl(),
                patchReviewReq.getReviewIdx()
        };
        int image = this.jdbcTemplate.update(modifyReviewImageQuery, modifyReviewImageParam);
        */

        String modifyReviewKeywordQuery = "update ReviewKeyword set satisfaction =?, delivery =?, quality =? where reviewIdx =?";
        Object[] modifyReviewKeywordParam = new Object[] {
                patchReviewReq.getSatisfaction(),
                patchReviewReq.getDelivery(),
                patchReviewReq.getQuality(),
                patchReviewReq.getReviewIdx()
        };
        int keyword = this.jdbcTemplate.update(modifyReviewKeywordQuery, modifyReviewKeywordParam);

        return content & keyword;
    }

    public int updateStatusReviewImages(int reviewIdx) {
        String query = "update ReviewImage set status = 'N' where reviewIdx =?";
        return this.jdbcTemplate.update(query, reviewIdx);
    }

    public void insertReviewImage(int reviewIdx, String image) {
        String insertImageQuery = "insert into ReviewImage(reviewImageUrl, reviewIdx) values(?,?)";
        this.jdbcTemplate.update(insertImageQuery, image, reviewIdx);
    }

    public String checkAlreadyReviewHelp(int userIdx, int reviewIdx) {
        String CheckIsHelpQuery = "select exists(select isHelp From ReviewHelp WHERE reviewIdx =? AND userIdx =? and status='Y');";
        if (this.jdbcTemplate.queryForObject(CheckIsHelpQuery, int.class, reviewIdx, userIdx)==0){
            return "N";
        }
        else {
            return "Y";
        }
    }

    public int createReviewHelp(int userIdx, int reviewIdx, String isHelp) {
        String createReviewHelpQuery = "insert into ReviewHelp(userIdx, reviewIdx, isHelp, status) values(?,?,?, 'Y')";
        return this.jdbcTemplate.update(createReviewHelpQuery, userIdx, reviewIdx, isHelp);
    }

    public int deleteExistsReviewHelp(int userIdx, int reviewIdx) {
        String deleteExistsReviewHelpQuery = "update ReviewHelp set status = 'N' where userIdx = ? and reviewIdx =? and status = 'Y'";
        return this.jdbcTemplate.update(deleteExistsReviewHelpQuery, userIdx, reviewIdx);
    }
}

