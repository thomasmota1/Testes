package com.tms.aula8.examples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceMockitoTest {
    @Test
    void shouldTransferMoneyAndUpdateBothAccounts() {
        Account sender = new Account("1", 200);
        Account beneficiary = new Account("2", 100);
        AccountManager accountManager = mock(AccountManager.class);
        when(accountManager.findAccountForUser("sender")).thenReturn(sender);
        when(accountManager.findAccountForUser("beneficiary")).thenReturn(beneficiary);

        new AccountService(accountManager).transfer("sender", "beneficiary", 50);

        assertEquals(150, sender.getBalance());
        assertEquals(150, beneficiary.getBalance());
        verify(accountManager).updateAccount(sender);
        verify(accountManager).updateAccount(beneficiary);
    }
}
