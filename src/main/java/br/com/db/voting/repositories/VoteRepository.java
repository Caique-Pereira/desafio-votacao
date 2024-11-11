package br.com.db.voting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.db.voting.models.Topic;
import br.com.db.voting.models.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long>{
    boolean existsByAssociatedIdAndTopic(Long associatedId, Topic topic);
    
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.topic = :topic AND v.vote = true")
    long countPositiveVotesByTopic(Topic topic);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.topic = :topic AND v.vote = false")
    long countNegativeVotesByTopic(Topic topic);
    
  
}


