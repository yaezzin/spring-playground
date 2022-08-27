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

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select userIdx, status, createdAt, nickname, phoneNumber, profileImage from User";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("status"),
                        rs.getString("createdAt"),
                        rs.getString("nickname"),
                        rs.getString("phoneNumber"),
                        rs.getString("profileImage"))
                );
    }

    public List<GetUserRes> getUsersByNickname(String nickname){
        String getUsersByNicknameQuery = "select userIdx, status, createdAt, nickname, phoneNumber, profileImage from User where nickname =?";
        String getUsersByNicknameParams = nickname;
        return this.jdbcTemplate.query(getUsersByNicknameQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("status"),
                        rs.getString("createdAt"),
                        rs.getString("nickname"),
                        rs.getString("phoneNumber"),
                        rs.getString("profileImage")),
                getUsersByNicknameParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from User where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("status"),
                        rs.getString("createdAt"),
                        rs.getString("nickname"),
                        rs.getString("phoneNumber"),
                        rs.getString("profileImage")),
                getUserParams);
    }

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (nickname, phoneNumber, password, profileImage) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getNickname(), postUserReq.getPhoneNumber(), postUserReq.getPassword(), postUserReq.getProfileImage()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checkPhoneNumber(String phoneNumber){
        String checkPhoneNumberQuery = "select exists(select phoneNumber from User where phoneNumber = ?)";
        String checkPhoneNumberParams = phoneNumber;
        return this.jdbcTemplate.queryForObject(checkPhoneNumberQuery,
                int.class,
                checkPhoneNumberParams);
    }

    public int modifyUser(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set nickname = ?, profileImage = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickname(), patchUserReq.getProfileImage(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int deleteUser(DeleteUserReq deleteUserReq) {
        String deleteUserNameQuery = "update User set status = 'Deleted', deleteReason = ? where userIdx = ?";
        Object[] deleteUserNameParams = new Object[]{deleteUserReq.getDeleteReason(), deleteUserReq.getUserIdx()};
        return this.jdbcTemplate.update(deleteUserNameQuery, deleteUserNameParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, password, phoneNumber, nickname, profileImage from User where phoneNumber = ?";
        String getPwdParams = postLoginReq.getPhoneNumber();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("phoneNumber"),
                        rs.getString("password"),
                        rs.getString("nickname"),
                        rs.getString("profileImage")
                ),
                getPwdParams
                );
    }

    public int createUserBadge(PostBadgeReq postBadgeReq) {
        String createUserBadgeQuery = "insert into Badge(badgeCategoryIdx, userIdx) values(?,?)";
        Object[] createUserBadgeParam = new Object[]{postBadgeReq.getCategoryIdx(), postBadgeReq.getUserIdx()};
        this.jdbcTemplate.update(createUserBadgeQuery, createUserBadgeParam);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
}
