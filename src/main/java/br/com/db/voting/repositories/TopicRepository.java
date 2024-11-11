package br.com.db.voting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.db.voting.models.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long>{

}

