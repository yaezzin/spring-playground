package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.board.model.PatchBoardReq;
import com.example.demo.src.board.model.PostBoardReq;
import com.example.demo.src.board.model.PostBoardRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BoardService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BoardDao boardDao;
    private final BoardProvider boardProvider;

    @Autowired
    public BoardService(BoardDao boardDao, BoardProvider boardProvider) {
        this.boardDao = boardDao;
        this.boardProvider = boardProvider;
    }

    @Transactional
    public PostBoardRes createBoard(PostBoardReq postBoardReq) throws BaseException {
        try {
            int postBoardRes = boardDao.createBoard(postBoardReq);
            return new PostBoardRes(postBoardRes);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void modifyBoard(PatchBoardReq patchBoardReq) throws BaseException {
        try {
            int patchBoardRes = boardDao.modifyBoard(patchBoardReq);
            if (patchBoardRes == 0) {
                throw new BaseException(MODIFY_FAIL_BOARD);
            }
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void deleteBoard(int boardIdx) throws BaseException {
        try {
            int deleteBoardRes = boardDao.deleteBoard(boardIdx);
            if (deleteBoardRes == 0) {
                throw new BaseException(DELETE_FAIL_BOARD);
            }
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
