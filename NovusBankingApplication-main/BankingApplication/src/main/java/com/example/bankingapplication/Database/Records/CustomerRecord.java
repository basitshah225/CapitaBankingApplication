package com.example.bankingapplication.Database.Records;

/**
 * Record which stores information from the customer table
 * @param firstName The first name of the Customer
 * @param lastName The last name of the Customer
 * @param address The address of the Customer
 * @param registrationNumber The registration number of the Customer
 */
public record CustomerRecord(String firstName, String lastName, String address, String registrationNumber) {
}
