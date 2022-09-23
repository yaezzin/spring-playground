package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (email, password, userName, phoneNumber) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getEmail(), postUserReq.getPassword(), postUserReq.getUserName(), postUserReq.getPhoneNumber()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }
    public int checkPhoneNumber(String phoneNumber){
        String checkPhoneNumberQuery = "select exists(select phoneNumber from User where phoneNumber = ?)";
        String checkPhoneNumberParams = phoneNumber;
        return this.jdbcTemplate.queryForObject(checkPhoneNumberQuery,
                int.class,
                checkPhoneNumberParams);

    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, email, password, userName, phoneNumber from User where email = ?";
        String getPwdParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("userName"),
                        rs.getString("phoneNumber")
                ),
                getPwdParams
                );
    }

    public GetUserRes getUser(int userIdx) {
        String getUserQuery = "select userIdx, email, password, userName, phoneNumber, profileImage, status, createdAt from User where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("userName"),
                        rs.getString("phoneNumber"),
                        rs.getString("profileImage"),
                        rs.getString("status"),
                        rs.getString("createdAt")
                ),
                getUserParams);
    }

    public int modifyUserProfile(PatchUserReq patchUserReq) {
        String modifyUserProfileQuery = "update User set profileImage = ? where userIdx = ?";
        Object[] modifyUserProfileParams = new Object[]{patchUserReq.getProfileImage(), patchUserReq.getUserIdx()};
        return this.jdbcTemplate.update(modifyUserProfileQuery, modifyUserProfileParams);
    }

    public int checkUser(int userIdx) {
        String Query = "SELECT EXISTS(SELECT * FROM User WHERE status = 'Y' AND userIdx = ?);";
        int Param = userIdx;
        return this.jdbcTemplate.queryForObject(Query, int.class, Param);
    }

    public int checkUserEmail(String email) {
        String Query = "select exists(select * from User where status = 'Y' and email =?)";
        Object[] Param = new Object[]{email};
        return this.jdbcTemplate.queryForObject(Query, int.class, Param);
    }

    public List<GetUserProdWishRes> getUserProductWish(int userIdx) {
        String GetUserProdWishQuery =
                "select P.productIdx, P.productName, PIF.price, P.discount, \n" +
                "    concat(PIF.quantity, '개') as quantity,\n" +
                "    concat(PIF.kg, 'kg') as kg,\n" +
                "    concat(PIF.liter, 'L') as liter,\n" +
                "    (select PI.prodRepImageUrl from ProductImage PI where PI.productIdx = P.productIdx limit 1) as prodRepImageUrl\n" +
                "from Product P\n" +
                "    left join ProductInfo PIF  on P.productIdx = PIF.productIdx\n" +
                "    left join Wish W on W.productIdx = P.productIdx\n" +
                "    left join User U on U.userIdx = W.userIdx\n" +
                "where U.userIdx = ? and W.status = 'Y' order by W.createdAt desc";
        Object[] GetUserProdWishParam = new Object[] {userIdx};
        return this.jdbcTemplate.query(GetUserProdWishQuery,
                (rs, rowNum) -> new GetUserProdWishRes(
                        rs.getInt("productIdx"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getInt("discount"),
                        rs.getString("prodRepImageUrl"),
                        rs.getString("quantity"),
                        rs.getString("kg"),
                        rs.getString("liter"))
                , GetUserProdWishParam);
    }

    public List<GetUserCartRes> getUserCart(int userIdx, int deliveryType) {
        String getUserCartQuery =
                "select P.productIdx, P.productName, \n" +
                "concat(PIF.kg, 'kg') as kg,\n" +
                "concat(PIF.quantity, '개') as quantity,\n" +
                "concat(PIF.liter, 'L') as liter,\n" +
                "PIF.price, P.discount, P.deliveryType, \n" +
                "  (select PI.prodRepImageUrl from ProductImage PI where PI.productIdx = P.productIdx limit 1) as prodRepImageUrl\n" +
                "from Cart C\n" +
                "    left join Product P       on P.productIdx = C.productIdx\n" +
                "    left join ProductInfo PIF on P.productIdx = PIF.productIdx\n" +
                "    left join User U          on U.userIdx = C.userIdx\n" +
                "where U.userIdx = ? and P.deliveryType = ? and C.status = 'Y' order by C.createdAt desc";

        Object[] getUserCartParam = new Object[]{userIdx, deliveryType};
        return this.jdbcTemplate.query(getUserCartQuery,
                (rs, rowNum) -> new GetUserCartRes(
                        rs.getInt("productIdx"),
                        rs.getString("productName"),
                        rs.getString("kg"),
                        rs.getString("quantity"),
                        rs.getString("liter"),
                        rs.getInt("price"),
                        rs.getInt("discount"),
                        rs.getInt("deliveryType"),
                        rs.getString("prodRepImageUrl")
                ), getUserCartParam);
    }

    public int checkPassword(String email, String encryptPwd) {
        String query = "SELECT EXISTS(SELECT * FROM User U WHERE U.email=? AND U.password=? AND U.status='Y');";
        Object[] Params = new Object[]{email, encryptPwd};
        return this.jdbcTemplate.queryForObject(query, int.class, Params);
    }

    public int deleteUser(int userIdx) {
        String query = "update User set status = 'N' where userIdx = ?";
        return this.jdbcTemplate.update(query, userIdx);
    }

    public int createAddress(PostUserAddressReq postUserAddressReq) {
        String query1 = "insert into UserAddress(recipient, phoneNumber, isDefaultAddress, userIdx) values(?,?,?,?);";
        Object[] params1 = new Object[] {
                postUserAddressReq.getRecipient(),
                postUserAddressReq.getPhoneNumber(),
                postUserAddressReq.getIsDefaultAddress(),
                postUserAddressReq.getUserIdx(),
        };
        this.jdbcTemplate.update(query1, params1);

        String query2 = "insert into Address(address, addressDetail, deliveryRequest, zipCode, doorCode, userAddressIdx) values(?,?,?,?,?,?)";
        Object[] param2 = new Object[] {
                postUserAddressReq.getAddress(),
                postUserAddressReq.getAddressDetail(),
                postUserAddressReq.getDeliveryRequest(),
                postUserAddressReq.getZipCode(),
                postUserAddressReq.getDoorCode(),
                postUserAddressReq.getUserAddressIdx()
        };
        this.jdbcTemplate.update(query2, param2);

        String lastProductIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastProductIdxQuery, int.class);
    }
}
