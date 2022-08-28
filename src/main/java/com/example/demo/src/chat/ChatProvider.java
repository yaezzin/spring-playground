package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.src.chat.model.GetChatDetailRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatProvider {

    @Autowired private final ChatDao chatDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ChatProvider(ChatDao chatDao) {
        this.chatDao = chatDao;
    }

    @Transactional
    public List<GetChatDetailRes> getChatRoom(int chatIdx) throws BaseException  {
        List<GetChatDetailRes> getChatDetailResList = chatDao.getChatRoom(chatIdx);
        return getChatDetailResList;
    }
}
