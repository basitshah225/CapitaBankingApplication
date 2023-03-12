package com.example.bankingapplication.Accounts.Records;

/**
 * Record to identify an account
 * @param accountNumber The account number of the account
 * @param sortCode The sort code of the account
 */
public record AccountIdentifier(int accountNumber, int sortCode) {
}
