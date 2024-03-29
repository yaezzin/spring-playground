package com.example.demo.src.board;

import com.example.demo.src.board.model.*;
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

    public List<GetBoardRes> getBoardsByCategory(int boardCategoryIdx) {
        String getBoardsByKeywordQuery =
                "select boardIdx, B.createdAt, B.content, U.nickname, R.regionTown, BC.categoryName\n" +
                        "from Board B\n" +
                        "    left join User U           on U.userIdx = B.userIdx\n" +
                        "    left join UserRegion UG    on B.userIdx = UG.userIdx \n" +
                        "    left join Region R         on R.regionIdx = UG.regionIdx\n" +
                        "    left join BoardCategory BC on B.categoryIdx = BC.boardCategoryIdx\n" +
                        "where U.status = 'Y' and B.status = 'Y' and boardCategoryIdx = ?\n";

        int getBoardsByKeywordParam = boardCategoryIdx;
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

    public List<GetBoardDetailRes> getBoard(int boardIdx) {
        String getBoardDetailQuery =
                "select B.boardIdx, B.createdAt, B.content, U.nickname, U.profileImage, U.authCount,\n" +
                        "R.regionTown, BC.categoryName, BI.boardImage, C.comment as comment\n" +
                "from Board B\n" +
                "    left join User U            on U.userIdx = B.userIdx\n" +
                "    left join UserRegion UG     on B.userIdx = UG.userIdx \n" +
                "    left join Region R          on R.regionIdx = UG.regionIdx\n" +
                "    left join BoardImage BI     on B.boardIdx = BI.boardIdx\n" +
                "    left join BoardCategory BC  on B.categoryIdx = BC.boardCategoryIdx\n" +
                "    left join Comment C         on C.boardIdx = B.boardIdx\n" +
                "where U.status = 'Y' and B.status = 'Y' and B.boardIdx = ?";


                /*
                "select B.boardIdx,\n" +
                        "B.createdAt, B.content,\n" +
                        "U.nickname, U.profileImage, U.authCount,\n" +
                        "R.regionTown, BC.categoryName, BI.boardImage\n" +
                "from Board B\n" +
                "    left join User U            on U.userIdx = B.userIdx\n" +
                "    left join UserRegion UG     on B.userIdx = UG.userIdx \n" +
                "    left join Region R          on R.regionIdx = UG.regionIdx\n" +
                "    left join BoardImage BI     on B.boardIdx = BI.boardIdx\n" +
                "    left join BoardCategory BC  on B.categoryIdx = BC.boardCategoryIdx\n" +
                "where U.status = 'Y' and B.status = 'Y' and B.boardIdx = ?"; */
        int getBoardDetailParam = boardIdx;

        return this.jdbcTemplate.query(getBoardDetailQuery,
                (rs, rowNum) -> new GetBoardDetailRes(
                        rs.getInt("boardIdx"),
                        rs.getString("createdAt"),
                        rs.getString("content"),
                        rs.getString("nickname"),
                        rs.getString("profileImage"),
                        rs.getInt("authCount"),
                        rs.getString("regionTown"),
                        rs.getString("categoryName"),
                        rs.getString("boardImage"),
                        rs.getString("comment")
            ), getBoardDetailParam
        );
    }

    public int modifyBoard(PatchBoardReq patchBoardReq) {
        String patchBoardQuery = "update Board set categoryIdx = ?, content = ? where boardIdx = ?";
        Object[] patchBoardParams = new Object[]{
                patchBoardReq.getCategoryIdx(),
                patchBoardReq.getContent(),
                patchBoardReq.getBoardIdx()
        };
        return this.jdbcTemplate.update(patchBoardQuery, patchBoardParams);
    }

    public int deleteBoard(int boardIdx) {
        Object[] deleteBoardParams = new Object[]{boardIdx};
        int deleteBoardQuery1 = this.jdbcTemplate.update("delete from BoardImage where boardIdx = ?", deleteBoardParams);
        int deleteBoardQuery2 = this.jdbcTemplate.update("delete from Board where boardIdx = ?", deleteBoardParams);
        return deleteBoardQuery1 & deleteBoardQuery2;
    }

    public List<GetBoardDetailRes> createComment(PostCommentReq postCommentReq, int boardIdx, int userIdx) {
        Object[] createCommentParams = new Object[] {
                postCommentReq.getComment(),
                boardIdx,
                userIdx
        };
        this.jdbcTemplate.update("insert Comment(comment, boardIdx, userIdx) values(?,?,?)", createCommentParams);
        List<GetBoardDetailRes> getBoardResWishComment = getBoard(boardIdx);
        return getBoardResWishComment;
    }

    public int getCommentUserIdx(int commentIdx) {
        return this.jdbcTemplate.queryForObject("select userIdx from Comment where commentIdx = ?", Integer.class, commentIdx);
    }

    public int getCommentBoardIdx(int commentIdx) {
        return this.jdbcTemplate.queryForObject("select boardIdx from Comment where commentIdx = ?",Integer.class, commentIdx);
    }

    public List<GetBoardDetailRes> modifyComment(String comment, int commentIdx, int boardIdx) {
        Object[] modifyCommentParams = new Object[]{commentIdx, comment, boardIdx};
        this.jdbcTemplate.update("update Comment set comment = ? where commentIdx = ? and boardIdx = ?", modifyCommentParams);
        List<GetBoardDetailRes> getBoardComment = getBoard(boardIdx);
        return getBoardComment;
    }

    public void deleteComment(DeleteCommentReq deleteCommentReq) {
        Object[] deleteCommentParams = new Object[]{deleteCommentReq.getCommentIdx()};
        this.jdbcTemplate.update("delete from Comment where commentIdx =?", deleteCommentParams);
    }
}
