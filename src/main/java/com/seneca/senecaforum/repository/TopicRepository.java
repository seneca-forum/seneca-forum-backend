package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic,Integer> {
}
