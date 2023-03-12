package com.example.bankingapplication.Database.Records;

import java.math.BigDecimal;
import java.util.Date;

/**
 * A record storing information from the ISA account table
 * @param ISAAccountID The ID of the isa account
 * @param moneyInThisYear The amount of money in this year
 * @param dateLastUpdated The date since the apr was last updated
 */
public record ISAAccountRecord(int ISAAccountID, BigDecimal moneyInThisYear, Date dateLastUpdated) { }


