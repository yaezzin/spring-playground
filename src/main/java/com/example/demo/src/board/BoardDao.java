package com.example.demo.src.board;

import com.example.demo.src.board.model.GetBoardRes;
import com.example.demo.src.board.model.PostBoardReq;
import com.example.demo.src.board.model.PostBoardRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

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

    public List<GetBoardRes> getBoards() {
        String getBoardsQuery =
                "select boardIdx, B.createdAt, B.content, U.nickname, R.regionTown, BC.categoryName\n" +
                "from Board B\n" +
                "    left join User U           on U.userIdx = B.userIdx\n" +
                "    left join UserRegion UG    on B.userIdx = UG.userIdx \n" +
                "    left join Region R         on R.regionIdx = UG.regionIdx\n" +
                "    left join BoardCategory BC on B.categoryIdx = BC.boardCategoryIdx\n" +
                "where U.status = 'Y' and B.status = 'Y'\n";
        return this.jdbcTemplate.query(getBoardsQuery,
                (rs, rowNum) -> new GetBoardRes(
                        rs.getInt("boardIdx"),
                        rs.getString("createdAt"),
                        rs.getString("content"),
                        rs.getString("nickname"),
                        rs.getString("regionTown"),
                        rs.getString("categoryName")
                )
        );
    }

    public List<GetBoardRes> getBoardsByKeyword(String search) {

        String getBoardsByKeywordQuery =
                "select boardIdx, B.createdAt, B.content, U.nickname, R.regionTown, BC.categoryName\n" +
                "from Board B\n" +
                "    left join User U           on U.userIdx = B.userIdx\n" +
                "    left join UserRegion UG    on B.userIdx = UG.userIdx \n" +
                "    left join Region R         on R.regionIdx = UG.regionIdx\n" +
                "    left join BoardCategory BC on B.categoryIdx = BC.boardCategoryIdx\n" +
                "where U.status = 'Y' and B.status = 'Y'\n" +
                "having content LIKE concat('%', ?, '%')";


        String getBoardsByKeywordParam = search;
        return this.jdbcTemplate.query(getBoardsByKeywordQuery,
                (rs, rowNum) -> new GetBoardRes(
                        rs.getInt("boardIdx"),
                        rs.getString("createdAt"),
                        rs.getString("content"),
                        rs.getString("nickname"),
                        rs.getString("regionTown"),
                        rs.getString("categoryName")
                ), getBoardsByKeywordParam
        );
    }
}
