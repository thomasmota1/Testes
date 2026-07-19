package com.tms.aula8.examples;

import java.util.Objects;

public class AccountService {
    private final AccountManager accountManager;

    public AccountService(AccountManager accountManager) {
        this.accountManager = Objects.requireNonNull(accountManager);
    }

    public void transfer(String senderId, String beneficiaryId, long amount) {
        Account sender = accountManager.findAccountForUser(senderId);
        Account beneficiary = accountManager.findAccountForUser(beneficiaryId);
        if (sender == null || beneficiary == null) {
            throw new IllegalArgumentException("Account not found");
        }

        sender.debit(amount);
        beneficiary.credit(amount);
        accountManager.updateAccount(sender);
        accountManager.updateAccount(beneficiary);
    }
}
