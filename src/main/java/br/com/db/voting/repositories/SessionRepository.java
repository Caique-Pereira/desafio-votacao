package br.com.db.voting.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.db.voting.models.Session;
import br.com.db.voting.models.Topic;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findOpenSessionByTopic(Topic topic);

}
