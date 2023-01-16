package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.src.chat.model.PostChatReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ChatService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChatDao chatDao;
    private final ChatProvider chatProvider;

    @Autowired
    public ChatService(ChatDao chatDao, ChatProvider chatProvider) {
        this.chatDao = chatDao;
        this.chatProvider = chatProvider;
    }


    public void createChat(PostChatReq postChatReq) throws BaseException {
        try {
            chatDao.createChat(postChatReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
       }
    }

}
