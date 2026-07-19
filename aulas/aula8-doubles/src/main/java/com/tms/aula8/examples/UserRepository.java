package com.tms.aula8.examples;

public interface UserRepository {
    void save(User user);

    User findById(String id);
}
