package com.example.demo.src.mypage;

import com.example.demo.src.mypage.model.GetMyProdRes;
import com.example.demo.src.mypage.model.GetWishRes;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MyPageDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetWishRes> getUserWish(int userIdx) {
        String getUserWishQuery =
                "select P.title, P.price, P.repImage, R.regionTown, \n" +
                "    (select count(*) from Wish W where W.productIdx = P.productIdx) as wishCount\n" +
                "from Wish W\n" +
                "    left join User U         on U.userIdx = W.userIdx\n" +
                "    left join Product P      on P.productIdx = W.productIdx\n" +
                "    left join UserRegion UG  on P.sellerIdx = UG.userIdx\n" +
                "    left join Region R       on R.regionIdx = UG.regionIdx\n" +
                "where U.userIdx = ?";
        int getUserWishParam = userIdx;

        return this.jdbcTemplate.query(getUserWishQuery,
                (rs,rowNum) -> new GetWishRes(
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("repImage"),
                        rs.getString("regionTown"),
                        rs.getString("wishCount")
                ), getUserWishParam
        );
    }

    public List<GetMyProdRes> getUserProducts(int userIdx) {
        String getUserProductsQuery =
                "select P.productIdx, P.title, P.price, P.repImage, P.createdAt, R.regionTown, \n" +
                "    (select count(*) from ChatRoom C where C.productIdx = P.productIdx) as chatCount\n" +
                "from Product P\n" +
                "    left join User U           on U.userIdx = P.sellerIdx\n" +
                "    left join UserRegion UG    on P.sellerIdx = UG.userIdx\n" +
                "    left join Region R         on R.regionIdx = UG.regionIdx\n" +
                "where U.userIdx = ?";
        int getUserProductsParam = userIdx;

        return this.jdbcTemplate.query(getUserProductsQuery,
                (rs,rowNum) -> new GetMyProdRes(
                        rs.getInt("productIdx"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("repImage"),
                        rs.getString("createdAt"),
                        rs.getString("regionTown"),
                        rs.getInt("chatCount")
                ), getUserProductsParam
        );
    }
}
