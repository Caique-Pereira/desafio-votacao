package br.com.db.voting.controllers;

import br.com.db.voting.models.Session;
import br.com.db.voting.records.CreateSessionRequest;
import br.com.db.voting.services.SessionService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping
    public ResponseEntity<Session> createSession(@Valid @RequestBody CreateSessionRequest request ) {
    	Session createdSession = sessionService.createSession(request.topic(), request.finalDatetime());
        return ResponseEntity.ok(createdSession);
    }
}
