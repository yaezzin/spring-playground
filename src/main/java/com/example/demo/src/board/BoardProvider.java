package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.src.board.model.GetBoardDetailRes;
import com.example.demo.src.board.model.GetBoardRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class BoardProvider {

    @Autowired private final BoardDao boardDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public BoardProvider(BoardDao boardDao) {
        this.boardDao = boardDao;
    }

    @Transactional
    public List<GetBoardRes> getBoards() throws BaseException {
        try {
            List<GetBoardRes> getBoardRes = boardDao.getBoards();
            return getBoardRes;
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<GetBoardRes> getBoardsByKeyword(String search) throws BaseException {
        //try {
            List<GetBoardRes> getBoardRes = boardDao.getBoardsByKeyword(search);
            return getBoardRes;
        //} catch(Exception exception) {
        //    throw new BaseException(DATABASE_ERROR);
        //}
    }

    @Transactional
    public List<GetBoardDetailRes> getBoard(int boardIdx) throws BaseException {
        //try {
            List<GetBoardDetailRes> getBoardDetailRes = boardDao.getBoard(boardIdx);
            return getBoardDetailRes;
        //} catch(Exception exception) {
        //    throw new BaseException(DATABASE_ERROR);
        //}
    }
}
