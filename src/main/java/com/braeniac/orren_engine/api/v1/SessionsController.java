package com.braeniac.orren_engine.api.v1;

import com.braeniac.orren_engine.application.SessionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/session")
public class SessionsController {

    private SessionService sessionService;

    public SessionsController(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping

}



