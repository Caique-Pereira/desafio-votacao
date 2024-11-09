package br.com.db.voting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.db.voting.models.Topic;
import br.com.db.voting.models.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long>{
    boolean existsByCpfAndTopic(String cpf, Topic topic);

}


