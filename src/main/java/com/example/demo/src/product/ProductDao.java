package com.example.demo.src.product;

import com.example.demo.src.product.model.GetProdDetailRes;
import com.example.demo.src.product.model.GetProdRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
        String getProdsQuery = "select productIdx, title, price, repImage, regionGu, regionTown, count(W.wishIdx)," +
                "case\n" +
                "when timestampdiff(MINUTE, Product.pulledAt, CURRENT_TIMESTAMP()) < 60\n" +
                "    then concat('끌올시간 ',timestampdiff(MINUTE, Product.pulledAt, CURRENT_TIMESTAMP()), '분 전')\n" +
                "when timestampdiff(HOUR, Product.pulledAt, CURRENT_TIMESTAMP()) < 24\n" +
                "    then concat('끌올시간 ',timestampdiff(HOUR, Product.pulledAt, CURRENT_TIMESTAMP()), '시간 전')\n" +
                "when timestampdiff(DAY, Product.pulledAt, CURRENT_TIMESTAMP()) < 30\n" +
                "    then concat('끌올시간 ',timestampdiff(DAY, Product.pulledAt, CURRENT_TIMESTAMP()), '일 전')\n" +
                "else date_format(Product.pulledAt, '%Y년-%m월-%d일')\n" +
                "end as pulledAt,\n" +
                "case\n" +
                "when timestampdiff(MINUTE, Product.createdAt, CURRENT_TIMESTAMP()) < 60\n" +
                "    then concat(timestampdiff(MINUTE, Product.createdAt, CURRENT_TIMESTAMP()), '분 전')\n" +
                "when timestampdiff(HOUR, Product.createdAt, CURRENT_TIMESTAMP()) < 24\n" +
                "    then concat(timestampdiff(HOUR, Product.createdAt, CURRENT_TIMESTAMP()), '시간 전')\n" +
                "when timestampdiff(DAY, Product.createdAt, CURRENT_TIMESTAMP()) < 30\n" +
                "    then concat(timestampdiff(DAY, Product.createdAt, CURRENT_TIMESTAMP()), '일 전')\n" +
                "else date_format(Product.createdAt, '%Y년-%m월-%d일')\n" +
                "end as createdAt,\n" +
                "from Product P\n" +
                "LEFT JOIN User U   on U.userIdx = P.sellerIdx\n" +
                "LEFT JOIN Region R on R.regionIdx = P.regionIdx\n" +
                "LEFT JOIN Wish W   on W.productIdx = P.productIdx\n" +
                "WHERE U.status = 'Y' and P.status = 'Y'\n" +
                "ORDER BY DATE(P.createdAt) DESC";

        return this.jdbcTemplate.query(getProdsQuery,
                (rs, rowNum) -> new GetProdRes(
                        rs.getInt("productIdx"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("pulledAt"),
                        rs.getString("repImage"),
                        rs.getString("regionNameGu"),
                        rs.getString("regionNameTown"),
                        rs.getInt("count(W.wishIdx)")
                )
        );
    }

    public List<GetProdRes> getProductsByTitle(String title) {
        String getProdsQuery = "select productIdx, title, price, repImage, regionGu, regionTown, count(W.wishIdx), " +
                "case" +
                "when timestampdiff(MINUTE, Product.pulledAt, CURRENT_TIMESTAMP()) < 60\n" +
                "    then concat('끌올시간 ',timestampdiff(MINUTE, Product.pulledAt, CURRENT_TIMESTAMP()), '분 전')\n" +
                "when timestampdiff(HOUR, Product.pulledAt, CURRENT_TIMESTAMP()) < 24\n" +
                "    then concat('끌올시간 ',timestampdiff(HOUR, Product.pulledAt, CURRENT_TIMESTAMP()), '시간 전')\n" +
                "when timestampdiff(DAY, Product.pulledAt, CURRENT_TIMESTAMP()) < 30\n" +
                "    then concat('끌올시간 ',timestampdiff(DAY, Product.pulledAt, CURRENT_TIMESTAMP()), '일 전')\n" +
                "else date_format(Product.pulledAt, '%Y년-%m월-%d일')\n" +
                "end as pulledAt," +
                "case" +
                "when timestampdiff(MINUTE, Product.createdAt, CURRENT_TIMESTAMP()) < 60\n" +
                "    then concat(timestampdiff(MINUTE, Product.createdAt, CURRENT_TIMESTAMP()), '분 전')\n" +
                "when timestampdiff(HOUR, Product.createdAt, CURRENT_TIMESTAMP()) < 24\n" +
                "    then concat(timestampdiff(HOUR, Product.createdAt, CURRENT_TIMESTAMP()), '시간 전')\n" +
                "when timestampdiff(DAY, Product.createdAt, CURRENT_TIMESTAMP()) < 30\n" +
                "    then concat(timestampdiff(DAY, Product.createdAt, CURRENT_TIMESTAMP()), '일 전')\n" +
                "else date_format(Product.createdAt, '%Y년-%m월-%d일')\n" +
                "end as createdAt,\n" +
                "from Product P\n" +
                "LEFT JOIN User U   on U.userIdx = P.sellerIdx\n" +
                "LEFT JOIN Region R on R.regionIdx = P.regionIdx\n" +
                "LEFT JOIN Wish W   on W.productIdx = P.productIdx\n" +
                "WHERE (U.status = 'Y' and P.status = 'Y') AND title LIKE concat('%', ?, '%')\n" +
                "ORDER BY date(P.createdAt) DESC";

        String getProductsByTitleParams = title;
        return this.jdbcTemplate.query(getProdsQuery,
                (rs, rowNum) -> new GetProdRes(
                        rs.getInt("productIdx"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("pulledAt"),
                        rs.getString("repImage"),
                        rs.getString("regionNameGu"),
                        rs.getString("regionNameTown"),
                        rs.getInt("count(W.wishIdx)")),
                getProductsByTitleParams);
    }

    public List<GetProdDetailRes> getProduct(int productIdx) { //왜 여기서 리스트를 반환해야할까??
        String getProdsDetailQuery = "select productIdx, title,description, price, count(W.wishIdx), veiwCount, canProposal \n" +
                "case\n" +
                "when timestampdiff(MINUTE, P.createdAt, CURRENT_TIMESTAMP()) < 60\n" +
                "    then concat(timestampdiff(MINUTE, P.createdAt, CURRENT_TIMESTAMP()), '분 전')\n" +
                "when timestampdiff(HOUR, P.createdAt, CURRENT_TIMESTAMP()) < 24\n" +
                "    then concat(timestampdiff(HOUR, P.createdAt, CURRENT_TIMESTAMP()), '시간 전')\n" +
                "when timestampdiff(DAY, P.createdAt, CURRENT_TIMESTAMP()) < 30\n" +
                "    then concat(timestampdiff(DAY, P.createdAt, CURRENT_TIMESTAMP()), '일 전')\n" +
                "else date_format(P.createdAt, '%Y년-%m월-%d일')\n" +
                "end as createdAt,\n" +
                "case\n" +
                "when timestampdiff(MINUTE, P.pulledAt, CURRENT_TIMESTAMP()) < 60\n" +
                "     then concat('끌올 ', timestampdiff(MINUTE, P.pulledAt, CURRENT_TIMESTAMP()), '분 전')\n" +
                "when timestampdiff(HOUR, P.pulledAt, CURRENT_TIMESTAMP()) < 24\n" +
                "     then concat('끌올 ', timestampdiff(HOUR, P.pulledAt, CURRENT_TIMESTAMP()), '시간 전')\n" +
                "when timestampdiff(DAY, P.pulledAt, CURRENT_TIMESTAMP()) < 30\n" +
                "     then concat('끌올 ', timestampdiff(DAY, P.pulledAt, CURRENT_TIMESTAMP()), '일 전')\n" +
                "else date_format(P.pulledAt, '%Y년-%m월-%d일')\n" +
                "end as pulledAt,\n" +
                "U.userIdx, U.profileImage, regionGu, regionTown, U.mannerTemp\n" +
                "FROM Product P\n" +
                    "LEFT JOIN User U               ON U.userIdx = P.sellerIdx\n" +
                    "LEFT JOIN ProductImage PI      ON PI.productIdx = P.productIdx\n" +
                    "LEFT JOIN Wish W               ON W.productIdx = P.productIdx\n" +
                    "LEFT JOIN ProductCategory PC   ON PC.categoryIdx = P.categoryIdx\n" +
                    "LEFT JOIN Region R             ON R.regionIdx = P.regionIdx" +
                "WHERE (U.status = 'Y' and P.status = 'Y') AND P.prouctIdx = ?";

        int getProductsDetailParams = productIdx;
        return this.jdbcTemplate.query(getProdsDetailQuery,
                (rs, rowNum) -> new GetProdDetailRes(
                        rs.getInt("productIdx"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("pulledAt"),
                        rs.getInt("price"),
                        rs.getInt("count(W,wishIdx)"),
                        rs.getInt("viewCount"),
                        rs.getString("canProposal"),
                        rs.getInt("userIdx"),
                        rs.getString("profileImage"),
                        rs.getString("nickname"),
                        rs.getString("regionGu"),
                        rs.getString("regionTown"),
                        rs.getInt("mannerTemp")
                ),
                getProductsDetailParams);
    }

}
