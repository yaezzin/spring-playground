package com.example.demo.src.product;

import com.example.demo.src.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetProdRes> getProducts() {
        String getProdsQuery =
                "select productIdx,\n" +
                "      case when timestampdiff(second , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(second, P.updatedAt, current_timestamp),' 초 전')\n" +
                "           when timestampdiff(minute , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(minute, P.updatedAt, current_timestamp),' 분 전')\n" +
                "           when timestampdiff(hour , P.updatedAt, current_timestamp) <24\n" +
                "           then concat(timestampdiff(hour, P.updatedAt, current_timestamp),' 시간 전')\n" +
                "       end as createdAt,\n" +
                "       title,\n" +
                "       price,\n" +
                "      case when timestampdiff(second , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(second, P.updatedAt, current_timestamp),' 초 전')\n" +
                "           when timestampdiff(minute , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(minute, P.updatedAt, current_timestamp),' 분 전')\n" +
                "           when timestampdiff(hour , P.updatedAt, current_timestamp) <24\n" +
                "           then concat(timestampdiff(hour, P.updatedAt, current_timestamp),' 시간 전')\n" +
                "       end as pulledAt,\n" +
                "       repImage,\n" +
                "       regionGu,\n" +
                "       regionTown,\n" +
                "(select count(*) from Wish W where W.productIdx = P.productIdx) as wishCount\n" +
                "from Product P\n" +
                "left join User U        on P.sellerIdx = U.userIdx\n" +
                "left join UserRegion UG on P.sellerIdx = UG.userIdx\n" +
                "left join Region R      on R.regionIdx = UG.regionIdx\n" +
                "Where U.status = 'Y' and P.status = 'Y'\n";
                //"order by date(P.createdAt) asc;";

        return this.jdbcTemplate.query(getProdsQuery,
                (rs, rowNum) -> new GetProdRes(
                        rs.getInt("productIdx"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("pulledAt"),
                        rs.getString("repImage"),
                        rs.getString("regionGu"),
                        rs.getString("regionTown"),
                        rs.getInt("wishCount")
                )
        );
    }

    public List<GetProdRes> getProductsByTitle(String title) {
        String getProdsQuery =
                "select productIdx,\n" +
                "      case when timestampdiff(second , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(second, P.updatedAt, current_timestamp),' 초 전')\n" +
                "           when timestampdiff(minute , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(minute, P.updatedAt, current_timestamp),' 분 전')\n" +
                "           when timestampdiff(hour , P.updatedAt, current_timestamp) <24\n" +
                "           then concat(timestampdiff(hour, P.updatedAt, current_timestamp),' 시간 전')\n" +
                "       end as createdAt,\n" +
                "       title,\n" +
                "       price,\n" +
                "      case when timestampdiff(second , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(second, P.updatedAt, current_timestamp),' 초 전')\n" +
                "           when timestampdiff(minute , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(minute, P.updatedAt, current_timestamp),' 분 전')\n" +
                "           when timestampdiff(hour , P.updatedAt, current_timestamp) <24\n" +
                "           then concat(timestampdiff(hour, P.updatedAt, current_timestamp),' 시간 전')\n" +
                "       end as pulledAt,\n" +
                "       repImage,\n" +
                "       regionGu,\n" +
                "       regionTown,\n" +
                "       (select count(*) from Wish W where W.productIdx = P.productIdx) as wishCount\n" +
                "from Product P\n" +
                "left join User U        on P.sellerIdx = U.userIdx\n" +
                "left join UserRegion UG on P.sellerIdx = UG.userIdx\n" +
                "left join Region R      on R.regionIdx = UG.regionIdx\n" +
                "Where U.status = 'Y' and P.status = 'Y' and title = ?\n";
                //"order by date(P.createdAt) asc;\n";

        String getProductsByTitleParams = title;
        return this.jdbcTemplate.query(getProdsQuery,
                (rs, rowNum) -> new GetProdRes(
                        rs.getInt("productIdx"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("pulledAt"),
                        rs.getString("repImage"),
                        rs.getString("regionGu"),
                        rs.getString("regionTown"),
                        rs.getInt("wishCount")),
                getProductsByTitleParams);
    }

    public List<GetProdRes> getProductsByCategory(int categoryIdx) {
        String getProdsByCategoryQuery =
                "select productIdx,\n" +
                "      case when timestampdiff(second , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(second, P.updatedAt, current_timestamp),' 초 전')\n" +
                "           when timestampdiff(minute , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(minute, P.updatedAt, current_timestamp),' 분 전')\n" +
                "           when timestampdiff(hour , P.updatedAt, current_timestamp) <24\n" +
                "           then concat(timestampdiff(hour, P.updatedAt, current_timestamp),' 시간 전')\n" +
                "       end as createdAt,\n" +
                "       title,\n" +
                "       price,\n" +
                "       case when timestampdiff(second , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(second, P.updatedAt, current_timestamp),' 초 전')\n" +
                "           when timestampdiff(minute , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(minute, P.updatedAt, current_timestamp),' 분 전')\n" +
                "           when timestampdiff(hour , P.updatedAt, current_timestamp) <24\n" +
                "           then concat(timestampdiff(hour, P.updatedAt, current_timestamp),' 시간 전')\n" +
                "       end as pulledAt,\n" +
                "       repImage,\n" +
                "       regionGu,\n" +
                "       regionTown,\n" +
                "       (select count(*) from Wish W where W.productIdx = P.productIdx) as wishCount\n" +
                "from Product P\n" +
                "    left join User U          on P.sellerIdx = U.userIdx \n" +
                "    left join UserRegion UG   on P.sellerIdx = UG.userIdx \n" +
                "    left join Region R        on R.regionIdx = UG.regionIdx\n" +
                "Where U.status = 'Y' and P.status = 'Y' and categoryIdx = ?";

        int getProductsByCategoryParams = categoryIdx;
        return this.jdbcTemplate.query(getProdsByCategoryQuery,
                (rs, rowNum) -> new GetProdRes(
                        rs.getInt("productIdx"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("pulledAt"),
                        rs.getString("repImage"),
                        rs.getString("regionGu"),
                        rs.getString("regionTown"),
                        rs.getInt("wishCount")),
                getProductsByCategoryParams);
    }

    public List<GetProdDetailRes> getProduct(int productIdx) { //왜 여기서 리스트를 반환해야할까??
        String getProdsDetailQuery =
                "select productIdx,\n" +
                "     case when timestampdiff(second , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(second, P.updatedAt, current_timestamp),' 초 전')\n" +
                "           when timestampdiff(minute , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(minute, P.updatedAt, current_timestamp),' 분 전')\n" +
                "           when timestampdiff(hour , P.updatedAt, current_timestamp) <24\n" +
                "           then concat(timestampdiff(hour, P.updatedAt, current_timestamp),' 시간 전')\n" +
                "     end as createdAt,\n" +
                "     title,\n" +
                "     description,\n" +
                "     case when timestampdiff(second , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(second, P.updatedAt, current_timestamp),' 초 전')\n" +
                "           when timestampdiff(minute , P.updatedAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(minute, P.updatedAt, current_timestamp),' 분 전')\n" +
                "           when timestampdiff(hour , P.updatedAt, current_timestamp) <24\n" +
                "           then concat(timestampdiff(hour, P.updatedAt, current_timestamp),' 시간 전')\n" +
                "     end as pulledAt,\n" +
                "     price,\n" +
                "     (select count(*) from Wish W where W.productIdx = P.productIdx) as wishCount,\n" +
                "     viewCount, canProposal, sellerIdx, profileImage, nickname, regionGu, regionTown, mannerTemp\n" +
                "from Product P\n" +
                "    left join User U         on P.sellerIdx = U.userIdx \n" +
                "    left join UserRegion UG  on P.sellerIdx = UG.userIdx \n" +
                "    left join Region R       on R.regionIdx = UG.regionIdx\n" +
                "Where U.status = 'Y' and P.status = 'Y' and productIdx = ?";
                //"order by date(P.createdAt) asc;";

        int getProductsDetailParams = productIdx;
        return this.jdbcTemplate.query(getProdsDetailQuery,
                (rs, rowNum) -> new GetProdDetailRes(
                        rs.getInt("productIdx"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("pulledAt"),
                        rs.getInt("price"),
                        rs.getInt("wishCount"),
                        rs.getInt("viewCount"),
                        rs.getString("canProposal"),
                        rs.getInt("sellerIdx"),
                        rs.getString("profileImage"),
                        rs.getString("nickname"),
                        rs.getString("regionGu"),
                        rs.getString("regionTown"),
                        rs.getInt("mannerTemp")
                ),
                getProductsDetailParams);
    }

    public PostProdRes createProduct(PostProdReq postProdReq) {
        String createProductQuery = "insert into Product(title, description, canProposal, price, sellerIdx, categoryIdx, repImage) values(?,?,?,?,?,?,?)";
        Object[] createProductParams = new Object[] {
                postProdReq.getTitle(),
                postProdReq.getDescription(),
                postProdReq.getCanProposal(),
                postProdReq.getPrice(),
                postProdReq.getSellerIdx(),
                postProdReq.getCategoryIdx(),
                postProdReq.getRepImage()
        };

        this.jdbcTemplate.update(createProductQuery, createProductParams);
        String lastProductIdxQuery = "select last_insert_id()";
        int lastProductIdx = this.jdbcTemplate.queryForObject(lastProductIdxQuery, int.class);

        if (postProdReq.getProductImage() != null) {
            for (String s : postProdReq.getProductImage()) {
                String createImageProductQuery = "insert into ProductImage(productImage, productIdx) VALUES (?,?)";
                Object[] createProductImageParams = new Object[]{s, lastProductIdx};
                this.jdbcTemplate.update(createImageProductQuery, createProductImageParams);
            }
        }
        return new PostProdRes("상품 등록을 성공하였습니다.");
    }

    public int createWish(PostWishReq postWishReq) {
        String createWishQuery = "insert into Wish(productIdx, userIdx) values(?,?)";
        Object[] createWishParams = new Object[] {postWishReq.getProductIdx(), postWishReq.getUserIdx()};
        return this.jdbcTemplate.update(createWishQuery, createWishParams);
    }

    public int deleteWish(PostWishReq postWishReq) {
        String deleteWishQuery = "delete from Wish where productIdx =? and userIdx =?";
        Object[] deleteWishParams = new Object[] {postWishReq.getProductIdx(), postWishReq.getUserIdx()};
        return this.jdbcTemplate.update(deleteWishQuery, deleteWishParams);
    }

    public int modifyProductInfo(PatchProdReq patchProdReq) {
        String modifyProductInfoQuery = "update Product set title =?, description =?, price =?, canProposal=?, categoryIdx =? where productIdx = ? ";
        Object[] modifyProductInfoParams = new Object[]{
                patchProdReq.getTitle(),
                patchProdReq.getDescription(),
                patchProdReq.getPrice(),
                patchProdReq.getCanProposal(),
                patchProdReq.getCategoryIdx(),
                patchProdReq.getProductIdx()
        };
        return this.jdbcTemplate.update(modifyProductInfoQuery, modifyProductInfoParams);
    }

    public int updateViewCount(int productIdx) {
        String updateViewCountQuery = "update Product set viewCount = viewCount + 1 where productIdx = ?";
        int updateViewCountParams = productIdx;
        return this.jdbcTemplate.update(updateViewCountQuery, updateViewCountParams);
    }

    public int deleteProduct(int productIdx) {
        int deleteProductParams = productIdx;
        int deleteProductQuery1 = this.jdbcTemplate.update("delete from ProductImage where productIdx = ?", deleteProductParams);
        int deleteProductQuery2 = this.jdbcTemplate.update("delete from Product where productIdx = ?", deleteProductParams);
        return deleteProductQuery1 & deleteProductQuery2;
    }

    public int updatePulledAt(int productIdx) {
        String updatePulledAtQuery = "update Product set pulledAt = CURRENT_TIMESTAMP where productIdx = ?";
        int updatePulledAtParam = productIdx;
        return this.jdbcTemplate.update(updatePulledAtQuery, updatePulledAtParam);
    }

    public int updateStatus(PatchStatusReq patchStatusReq) {
        String updateStatusQuery = "update Product set status = ? where productIdx = ?";
        Object[] updateStatusParams = new Object[] {
                patchStatusReq.getStatus(),
                patchStatusReq.getProductIdx()
        };
        return this.jdbcTemplate.update(updateStatusQuery, updateStatusParams);
    }
}
