package com.seneca.senecaforum.websocket.repository;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.websocket.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("FROM Message m WHERE m.senderId = :senderId AND m.recipientId = :recipientId")
    List<Message> findChatMessagesFromSelectedUser(String senderId, String recipientId);

    @Query("FROM Message m WHERE m.chatroomId = :chatroomId")
    List<Message> findChatMessagesByChatroomId(String chatroomId);

    @Query("SELECT COUNT(*) FROM Message m WHERE m.recipientId = :currentUserId AND m.senderId = :onlineUserId AND m.status = 'RECEIVED'")
    int countNewMessagesFromOnlineUser(String currentUserId, String onlineUserId);
}
