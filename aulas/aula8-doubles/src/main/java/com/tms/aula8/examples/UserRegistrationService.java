package com.tms.aula8.examples;

public class UserRegistrationService {
    private final UserRepository repository;
    private final EmailService emailService;
    private final Logger logger;

    public UserRegistrationService(UserRepository repository, EmailService emailService, Logger logger) {
        this.repository = repository;
        this.emailService = emailService;
        this.logger = logger;
    }

    public void register(User user) {
        repository.save(user);
        emailService.send(user.getId(), "Welcome, " + user.getName());
        logger.log("registered " + user.getId());
    }
}
