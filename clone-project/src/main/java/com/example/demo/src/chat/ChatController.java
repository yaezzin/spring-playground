package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.chat.model.GetChatDetailRes;
import com.example.demo.src.chat.model.GetChatRes;
import com.example.demo.src.chat.model.PostChatReq;
import com.example.demo.src.product.ProductProvider;
import com.example.demo.src.product.ProductService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.example.demo.config.BaseResponseStatus.SUCCESS_CREATE_CHAT;

@RestController
@RequestMapping("/app/chats")
public class ChatController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired private final ChatProvider chatProvider;
    @Autowired private final ChatService chatService;
    @Autowired private final JwtService jwtService;

    public ChatController(ChatProvider chatProvider, ChatService chatService, JwtService jwtService) {
        this.chatProvider = chatProvider;
        this.chatService = chatService;
        this.jwtService = jwtService;
    }


    /* 채팅 생성 */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> createChat(@RequestBody PostChatReq postChatReq) throws BaseException {
        int userIdxByJwt = jwtService.getUserIdx();
        if (postChatReq.getSenderIdx() != userIdxByJwt) {
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        try {
            chatService.createChat(postChatReq);
            return new BaseResponse<>(SUCCESS_CREATE_CHAT);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 채팅방 세부 조회 */
    @ResponseBody
    @GetMapping("/{chatRoomIdx}")
    public BaseResponse<List<GetChatDetailRes>> getChatRoom(@PathVariable("chatRoomIdx") int chatRoomIdx) throws BaseException {
        try {
            List<GetChatDetailRes> getChatDetailRes = chatProvider.getChatRoom(chatRoomIdx);
            return new BaseResponse<>(getChatDetailRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 채팅방 전체 조회 */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetChatRes>> getChatRooms() {
        try {
            List<GetChatRes> getChatResList = chatProvider.getChatRooms();
            return new BaseResponse<>(getChatResList);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
