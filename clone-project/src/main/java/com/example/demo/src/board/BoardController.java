package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.board.model.*;
import com.example.demo.src.product.model.GetProdRes;
import com.example.demo.utils.JwtService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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

    /* 동네 생활 게시글 리스트 조회 by 카테고리 */
    @ResponseBody
    @GetMapping("/category")
    public BaseResponse<List<GetBoardRes>> getBoardsByCategory(@RequestParam int boardCategoryIdx) {
        try {
            List<GetBoardRes> getBoardResListByCategory = boardProvider.getBoardsByCategory(boardCategoryIdx);
            return new BaseResponse<>(getBoardResListByCategory);
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

    /* 동네 생활 게시글 삭제 */
    @ResponseBody
    @DeleteMapping("/{boardIdx}")
    public BaseResponse<String> deleteBoard(@PathVariable("boardIdx") int boardIdx) {
        try {
            boardService.deleteBoard(boardIdx);
            return new BaseResponse<>(SUCCESS_DELETE_BOARD);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 동네 생활 댓글 생성 */
    @ResponseBody
    @PostMapping("/{boardIdx}/comments")
    public BaseResponse<List<GetBoardDetailRes>> createComment(@PathVariable("boardIdx") int boardIdx, @RequestBody PostCommentReq postCommentReq) {
        try{
            int userIdx = jwtService.getUserIdx();
            List<GetBoardDetailRes> getBoardRes = boardService.createComment(postCommentReq, boardIdx, userIdx);
            return new BaseResponse<>(getBoardRes);

        } catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @DeleteMapping("/{boardIdx}/comments")
    public BaseResponse<String> deleteComment(@PathVariable("boardIdx") int boardIdx, @RequestBody DeleteCommentReq deleteCommentReq) {
        try {
            boardService.deleteComment(deleteCommentReq);
            return new BaseResponse<>(SUCCESS_DELETE_COMMENT);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /* 동네 생활 댓글 수정 */
    @ResponseBody
    @PatchMapping("/{boardIdx}/comments")
    public BaseResponse<List<GetBoardDetailRes>> modifyComment(@PathVariable("boardIdx") int boardIdx, @RequestBody PatchCommentReq patchCommentReq) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            int commentIdx = patchCommentReq.getCommentIdx();

            // 댓글을 생성한 유저와 로그인한 유저가 같은지 확인
            int userIdx = boardProvider.getCommentUserIdx(commentIdx);
            if (userIdxByJwt != userIdx) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // 댓글과 게시물의 ID가 같은지 확인
            int boardCommentIdx = boardProvider.getCommentBoardIdx(commentIdx);
            if (boardCommentIdx != boardIdx) {
                return new BaseResponse<>(MODIFY_FAIL_COMMENT);
            }

            List<GetBoardDetailRes> getBoardRes = boardService.modifyComment(patchCommentReq.getComment(), commentIdx, boardIdx);
            return new BaseResponse<>(getBoardRes);

        } catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
