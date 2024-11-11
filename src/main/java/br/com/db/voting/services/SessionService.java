package br.com.db.voting.services;

import br.com.db.voting.models.Session;
import br.com.db.voting.models.Topic;
import br.com.db.voting.repositories.SessionRepository;
import br.com.db.voting.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class SessionService {
    
    @Autowired
    private TopicRepository topicRepository;
    
    @Autowired
    private SessionRepository sessionRepository;

    public Session createSession(Long topicId, LocalDateTime finalDatetime) {
    	
        if (topicId == null) throw new IllegalArgumentException("Id da pauta não pode ser nulo.");
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException(String.format("Pauta com id %d não existe", topicId)));
        if (sessionRepository.existsByTopicAndFinalDateTimeAfter(topic, LocalDateTime.now())) throw new RuntimeException("Já existe uma sessão aberta para esta pauta.");
        
        
        if (finalDatetime == null) finalDatetime = LocalDateTime.now().plusMinutes(1);
        Session session = new Session();
        session.setTopic(topic);
        session.setFinalDateTime(finalDatetime);
        sessionRepository.save(session);
        
        return session;
    }
}
