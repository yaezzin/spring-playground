package com.example.demo.src.chat;

import com.example.demo.src.chat.model.PostChatReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

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
}
