package com.elearning.elearning_support.repositories.postgres.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.elearning.elearning_support.entities.answer.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Transactional
    @Modifying
    void deleteAllByQuestionId(Long questionId);

}
