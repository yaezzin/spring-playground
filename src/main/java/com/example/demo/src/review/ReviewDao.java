package com.example.demo.src.review;

import com.example.demo.src.review.model.*;
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
        String createReviewQuery = "insert into Review(title, description, starPoint, repImage, userIdx, productIdx) values(?,?,?,?,?,?)";
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

    public int checkReviewExist(int reviewIdx) {
        String checkReviewExistQuery = "select exists(select reviewIdx from Review where reviewIdx =? and status = 'Y')";
        return this.jdbcTemplate.queryForObject(checkReviewExistQuery, int.class, reviewIdx);
    }

    public List<GetReviewPreRes> getReviewPreview(int productIdx) {
        String getReviewPreviewQuery =
                "select R.reviewIdx, U.userName, R.title, R.description, R.starPoint, R.repImage, R.createdAt,\n" +
                "  (select count(*) from ReviewHelp RH where R.reviewIdx = RH.reviewIdx and status = 'Y') as reviewHelpCount \n" +
                "from Review R\n" +
                "    left join User U    on U.userIdx = R.userIdx\n" +
                "    left join Product P on P.productIdx = R.productIdx\n" +
                "where P.productIdx = ? order by ReviewHelpCount desc limit 3;"; // 도움이됐어요 순으로 3개만 리턴
        Object[] GetReviewPreviewParam = new Object[] {productIdx};
        return this.jdbcTemplate.query(getReviewPreviewQuery,
                (rs, rowNum) -> new GetReviewPreRes(
                        rs.getInt("reviewIdx"),
                        rs.getString("userName"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("starPoint"),
                        rs.getString("repImage"),
                        rs.getString("createdAt"),
                        rs.getInt("reviewHelpCount"))
                , GetReviewPreviewParam);
    }

    public List<GetUserReviewRes> getUserReview(int userIdx) {
        String getUserReviewQuery =
                "select R.reviewIdx, P.productName, PI.prodRepImageUrl, R.starPoint, R.createdAt,\n" +
                "         R.title, R.description, RK.delivery, RK.quality, RK.satisfaction, \n" +
                "         concat(PIF.quantity, '개') as quantity, \n" +
                "         concat(PIF.kg, 'kg') as weight,\n" +
                "         concat(PIF.liter, 'L') as liter\n" +
                "from Review R\n" +
                "\tleft join Product P        on P.productIdx = R.productIdx\n" +
                "    left join ProductImage PI  on P.productIdx = PI.productIdx\n" +
                "    left join ReviewKeyword RK on R.reviewIdx = RK.reviewIdx\n" +
                "    left join ProductInfo PIF  on P.productIdx = PIF.productIdx \n" +
                "where userIdx = ? order by R.createdAt desc";
        Object[] getUserReviewParam = new Object[] {userIdx};

        return this.jdbcTemplate.query(getUserReviewQuery,
                (rs, rowNum) -> new GetUserReviewRes(
                        rs.getInt("reviewIdx"),
                        rs.getString("productName"),
                        rs.getString("prodRepImageUrl"),
                        rs.getInt("starPoint"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("delivery"),
                        rs.getInt("quality"),
                        rs.getInt("satisfaction"),
                        rs.getString("quantity"),
                        rs.getString("weight"),
                        rs.getString("liter")
                ), getUserReviewParam
        );
    }

    public int deleteReview(int reviewIdx) {
        String updateStatusReviewQuery = "update Review set status = 'N' where reviewIdx =?";
        return this.jdbcTemplate.update(updateStatusReviewQuery, reviewIdx);
    }

    public List<String> getReviewPhotos(int productIdx) {
        String getReviewPhotosQuery =
                "select R.repImage\n" +
                "from Review R\n" +
                "    left join Product P on R.productIdx = P.productIdx\n" +
                "where P.productIdx = ? order by R.createdAt desc";
        Object[] getReviewPhotosParam = new Object[]{productIdx};
        return this.jdbcTemplate.query(getReviewPhotosQuery,
                (rs, rowNum) -> new String(rs.getString("repImage"))
                ,getReviewPhotosParam);
    }

    public List<String> getReviewPrePhotos(int productIdx) {
        String getReviewPrePhotosQuery =
                "select R.repImage\n" +
                "from Review R \n" +
                "    left join Product P on R.productIdx = P.productIdx\n" +
                "where P.productIdx = ? order by R.createdAt desc limit 8";
        Object[] getReviewPrePhotosParam = new Object[]{productIdx};
        return this.jdbcTemplate.query(getReviewPrePhotosQuery,
                (rs, rowNum) -> new String(rs.getString("repImage"))
                ,getReviewPrePhotosParam);
    }

    public int checkProductExist(int productIdx) {
        String Query = "select exists(select * from Product where status = 'Y' and productIdx =?)";
        Object[] Param = new Object[]{productIdx};
        return this.jdbcTemplate.queryForObject(Query, int.class, Param);
    }


}

