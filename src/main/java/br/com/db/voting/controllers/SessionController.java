package br.com.db.voting.controllers;

import br.com.db.voting.models.Session;
import br.com.db.voting.services.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping
    public ResponseEntity<Session> createSession(@RequestBody Session session) {
        Session createdSession = sessionService.createSession(session);
        return ResponseEntity.ok(createdSession);
    }

    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        Session session = sessionService.getSessionById(id);
        return ResponseEntity.ok(session);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable Long id, @RequestBody Session updatedSession) {
        Session session = sessionService.updateSession(id, updatedSession);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    // CLOSE SESSION
    @PutMapping("/{id}/close")
    public ResponseEntity<Session> closeSession(@PathVariable Long id) {
        Session closedSession = sessionService.closeSession(id);
        return ResponseEntity.ok(closedSession);
    }
}
