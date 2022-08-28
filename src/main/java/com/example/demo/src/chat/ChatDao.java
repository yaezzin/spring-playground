package com.example.demo.src.chat;

import com.example.demo.src.chat.model.GetChatDetailRes;
import com.example.demo.src.chat.model.GetChatRes;
import com.example.demo.src.chat.model.PostChatReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChatDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createChat(PostChatReq postChatReq) {
        String createChatQuery = "insert into ChatMessage(chatRoomIdx, senderIdx, message) values (?,?,?)";
        Object[] createChatParams = new Object[]{
                postChatReq.getChatRoomIdx(),
                postChatReq.getSenderIdx(),
                postChatReq.getMessage()
        };
        this.jdbcTemplate.update(createChatQuery, createChatParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public List<GetChatDetailRes> getChatRoom(int chatRoomIdx) {
        String getChatRoomQuery =
                "select  CR.chatRoomIdx, \n" +
                "        U1.nickname as senderNickname, \n" +
                "        U2.nickname as recieverNickname, \n" +
                "        U2.mannerTemp, U2.profileImage, CM.message, CM.createdAt,\n" +
                "        P.title, P.price, P.canProposal, PI.productImage\n" +
                "from ChatRoom CR\n" +
                "    join User U1         on U1.userIdx = CR.senderIdx\n" +
                "    join User U2         on U2.userIdx = CR.receiverIdx\n" +
                "    join Product P       on P.productIdx = CR.productIdx\n" +
                "    join ProductImage PI on PI.productIdx = CR.productIdx\n" +
                "    join ChatMessage CM  on CM.chatRoomIdx = CR.chatRoomIdx\n" +
                "where CR.chatRoomIdx = ? \n";

        int getChatRoomParam = chatRoomIdx;
        return this.jdbcTemplate.query(getChatRoomQuery,
                (rs, rowNum) -> new GetChatDetailRes(
                        rs.getInt("chatRoomIdx"),
                        rs.getString("senderNickname"),
                        rs.getString("recieverNickname"),
                        rs.getInt("mannerTemp"),
                        rs.getString("profileImage"),
                        rs.getString("message"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("canProposal"),
                        rs.getString("message")),
                getChatRoomParam);
    }

    public List<GetChatRes> getChatRooms() {
        String getChatRoomsQuery =
                "select  CR.chatRoomIdx, \n" +
                "        U.nickname,     \n" +
                "        R.regionTown,   \n" +
                "        U.profileImage, \n" +
                "        CM.message, CM.createdAt,\n" +
                "        P.productIdx, PI.productImage\n" +
                "from ChatRoom CR\n" +
                "    join Product P       on P.productIdx = CR.productIdx\n" +
                "    join ProductImage PI on PI.productIdx = CR.productIdx\n" +
                "    join ChatMessage CM  on CM.chatRoomIdx = CR.chatRoomIdx\n" +
                "    join UserRegion UG   on P.sellerIdx = UG.userIdx\n" +
                "    join Region R        on R.regionIdx = UG.regionIdx\n" +
                "    join User U          on U.userIdx = CR.receiverIdx";

        return this.jdbcTemplate.query(getChatRoomsQuery,
                (rs, rowNum) -> new GetChatRes(
                        rs.getInt("chatRoomIdx"),
                        rs.getString("nickname"),
                        rs.getString("profileImage"),
                        rs.getString("message"),
                        rs.getString("createdAt"),
                        rs.getInt("productIdx"),
                        rs.getString("productImage"),
                        rs.getString("regionTown")
                )
        );
    }
}
