package com.example.assistantmoderator.repository;

import com.example.assistantmoderator.model.MessageAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageAnalysisRepository extends JpaRepository<MessageAnalysis, Long> {
}
