package com.seneca.senecaforum.websocket.repository;

import com.seneca.senecaforum.websocket.domain.Chatroom;
import com.seneca.senecaforum.websocket.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Integer> {
    @Query("FROM Chatroom c WHERE c.senderId = :senderId AND c.recipientId = :recipientId")
    Optional<Chatroom> findChatroomBySenderIdAndRecipientId(String senderId, String recipientId);
}
