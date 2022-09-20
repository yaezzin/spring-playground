package com.example.demo.src.product;

import com.example.demo.src.product.model.GetProdRes;
import com.example.demo.src.product.model.PostProdReq;
import com.example.demo.src.product.model.PostProdRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public PostProdRes createProduct(PostProdReq postProdReq) {
        String createProductQuery = "insert into Product(productName, discount, deliveryAt, deliveryTip, categoryIdx, productInfoIdx, sellerIdx) values(?,?,?,?,?,?,?)";
        Object[] createProductParams = new Object[]{
                postProdReq.getProductName(),
                postProdReq.getDiscount(),
                postProdReq.getDeliveryAt(),
                postProdReq.getDeliveryTip(),
                postProdReq.getCategoryIdx(),
                postProdReq.getProductInfoIdx(),
                postProdReq.getSellerIdx()
        };

        this.jdbcTemplate.update(createProductQuery, createProductParams);
        String lastProductIdxQuery = "select last_insert_id()";
        int lastProductIdx = this.jdbcTemplate.queryForObject(lastProductIdxQuery, int.class);

        if (postProdReq.getProdContentImageUrl() != null && postProdReq.getProdRepImageUrl() != null) {
            for (String s1 : postProdReq.getProdRepImageUrl()) {
                for (String s2 : postProdReq.getProdContentImageUrl()) {
                    String createProdImageQuery = "insert into ProductImage(prodRepImageUrl, prodContentImageUrl, productIdx) values(?,?, ?)";
                    Object[] createProdImageParams = new Object[]{s1, s2, postProdReq.getProductIdx()};
                    this.jdbcTemplate.update(createProdImageQuery, createProdImageParams);
                }
            }
        }
        return new PostProdRes("상품 등록에 성공하였습니다.");
    }

    public List<GetProdRes> getProductsByKeyword(String keyword) {
        String getProdByKeywordQuery =
                "select P.productName, P.deliveryType, P.discount, P.overnightDelivery, PIF.price,\n" +
                "    (select count(*) from Review R where R.productIdx = P.productIdx) as reviewCount,\n" +
                "    (select avg(R.starPoint) from Review R where R.productIdx = P.productIdx) as StarPoint,\n" +
                "    (select PI.prodRepImageUrl from ProductImage PI where PI.productIdx = P.productIdx limit 1) as prodRepImageUrl\n" +
                "from Product P\n" +
                "    left join ProductInfo PIF on PIF.productIdx = P.productIdx\n" +
                "where P.productName like concat('%', ?, '%')";

        String getProdByKeywordParam = keyword;
        return this.jdbcTemplate.query(getProdByKeywordQuery,
                (rs, rowNum) -> new GetProdRes(
                        rs.getString("prodRepImageUrl"),
                        rs.getString("productName"),
                        rs.getString("deliveryType"),
                        rs.getInt("price"),
                        rs.getInt("discount"),
                        rs.getInt("starPoint"),
                        rs.getInt("reviewCount"),
                        rs.getBoolean("overnightDelivery")
                ), getProdByKeywordParam
        );

    }

    public List<GetProdRes> getProductsByCategory(int categoryIdx) {
        String getProdByCategoryQuery =
                "select PI.prodRepImageUrl, P.productName, P.deliveryType, PI2.price, P.discount, P.overnightDelivery,\n" +
                        "  (select count(*) from Review R where R.productIdx = P.productIdx) as reviewCount,\n" +
                        "  (select floor(avg(R.starPoint)) from Review R where R.productIdx = P.productIdx) as StarPoint\n" +
                        "from Product P\n" +
                        "    left join ProductImage PI  on P.productIdx = PI.productIdx\n" +
                        "    left join ProductInfo  PI2 on P.productInfoIdx = PI2.productInfoIdx\n" +
                        "    left join Category C       on P.categoryIdx = C.categoryIdx\n" +
                        "where P.categoryIdx = ?";
        int getProdByCategoryParam = categoryIdx;
        return this.jdbcTemplate.query(getProdByCategoryQuery,
                (rs, rowNum) -> new GetProdRes(
                        rs.getString("prodRepImageUrl"),
                        rs.getString("productName"),
                        rs.getString("deliveryType"),
                        rs.getInt("price"),
                        rs.getInt("discount"),
                        rs.getInt("starPoint"),
                        rs.getInt("reviewCount"),
                        rs.getBoolean("overnightDelivery")
                ), getProdByCategoryParam
        );
    }

    public int checkProductExist(int productIdx) {
        String Query = "select exists(select * from Product where status = 'Y' and productIdx =?)";
        Object[] Param = new Object[]{productIdx};
        return this.jdbcTemplate.queryForObject(Query, int.class, Param);
    }

    public int createProductWish(int userIdx, int productIdx) {
        String createProductWishQuery = "insert into Wish (userIdx, productIdx) values(?,?)";
        return this.jdbcTemplate.update(createProductWishQuery, userIdx, productIdx);
    }

    public int checkProductWishExist(int productIdx, int userIdx) {
        String Query = "select exists(select * from Wish where productIdx =? and userIdx =?)";
        Object[] Param = new Object[]{productIdx, userIdx};
        return this.jdbcTemplate.queryForObject(Query, int.class, Param);
    }
}
