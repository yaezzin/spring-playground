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
        String getUsersQuery = "select * from User";
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
        String getUsersByNicknameQuery = "select * from User where nickname =?";
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

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkPhoneNumber(String phoneNubmer){
        String checkPhoneNumberQuery = "select exists(select phoneNumber from User where phoneNumber = ?)";
        String checkPhoneNumberParams = phoneNubmer;
        return this.jdbcTemplate.queryForObject(checkPhoneNumberQuery,
                int.class,
                checkPhoneNumberParams);
    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update UserInfo set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
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
}
