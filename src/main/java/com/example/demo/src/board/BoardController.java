package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.board.model.*;
import com.example.demo.utils.JwtService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.SUCCESS_MODIFY_BOARD;

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

    /* 동네 생활 게시글 세부 조회 */
    @ResponseBody
    @GetMapping("/{boardIdx}")
    public BaseResponse<List<GetBoardDetailRes>> getBoard(@PathVariable("boardIdx") int boardIdx) {
        try {
            List<GetBoardDetailRes> getBoardDetailRes = boardProvider.getBoard(boardIdx);
            return new BaseResponse<>(getBoardDetailRes);
        } catch(BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 동네 생활 게시글 수정 */
    @ResponseBody
    @PatchMapping("/{boardIdx}")
    public BaseResponse<String> modifyBoard(@PathVariable("boardIdx") int boardIdx, @RequestBody PatchBoardReq patchBoardReq) {
        try {
            patchBoardReq = new PatchBoardReq(
                    patchBoardReq.getCategoryIdx(),
                    patchBoardReq.getContent(),
                    boardIdx
            );
            boardService.modifyBoard(patchBoardReq);
            return new BaseResponse<>(SUCCESS_MODIFY_BOARD);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
