package com.security.practice.util;

import com.security.practice.domain.entity.ChatRoom;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChangeListToPage {
    public PageImpl<ChatRoom> ConvertToPage(List<ChatRoom> chatRooms, int page, int pageSize) {
        int start = (page) * pageSize;
        int end = Math.min(start + pageSize, chatRooms.size());

        List<ChatRoom> content = chatRooms.subList(start, end);
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        return new PageImpl<>(content, pageRequest, chatRooms.size());
    }
}
