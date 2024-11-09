package br.com.db.voting.services;

import br.com.db.voting.models.Session;
import br.com.db.voting.repositories.SessionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session createSession(Session session) {
        if (session == null || session.getTopic() == null) {
            throw new IllegalArgumentException("Session and Topic cannot be null.");
        }
        session.setOpen(true); 
        return sessionRepository.save(session);
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + id));
    }

    public Session updateSession(Long id, Session updatedSession) {
        Session existingSession = getSessionById(id); 

        if (updatedSession.getTopic() == null) {
            throw new IllegalArgumentException("Topic cannot be null.");
        }

        existingSession.setTopic(updatedSession.getTopic());
        existingSession.setInitialDateTime(updatedSession.getInitialDateTime());
        existingSession.setFinalDateTime(updatedSession.getFinalDateTime());
        return sessionRepository.save(existingSession);
    }

    public void deleteSession(Long id) {
        Session session = getSessionById(id); 
        sessionRepository.delete(session);
    }

    public Session closeSession(Long id) {
        Session session = getSessionById(id);
        session.setOpen(false); 
        return sessionRepository.save(session);
    }
}
