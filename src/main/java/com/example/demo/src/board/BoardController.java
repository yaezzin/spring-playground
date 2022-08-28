package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.board.model.GetBoardRes;
import com.example.demo.src.board.model.PostBoardReq;
import com.example.demo.src.board.model.PostBoardRes;
import com.example.demo.utils.JwtService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/boards")
public class BoardController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BoardProvider boardProvider;
    private final BoardService boardService;
    private final JwtService jwtService;

    public BoardController(BoardProvider boardProvider, BoardService boardService, JwtService jwtService) {
        this.boardProvider = boardProvider;
        this.boardService = boardService;
        this.jwtService = jwtService;
    }

    /* 동네 생활 게시글 등록 */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostBoardRes> createBoard(@RequestBody PostBoardReq postBoardReq) {
        try {
           PostBoardRes postBoardRes = boardService.createBoard(postBoardReq);
           return new BaseResponse<>(postBoardRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 동네 생활 게시글 리스트 조회 */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetBoardRes>> getBoards(@RequestParam(required = false) String search) {
        try {
            if (search == null) {
                List<GetBoardRes> getBoardResList = boardProvider.getBoards();
                return new BaseResponse<>(getBoardResList);
            }
            List<GetBoardRes> getBoardResList = boardProvider.getBoardsByKeyword(search);
            return new BaseResponse<>(getBoardResList);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
