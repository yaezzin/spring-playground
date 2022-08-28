package com.example.demo.src.board;

import com.example.demo.src.board.model.PostBoardReq;
import com.example.demo.src.board.model.PostBoardRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class BoardDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createBoard(PostBoardReq postBoardReq) {
        String createBoardQuery = "insert into Board(userIdx, categoryIdx, content) values(?,?,?)";
        Object[] createBoardParams = new Object[]{
                postBoardReq.getUserIdx(),
                postBoardReq.getCategoryIdx(),
                postBoardReq.getContent()
        };
        this.jdbcTemplate.update(createBoardQuery, createBoardParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }
}
