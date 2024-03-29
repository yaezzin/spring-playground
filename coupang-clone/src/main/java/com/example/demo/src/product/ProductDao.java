package com.example.demo.src.product;

import com.example.demo.src.product.model.GetProdDetailRes;
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
        String createProductQuery = "insert into Product(productName, discount, deliveryAt, deliveryTip, categoryIdx, sellerIdx) values(?,?,?,?,?,?)";
        Object[] createProductParams = new Object[]{
                postProdReq.getProductName(),
                postProdReq.getDiscount(),
                postProdReq.getDeliveryAt(),
                postProdReq.getDeliveryTip(),
                postProdReq.getCategoryIdx(),
                postProdReq.getSellerIdx()
        };

        this.jdbcTemplate.update(createProductQuery, createProductParams);
        String lastProductIdxQuery = "select last_insert_id()";
        int lastProductIdx = this.jdbcTemplate.queryForObject(lastProductIdxQuery, int.class);

        if (postProdReq.getProdContentImageUrl() != null && postProdReq.getProdRepImageUrl() != null) {
            for (String s1 : postProdReq.getProdRepImageUrl()) {
                for (String s2 : postProdReq.getProdContentImageUrl()) {
                    String createProdImageQuery = "insert into ProductImage(prodRepImageUrl, prodContentImageUrl, productIdx) values(?,?,?)";
                    Object[] createProdImageParams = new Object[]{s1, s2, postProdReq.getProductIdx()};
                    this.jdbcTemplate.update(createProdImageQuery, createProdImageParams);
                }
            }
        }
        return new PostProdRes("상품 등록에 성공하였습니다.");
    }

    public GetProdDetailRes getProductDetail(int productIdx) {
        String query =
                "select P.productIdx, P.productName, P.discount, PIF.price, P.deliveryAt,\n" +
                "    U.userName as sellerName,\n" +
                "    (select floor(avg(R.starPoint)) from Review R where R.productIdx = P.productIdx) as StarPoint,\n" +
                "    (select count(*) from Review R where R.productIdx = P.productIdx) as reviewCount,\n" +
                "    concat(PIF.quantity, '개') as quantity,\n" +
                "    concat(PIF.kg, 'kg') as kg,\n" +
                "    concat(PIF.liter, 'L') as liter\n" +
                "from Product P \n" +
                "    left join ProductInfo PIF on PIF.productIdx = P.productIdx\n" +
                "    left join User U on P.sellerIdx = U.userIdx\n" +
                "where P.productIdx = ? and P.status = 'Y'";
        Object[] getProdDetailParam = new Object[] {productIdx};

        return this.jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> new GetProdDetailRes(
                        rs.getInt("productIdx"),
                        rs.getString("productName"),
                        rs.getInt("discount"),
                        rs.getInt("price"),
                        rs.getString("deliveryAt"),
                        rs.getString("sellerName"),
                        rs.getInt("starPoint"),
                        rs.getInt("reviewCount"),
                        rs.getString("quantity"),
                        rs.getString("kg"),
                        rs.getString("liter")
                ), getProdDetailParam);
    }

    public List<String> getProductRepImages(int productIdx) {
        String getProductRepImageQuery = "select prodRepImageUrl from ProductImage where productIdx = ? and status = 'Y'";
        int getProductRepImageParam = productIdx;
        return this.jdbcTemplate.query(getProductRepImageQuery,
                (rs, rowNum) -> new String(rs.getString("prodRepImageUrl")), getProductRepImageParam);
    }

    public List<String> getProductContentImages(int productIdx) {
        String getProductContentImageQuery = "select prodContentImageUrl from ProductImage where productIdx = ? and status = 'Y'";
        int getProductContentImageParam = productIdx;
        List<String> prodContentImageUrl = this.jdbcTemplate.query(getProductContentImageQuery,
                (rs, rowNum) -> new String(rs.getString("prodContentImageUrl")), getProductContentImageParam);
        return prodContentImageUrl;
    }

    public List<GetProdRes> getProductsByKeyword(String keyword) {
        String getProdByKeywordQuery =
                "select P.productIdx, P.productName, P.deliveryType, P.discount, P.overnightDelivery, PIF.price,\n" +
                "    (select count(*) from Review R where R.productIdx = P.productIdx) as reviewCount,\n" +
                "    (select avg(R.starPoint) from Review R where R.productIdx = P.productIdx) as StarPoint,\n" +
                "    (select PI.prodRepImageUrl from ProductImage PI where PI.productIdx = P.productIdx limit 1) as prodRepImageUrl\n" +
                "from Product P\n" +
                "    left join ProductInfo PIF on PIF.productIdx = P.productIdx\n" +
                "where P.productName like concat('%', ? , '%')";

        String getProdByKeywordParam = keyword;
        return this.jdbcTemplate.query(getProdByKeywordQuery,
                (rs, rowNum) -> new GetProdRes(
                        rs.getInt("productIdx"),
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

    public List<GetProdRes> getProductsByHighPrice(int categoryIdx) {
        String getProdByHighPriceQuery =
                "select P.productIdx, P.productName, P.deliveryType, P.discount, P.overnightDelivery, PIF.price,\n" +
                " (select count(*) from Review R where R.productIdx = P.productIdx) as reviewCount,\n" +
                " (select avg(R.starPoint) from Review R where R.productIdx = P.productIdx) as StarPoint,\n" +
                " (select PI.prodRepImageUrl from ProductImage PI where PI.productIdx = P.productIdx limit 1) as prodRepImageUrl\n" +
                "from Product P \n" +
                "    left join ProductInfo PIF on PIF.productIdx = P.productIdx\n" +
                "where P.categoryIdx = ? order by PIF.price desc";
        int getProdByHighPriceParam = categoryIdx;
        return this.jdbcTemplate.query(getProdByHighPriceQuery,
                (rs, rowNum) -> new GetProdRes(
                        rs.getInt("productIdx"),
                        rs.getString("prodRepImageUrl"),
                        rs.getString("productName"),
                        rs.getString("deliveryType"),
                        rs.getInt("price"),
                        rs.getInt("discount"),
                        rs.getInt("starPoint"),
                        rs.getInt("reviewCount"),
                        rs.getBoolean("overnightDelivery")),
                getProdByHighPriceParam);
    }

    public List<GetProdRes> getProductsByLowPrice(Integer categoryIdx) {
        String getProdByLowPriceQuery =
                "select P.productIdx, P.productName, P.deliveryType, P.discount, P.overnightDelivery, PIF.price,\n" +
                " (select count(*) from Review R where R.productIdx = P.productIdx) as reviewCount,\n" +
                " (select avg(R.starPoint) from Review R where R.productIdx = P.productIdx) as StarPoint,\n" +
                " (select PI.prodRepImageUrl from ProductImage PI where PI.productIdx = P.productIdx limit 1) as prodRepImageUrl\n" +
                "from Product P \n" +
                "    left join ProductInfo PIF on PIF.productIdx = P.productIdx\n" +
                "where P.categoryIdx = ? order by PIF.price asc;";
        int getProdByLowPriceParam = categoryIdx;

        return this.jdbcTemplate.query(getProdByLowPriceQuery,
                (rs, rowNum) -> new GetProdRes(
                        rs.getInt("productIdx"),
                        rs.getString("prodRepImageUrl"),
                        rs.getString("productName"),
                        rs.getString("deliveryType"),
                        rs.getInt("price"),
                        rs.getInt("discount"),
                        rs.getInt("starPoint"),
                        rs.getInt("reviewCount"),
                        rs.getBoolean("overnightDelivery")),
                getProdByLowPriceParam);
    }

    public List<GetProdRes> getProductsByCategory(Integer categoryIdx) {
        String getProdByCategoryQuery =
                "select P.productIdx, PI.prodRepImageUrl, P.productName, P.deliveryType, PI2.price, P.discount, P.overnightDelivery,\n" +
                        "  (select count(*) from Review R where R.productIdx = P.productIdx) as reviewCount,\n" +
                        "  (select floor(avg(R.starPoint)) from Review R where R.productIdx = P.productIdx) as StarPoint\n" +
                        "from Product P\n" +
                        "    left join ProductImage PI  on P.productIdx = PI.productIdx\n" +
                        "    left join ProductInfo  PI2 on P.productIdx = PI2.productIdx\n" +
                        "    left join Category C       on P.categoryIdx = C.categoryIdx\n" +
                        "where P.categoryIdx = ?";
        int getProdByCategoryParam = categoryIdx;
        return this.jdbcTemplate.query(getProdByCategoryQuery,
                (rs, rowNum) -> new GetProdRes(
                        rs.getInt("productIdx"),
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

    public List<GetProdRes> getProductsByKeywordAndHighPrice(String keyword) {
        String getProdByKeywordAndHighPriceQuery =
                "select P.productIdx, P.productName, P.deliveryType, P.discount, P.overnightDelivery, PIF.price,\n" +
                " (select count(*) from Review R where R.productIdx = P.productIdx) as reviewCount,\n" +
                " (select avg(R.starPoint) from Review R where R.productIdx = P.productIdx) as StarPoint,\n" +
                " (select PI.prodRepImageUrl from ProductImage PI where PI.productIdx = P.productIdx limit 1) as prodRepImageUrl\n" +
                "from Product P \n" +
                "    left join ProductInfo PIF on PIF.productIdx = P.productIdx\n" +
                "where P.productName like concat('%', ?, '%') order by PIF.price desc";
        //String getProdByKeywordAndHighPriceParam = keyword;
        Object [] getProdByKeywordAndHighPriceParam = new Object[] {keyword};
        return this.jdbcTemplate.query(getProdByKeywordAndHighPriceQuery,
                (rs, rowNum) -> new GetProdRes(
                        rs.getInt("productIdx"),
                        rs.getString("prodRepImageUrl"),
                        rs.getString("productName"),
                        rs.getString("deliveryType"),
                        rs.getInt("price"),
                        rs.getInt("discount"),
                        rs.getInt("starPoint"),
                        rs.getInt("reviewCount"),
                        rs.getBoolean("overnightDelivery")
                ), getProdByKeywordAndHighPriceParam
        );
    }

    public List<GetProdRes> getProductsByKeywordAndLowPrice(String keyword) {
        String getProdByKeywordAndLowPriceQuery =
                "select P.productIdx, P.productName, P.deliveryType, P.discount, P.overnightDelivery, PIF.price,\n" +
                        " (select count(*) from Review R where R.productIdx = P.productIdx) as reviewCount,\n" +
                        " (select avg(R.starPoint) from Review R where R.productIdx = P.productIdx) as StarPoint,\n" +
                        " (select PI.prodRepImageUrl from ProductImage PI where PI.productIdx = P.productIdx limit 1) as prodRepImageUrl\n" +
                        "from Product P \n" +
                        "    left join ProductInfo PIF on PIF.productIdx = P.productIdx\n" +
                        "where P.productName like concat('%', ?, '%') order by PIF.price asc";

        String getProdByKeywordAndLowPriceParam = keyword;
        return this.jdbcTemplate.query(getProdByKeywordAndLowPriceQuery,
                (rs, rowNum) -> new GetProdRes(
                        rs.getInt("productIdx"),
                        rs.getString("prodRepImageUrl"),
                        rs.getString("productName"),
                        rs.getString("deliveryType"),
                        rs.getInt("price"),
                        rs.getInt("discount"),
                        rs.getInt("starPoint"),
                        rs.getInt("reviewCount"),
                        rs.getBoolean("overnightDelivery")
                ), getProdByKeywordAndLowPriceParam
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

    public int deleteProductWish(int userIdx, int productIdx) {
        String Query = "update Wish set status = 'N' where userIdx =? and productIdx =? and status = 'Y'";
        return this.jdbcTemplate.update(Query, userIdx, productIdx);
    }

    public int checkProductWishExist(int productIdx, int userIdx) {
        String Query = "select exists(select * from Wish where productIdx =? and userIdx =? and status = 'Y')";
        Object[] Param = new Object[]{productIdx, userIdx};
        return this.jdbcTemplate.queryForObject(Query, int.class, Param);
    }


}
