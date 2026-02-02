package com.example.assistantmoderator.repository;

import com.example.assistantmoderator.model.MessageAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageAnalysisRepository extends JpaRepository<MessageAnalysis, Long> {
    Optional<MessageAnalysis> findByUserText(String text);
}
