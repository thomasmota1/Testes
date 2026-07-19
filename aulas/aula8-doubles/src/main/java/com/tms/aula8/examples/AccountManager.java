package com.tms.aula8.examples;

public interface AccountManager {
    Account findAccountForUser(String userId);

    void updateAccount(Account account);
}
