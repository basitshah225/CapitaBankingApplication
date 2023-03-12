package com.example.bankingapplication.Database.Records;

import java.math.BigDecimal;

/**
 * Record which stores information from the business account table
 * @param businessAccountID The account id of the business
 * @param overdraftAmount The overdraft amount of the business
 * @param chequeBookIssued If a cheque book has been issued
 * @param loanAmount The amount of loan
 * @param businessID The ID of the business
 */
public record BusinessAccountRecord(int businessAccountID, BigDecimal overdraftAmount, boolean chequeBookIssued, BigDecimal loanAmount, int businessID) {
}
