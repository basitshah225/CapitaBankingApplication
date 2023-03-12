package com.example.bankingapplication.Database.Records;

import java.math.BigDecimal;

/**
 * A record which stores information from the Personal Account
 * @param personalAccountID The personal ID of the account
 * @param overdraftAmount The overdraft amount tied to that account
 */
public record PersonalAccountRecord(int personalAccountID, BigDecimal overdraftAmount) {
}
