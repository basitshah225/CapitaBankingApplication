package com.example.bankingapplication;

import com.example.bankingapplication.Accounts.*;
import com.example.bankingapplication.Accounts.Records.AccountIdentifier;
import com.example.bankingapplication.Database.AccountHandler;
import com.example.bankingapplication.Security.InputVerifier;
import com.example.bankingapplication.Accounts.AccountManager;
import com.example.bankingapplication.Threads.ThreadRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;
import static com.example.bankingapplication.Accounts.PhotoIDType.*;

/**
 * The main class console
 */
public class ConsoleMain {
    public static void main(String[] args) throws SQLException, IOException {
        ThreadRunner threadRunner = ThreadRunner.getInstance();
        boolean continueLoop = true;
        Account currentAccount = null;
        String result;

        //run the system until user wants to exit
        while (continueLoop) {
            result = newOrExistingCustomerMenu();
            if (result.equals("New Customer")) {
                currentAccount = createAccount();
            } else if (result.equals("Existing Customer")) {
                currentAccount = null;
                while (currentAccount == null) {
                    currentAccount = verifyCustomer();
                }
            } else if (result.equals("Exit System")) {
                continueLoop = false;
                threadRunner.cancelThread();
                break;
            }
            existingCustomerMenu(currentAccount);
        }
    }
    /**
     * @return returns the account based on customer information
     * @throws SQLException
     */
    private static Account verifyCustomer() throws SQLException {
        Account customerAccount;
        AccountManager account = new AccountManager();
        InputVerifier verifier = new InputVerifier();
        Scanner input = new Scanner(System.in);
        String userInput;
        boolean continueLoop = true;
        String[] details = new String[5];
        //Loop until valid name
        while (continueLoop) {
            System.out.print("Please enter customers first name: ");
            userInput = input.nextLine();
            if (verifier.verifyName(userInput)) {
                details[0] = userInput;
                continueLoop = false;   //escape loop
            } else {
                System.out.println("Invalid input: Name cannot contain numbers or empty space");
            }
        }

        continueLoop = true; //reset boolean value
        //loop until valid last name
        while (continueLoop) {
            System.out.print("Please enter customers last name: ");
            userInput = input.nextLine();
            if (verifier.verifyName(userInput)) {
                details[1] = userInput;
                continueLoop = false;
            } else {
                System.out.println("Invalid input: Name cannot contain numbers or empty space");
            }
        }
        //loop until valid postcode entered
        details[2] = customerAddress();
        //loop until valid account number
        continueLoop = true;
        while (continueLoop) {
            System.out.print("Please enter customers account number: ");
            userInput = input.nextLine();
            if (verifier.verifyAccountNumber(userInput)) {
                details[3] = userInput;
                continueLoop = false;
            } else {
                System.out.println("Invalid input: account number can only contain numbers");
            }
        }
        //loop until valid sort code
        continueLoop = true;
        while (continueLoop) {
            System.out.print("Please enter customers sort code: ");
            userInput = input.nextLine();
            if (verifier.verifySortCode(userInput)) {
                details[4] = userInput;
                continueLoop = false;
            } else {
                System.out.println("Invalid input: sort code must be a 6 digit number");
            }
        }
        //try getting account and force re-entry of data if verification fails
        try {
            customerAccount = account.getAccount(Integer.parseInt(details[3]), Integer.parseInt(details[4]), details[0], details[1], details[2]);
            if (customerAccount.getAccountType().equals("PersonalAccount")) {
                PersonalAccount acc = (PersonalAccount) customerAccount;
                System.out.println("Customer verified successfully");
                return acc;
            } else if (customerAccount.getAccountType().equals("ISAAccount")) {
                ISAAccount acc = (ISAAccount) customerAccount;
                System.out.println("Customer verified successfully");
                return acc;
            } else if (customerAccount.getAccountType().equals("BusinessAccount")) {
                BusinessAccount acc = (BusinessAccount) customerAccount;
                System.out.println("Customer verified successfully");
                return acc;
            } else {
                System.out.println("Customer verification failed");
            }
        } catch (Exception e) {
            System.out.println("Customer verification failed");
            return null;
        }
        return null;
    }
    /**
     * @param currentAccount The account currently being accessed
     */
    private static void accountOptions(Account currentAccount) {
        //print options based on current account type being accessed
        switch (currentAccount.getAccountType()) {
            case "PersonalAccount":
                System.out.printf("Viewing Personal Account For %s %n", currentAccount.getFirstName().toUpperCase());
                System.out.println("1. Deposit Money");
                System.out.println("2. Withdraw Money");
                System.out.println("3. Send Money");
                System.out.println("4. View Account Details");
                System.out.println("5. Change Customer Details");
                System.out.println("6. Change Overdraft Limit");
                System.out.println("7. Exit Current Menu");
                System.out.print("Please press a number corresponding to the above options: ");
                break;
            case "BusinessAccount":
                System.out.printf("Viewing Business Account For %s %n", currentAccount.getFirstName().toUpperCase());
                System.out.println("1. Deposit Money");
                System.out.println("2. Withdraw Money");
                System.out.println("3. Send Money");
                System.out.println("4. View Account Details");
                System.out.println("5. Change Customer Details");
                System.out.println("6. Change Overdraft Limit");
                System.out.println("7. Change Loan Limit");
                System.out.println("8. Exit Current Menu");
                System.out.print("Please press a number corresponding to the above options: ");
                break;
            case "ISAAccount":
                System.out.printf("Viewing ISA Account For %s %n", currentAccount.getFirstName().toUpperCase());
                System.out.println("1. Deposit Money");
                System.out.println("2. Withdraw Money");
                System.out.println("3. Send Money");
                System.out.println("4. View Account Details");
                System.out.println("5. Change Customer Details");
                System.out.println("6. Exit Current Menu");
                System.out.print("Please press a number corresponding to the above options: ");
                break;
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void depositPersonalAccount(Account currentAccount) throws SQLException {
        double currentBalance;
        double maxBalance = 999999999999999.9999;
        String userInput;
        Scanner input = new Scanner(System.in);
        InputVerifier verifier = new InputVerifier();
        PersonalAccount personalAccount = (PersonalAccount) currentAccount;

        currentBalance = Double.parseDouble(String.valueOf(currentAccount.getBalance()));
        System.out.print("How much money would the customer like to deposit?: ");
        userInput = input.nextLine();
        if (verifier.isANumberInRange(userInput, 0.01, (maxBalance - currentBalance))) {
            personalAccount.confirmDeposit(currentAccount.getAccountNumber(), currentAccount.getSortCode(), BigDecimal.valueOf(Double.parseDouble(userInput)));
            System.out.printf("£%s deposited into current account%n", userInput);
        } else {
            System.out.println("Invalid input: input must be a number greater than 0 and must not make the balance exceed 999999999999999.99");
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void depositBusinessAccount(Account currentAccount) throws SQLException {
        double currentBalance;
        double maxBalance = 999999999999999.9999;
        String userInput;
        Scanner input = new Scanner(System.in);
        InputVerifier verifier = new InputVerifier();
        BusinessAccount businessAccount = (BusinessAccount) currentAccount;

        currentBalance = Double.parseDouble(String.valueOf(currentAccount.getBalance()));
        System.out.print("How much money would the customer like to deposit?: ");
        userInput = input.nextLine();
        if (verifier.isANumberInRange(userInput, 0.01, (maxBalance - currentBalance))) {
            businessAccount.confirmDeposit(currentAccount.getAccountNumber(), currentAccount.getSortCode(), BigDecimal.valueOf(Double.parseDouble(userInput)));
            System.out.printf("£%s deposited into current account%n", userInput);
        } else {
            System.out.println("Invalid input: input must be a number greater than 0 and must not make the balance exceed 999999999999999.99");
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void depositIsaAccount(Account currentAccount) throws SQLException {
        double currentBalance;
        double maxBalance = 999999999999999.9999;
        String userInput;
        Scanner input = new Scanner(System.in);
        InputVerifier verifier = new InputVerifier();
        ISAAccount isaAccount = (ISAAccount) currentAccount;

        currentBalance = Double.parseDouble(String.valueOf(currentAccount.getBalance()));
        System.out.print("How much money would the customer like to deposit?: ");
        userInput = input.nextLine();
        if (verifier.isANumberInRange(userInput, 0.01, (maxBalance - currentBalance))) {
            isaAccount.increaseMoneyInThisYear(currentAccount.getAccountNumber(), currentAccount.getSortCode(), BigDecimal.valueOf(Double.parseDouble(userInput)));
            System.out.printf("£%s deposited into current account%n", userInput);
        } else {
            System.out.println("Invalid input: input must be a number greater than 0 and must not make the balance exceed 999999999999999.99");
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void withdrawPersonalAccount(Account currentAccount) throws SQLException {
        double currentBalance;
        double overdraftAmount;
        double highest;
        String userInput;
        Scanner input = new Scanner(System.in);
        InputVerifier verifier = new InputVerifier();
        PersonalAccount personalAccount = (PersonalAccount) currentAccount;

        currentBalance = Double.parseDouble(String.valueOf(currentAccount.getBalance()));
        overdraftAmount = Double.parseDouble(String.valueOf(personalAccount.getOverDraftAmount()));
        highest = Math.round((currentBalance + overdraftAmount) * 100.0) / 100.0;

        System.out.print("How much money would the customer like to withdraw?: ");
        userInput = input.nextLine();
        if (verifier.isANumberInRange(userInput, 0.01, highest)) {
            personalAccount.confirmWithdraw(currentAccount.getAccountNumber(), currentAccount.getSortCode(), BigDecimal.valueOf(Double.parseDouble(userInput)));
        } else {
            System.out.println("Invalid input: input can only be a number greater than 0 and must not make the balance exceed its overdraft limit");
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void withdrawBusinessAccount(Account currentAccount) throws SQLException {
        double currentBalance;
        double overdraftAmount;
        double loanAmount;
        double highest;
        String userInput;
        Scanner input = new Scanner(System.in);
        InputVerifier verifier = new InputVerifier();
        BusinessAccount businessAccount = (BusinessAccount) currentAccount;

        currentBalance = Double.parseDouble(String.valueOf(currentAccount.getBalance()));
        overdraftAmount = Double.parseDouble(String.valueOf(businessAccount.getOverDraftAmount()));
        loanAmount = Double.parseDouble(String.valueOf(businessAccount.getLoanAmount()));
        highest = Math.round((currentBalance + overdraftAmount + loanAmount) * 100.0) / 100.0;
        System.out.println("How much money would the customer like to withdraw?: ");
        userInput = input.nextLine();
        if (verifier.isANumberInRange(userInput, 0.01, (highest))) {
            businessAccount.confirmWithdraw(currentAccount.getAccountNumber(), currentAccount.getSortCode(), BigDecimal.valueOf(Double.parseDouble(userInput)));
        } else {
            System.out.println("Invalid input: input can only be a number greater than 0 and must not make the balance exceed its overdraft plus loan limit");
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void withdrawIsaAccount(Account currentAccount) throws SQLException {
        double currentBalance;
        double highest;
        String userInput;
        Scanner input = new Scanner(System.in);
        InputVerifier verifier = new InputVerifier();
        ISAAccount isaAccount = (ISAAccount) currentAccount;

        currentBalance = Double.parseDouble(String.valueOf(currentAccount.getBalance()));
        highest = highest = Math.round((currentBalance) * 100.0) / 100.0;
        System.out.println("How much money would the customer like to withdraw?: ");
        userInput = input.nextLine();
        if (verifier.isANumberInRange(userInput, 0.01, (highest))) {
            isaAccount.confirmWithdraw(currentAccount.getAccountNumber(), currentAccount.getSortCode(), BigDecimal.valueOf(Double.parseDouble(userInput)));
        } else {
            System.out.println("Invalid input: input can only be a number greater than 0 and must not make the balance negative");
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void sendMoneyFromPersonalAccount(Account currentAccount) throws SQLException {
        double currentBalance;
        double overdraftAmount;
        double sendAmount = 0;
        double highest;
        int receiverSortcode = 0;
        int receiverAccountNumber = 0;
        int skipLoop = 0;
        boolean continueLoop = false;
        String userInput;
        Scanner input = new Scanner(System.in);
        InputVerifier verifier = new InputVerifier();
        PersonalAccount personalAccount = (PersonalAccount) currentAccount;

        currentBalance = Double.parseDouble(String.valueOf(currentAccount.getBalance()));
        overdraftAmount = Double.parseDouble(String.valueOf(personalAccount.getOverDraftAmount()));
        highest = Math.round((currentBalance + overdraftAmount) * 100.0) / 100.0;
        System.out.print("How much money would the customer like to send?: ");
        userInput = input.nextLine();
        if (verifier.isANumberInRange(userInput, 0.0001, highest)) {
            sendAmount = Double.parseDouble(userInput);
            continueLoop = true;
        } else {
            System.out.println("Invalid Input: input must be a number greater than 0 and not make the balance exceed the overdraft limit");
            skipLoop = 1;
        }
        if (skipLoop == 0) {
            while (continueLoop) {
                System.out.print("Please input the sort code of the account to transfer money to: ");
                userInput = input.nextLine();
                if (verifier.verifySortCode(userInput)) {
                    receiverSortcode = Integer.parseInt(userInput);
                    continueLoop = false;
                } else {
                    System.out.println("Invalid Input: sort code must be a 6 digit number");
                }
            }
            while (continueLoop) {
                System.out.print("Please input the account number of the account to transfer money to: ");
                userInput = input.nextLine();
                if (verifier.verifyAccountNumber(userInput)) {
                    receiverAccountNumber = Integer.parseInt(userInput);
                    continueLoop = false;
                } else {
                    System.out.println("Invalid Input: account number can only contain numbers");
                }
            }
            continueLoop = true;
            if (receiverAccountNumber == currentAccount.getAccountNumber()) {
                System.out.println("Cannot send money from this account to itself");
            } else {
                personalAccount.sendMoneyToOtherAccount(currentAccount.getAccountNumber(), currentAccount.getSortCode(), BigDecimal.valueOf(sendAmount), receiverAccountNumber, receiverSortcode);
                System.out.printf("£%s sent successfully to other account%n", sendAmount);
            }
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void sendMoneyFromBusinessAccount(Account currentAccount) throws SQLException {
        double currentBalance;
        double overdraftAmount;
        double loanAmount;
        double sendAmount = 0;
        double highest;
        int receiverSortcode = 0;
        int receiverAccountNumber = 0;
        int skipLoop = 0;
        boolean continueLoop = true;
        String userInput;
        Scanner input = new Scanner(System.in);
        InputVerifier verifier = new InputVerifier();
        BusinessAccount businessAccount = (BusinessAccount) currentAccount;

        currentBalance = Double.parseDouble(String.valueOf(currentAccount.getBalance()));
        overdraftAmount = Double.parseDouble(String.valueOf(businessAccount.getOverDraftAmount()));
        loanAmount = Double.parseDouble(String.valueOf(businessAccount.getLoanAmount()));
        highest = Math.round((currentBalance + overdraftAmount + loanAmount) * 100.0) / 100.0;

        System.out.print("How much money would the customer like to send?: ");
        userInput = input.nextLine();
        if (verifier.isANumberInRange(userInput, 0.01, (highest))) {
            sendAmount = Double.parseDouble(userInput);
        } else {
            System.out.println("Invalid Input: input must be 0.01 or greater and not make the balance exceed the overdraft + loan limit");
            skipLoop = 1;
        }
        if (skipLoop == 0) {
            while (continueLoop) {
                System.out.print("Please input the sort code of the account to transfer money to: ");
                userInput = input.nextLine();
                if (verifier.verifySortCode(userInput)) {
                    receiverSortcode = Integer.parseInt(userInput);
                    continueLoop = false;
                } else {
                    System.out.println("Invalid Input: sort code must be a 6 digit number");
                }
            }
            continueLoop = true;
            while (continueLoop) {
                System.out.print("Please input the account number of the account to transfer money to: ");
                userInput = input.nextLine();
                if (verifier.verifyAccountNumber(userInput)) {
                    receiverAccountNumber = Integer.parseInt(userInput);
                    continueLoop = false;
                } else {
                    System.out.println("Invalid Input: account number can only contain numbers");
                }
            }
            if (receiverAccountNumber == currentAccount.getAccountNumber()) {
                System.out.println("Cannot send money from this account to itself");
            } else {
                businessAccount.sendMoneyToOtherAccount(currentAccount.getAccountNumber(), currentAccount.getSortCode(), BigDecimal.valueOf(sendAmount), receiverAccountNumber, receiverSortcode);
                System.out.printf("£%s sent successfully to other account%n", sendAmount);
            }
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void sendMoneyFromIsaAccount(Account currentAccount) throws SQLException {
        double currentBalance;
        double sendAmount = 0;
        double highest;
        int receiverSortcode = 0;
        int receiverAccountNumber = 0;
        int skipLoop = 0;
        boolean continueLoop = true;
        String userInput;
        Scanner input = new Scanner(System.in);
        InputVerifier verifier = new InputVerifier();
        ISAAccount isaAccount = (ISAAccount) currentAccount;

        currentBalance = Double.parseDouble(String.valueOf(currentAccount.getBalance()));
        highest = Math.round((currentBalance) * 100.0) / 100.0;

        System.out.print("How much money would the customer like to send?: ");
        userInput = input.nextLine();
        if (verifier.isANumberInRange(userInput, 0.01, (currentBalance))) {
            sendAmount = Double.parseDouble(userInput);
        } else {
            System.out.println("Invalid Input: input must be a number greater than 0 and not make the balance negative");
            skipLoop = 1;
        }
        if (skipLoop == 0) {
            while (continueLoop) {
                System.out.print("Please input the sort code of the account to transfer money to: ");
                userInput = input.nextLine();
                if (verifier.verifySortCode(userInput)) {
                    receiverSortcode = Integer.parseInt(userInput);
                    continueLoop = false;
                } else {
                    System.out.println("Invalid Input: sort code must be a 6 digit number");
                }
            }
            continueLoop = true;
            while (continueLoop) {
                System.out.print("Please input the account number of the account to transfer money to: ");
                userInput = input.nextLine();
                if (verifier.verifyAccountNumber(userInput)) {
                    receiverAccountNumber = Integer.parseInt(userInput);
                    continueLoop = false;
                } else {
                    System.out.println("Invalid Input: account number can only contain numbers");
                }
            }
            if (receiverAccountNumber == currentAccount.getAccountNumber()) {
                System.out.println("Cannot send money from this account to itself");
            } else {
                isaAccount.sendMoneyToOtherAccount(currentAccount.getAccountNumber(), currentAccount.getSortCode(), BigDecimal.valueOf(sendAmount), receiverAccountNumber, receiverSortcode);
                System.out.printf("£%s sent successfully to other account%n", sendAmount);
            }
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void personalAccountDetails(Account currentAccount) throws SQLException {
        String accountType = "Personal Account";
        int accountNumber = currentAccount.getAccountNumber();
        int accountSortCode = currentAccount.getSortCode();
        BigDecimal accountBalance = currentAccount.getBalance();
        BigDecimal accountOverdraftLimit = ((PersonalAccount) currentAccount).getOverDraftAmount();

        System.out.printf("%s%nAccount Number: %s%nAccount SortCode: %s%nAccount Balance: %s%nAccount Overdraft Limit: %s%n",
                accountType, accountNumber, accountSortCode, accountBalance, accountOverdraftLimit);
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void businessAccountDetails(Account currentAccount) throws SQLException {
        String accountType = "Business Account";
        int accountNumber = currentAccount.getAccountNumber();
        int accountSortCode = currentAccount.getSortCode();
        BigDecimal accountBalance = currentAccount.getBalance();
        BigDecimal accountOverdraftLimit = ((BusinessAccount) currentAccount).getOverDraftAmount();
        BigDecimal accountLoanLimit = ((BusinessAccount) currentAccount).getLoanAmount();

        System.out.printf("%s%nAccount Number: %s%nAccount SortCode: %s%nAccount Balance: %s%nAccount Overdraft Limit: %s%nAccount Loan Limit: %s%n",
                accountType, accountNumber, accountSortCode, accountBalance, accountOverdraftLimit, accountLoanLimit);
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void isaAccountDetails(Account currentAccount) throws SQLException {
        String accountType = "ISA Account";
        int accountNumber = currentAccount.getAccountNumber();
        int accountSortCode = currentAccount.getSortCode();
        BigDecimal accountBalance = currentAccount.getBalance();

        System.out.printf("%s%nAccount Number: %s%nAccount SortCode: %s%nAccount Balance: %s%n",
                accountType, accountNumber, accountSortCode, accountBalance);
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void changeCustomerDetails(Account currentAccount) throws SQLException {
        boolean continueLoop = true;
        String userInput;
        String newAddress;
        Scanner input = new Scanner(System.in);
        AccountHandler handler = new AccountHandler();

        while (continueLoop) {
            System.out.println("1. Change customer's last name");
            System.out.println("2. Change customer's address");
            System.out.print("Please enter a number corresponding to the options above: ");
            userInput = input.nextLine();
            if (userInput.equals("1")) {
                while (continueLoop) {
                    System.out.print("Please enter customers new last name: ");
                    userInput = input.nextLine();
                    if (userInput.matches("[a-zA-Z]+")) {
                        handler.updateAccountLastName(currentAccount.getAccountNumber(), currentAccount.getSortCode(), userInput);
                        System.out.printf("Customer's last name changed to %s%n", userInput);
                        continueLoop = false;
                    } else {
                        System.out.println("Invalid Input: name can only contain letters");
                    }
                }
            } else if (userInput.equals("2")) {
                System.out.println("Please enter customers new address details");
                newAddress = customerAddress();
                System.out.println();
                handler.updateAccountAddress(currentAccount.getAccountNumber(), currentAccount.getSortCode(), newAddress);
                continueLoop = false;
            } else {
                System.out.println("Invalid Input: input can only be either 1 or 2");
            }
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void changePersonalOverdraftLimit(Account currentAccount) throws SQLException {
        boolean continueLoop = true;
        String userInput;
        Scanner input = new Scanner(System.in);
        InputVerifier verifier = new InputVerifier();
        PersonalAccount personalAccount = (PersonalAccount) currentAccount;

        while (continueLoop) {
            if (Double.parseDouble(String.valueOf(currentAccount.getBalance())) < 0.00) {
                System.out.println("Cannot change overdraft amount whilst in overdraft");
                continueLoop = false;
            } else {
                System.out.print("Please enter new overdraft limit between £0 and £2500: ");
                userInput = input.nextLine();
                if (verifier.isANumberInRange(userInput, 0.00, 2500.00)) {
                    personalAccount.setOverDraftAmount(BigDecimal.valueOf(Double.parseDouble(userInput)));
                    //System.out.printf("Overdraft Limit changed to: %s%n", userInput);
                    continueLoop = false;
                } else {
                    System.out.println("Invalid Input: input can only be a number between 0 and 2500");
                    continueLoop = false;
                }
            }
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void changeBusinessOverdraftLimit(Account currentAccount) throws SQLException {
        boolean continueLoop = true;
        String userInput;
        Scanner input = new Scanner(System.in);
        InputVerifier verifier = new InputVerifier();
        BusinessAccount businessAccount = (BusinessAccount) currentAccount;

        while (continueLoop) {
            if (Double.parseDouble(String.valueOf(currentAccount.getBalance())) < 0.00) {
                System.out.println("Cannot change overdraft amount whilst in overdraft");
                continueLoop = false;
            } else {
                System.out.println("Please enter new overdraft limit between £0 and £5000");
                userInput = input.nextLine();
                if (verifier.isANumberInRange(userInput, 0.00, 5000.00)) {
                    businessAccount.setOverDraftAmount(BigDecimal.valueOf(Double.parseDouble(userInput)));
                    System.out.printf("Overdraft Limit changed to: %s%n", userInput);
                    continueLoop = false;
                } else {
                    System.out.println("Invalid Input: input can only be a number between 0 and 5000");
                    continueLoop = false;
                }
            }
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void changeBusinessLoanLimit(Account currentAccount) throws SQLException {
        boolean continueLoop = true;
        String userInput;
        Scanner input = new Scanner(System.in);
        InputVerifier verifier = new InputVerifier();
        BusinessAccount businessAccount = (BusinessAccount) currentAccount;

        while (continueLoop) {
            if (Double.parseDouble(String.valueOf(currentAccount.getBalance())) < 0.00) {
                System.out.println("Cannot change loan amount whilst in negative balance");
                continueLoop = false;
            } else {
                System.out.println("Please enter new loan limit between £0 and £10,000,000");
                userInput = input.nextLine();
                if (verifier.isANumberInRange(userInput, 0.00, 10000000)) {
                    businessAccount.setLoanAmount(BigDecimal.valueOf(Double.parseDouble(userInput)));
                    System.out.printf("Loan Limit changed to: %s%n", userInput);
                    continueLoop = false;
                } else {
                    System.out.println("Invalid Input: input can only be a number between 0 and 10000000");
                    continueLoop = false;
                }
            }
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void personalAccountActions(Account currentAccount) throws SQLException {
        boolean continueLoop = true;
        String userInput;
        Scanner input = new Scanner(System.in);

        while (continueLoop) {
            accountOptions(currentAccount);
            userInput = input.nextLine();
            switch (userInput) {
                case "1":
                    depositPersonalAccount(currentAccount);
                    break;
                case "2":
                    withdrawPersonalAccount(currentAccount);
                    break;
                case "3":
                    sendMoneyFromPersonalAccount(currentAccount);
                    break;
                case "4":
                    personalAccountDetails(currentAccount);
                    break;
                case "5":
                    changeCustomerDetails(currentAccount);
                    break;
                case "6":
                    changePersonalOverdraftLimit(currentAccount);
                    break;
                case "7":
                    continueLoop = false;
                    break;
                default:
                    System.out.println("Invalid Input: input must be one of the numbers on screen");
            }
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void businessAccountActions(Account currentAccount) throws SQLException {
        boolean continueLoop = true;
        String userInput;
        Scanner input = new Scanner(System.in);

        while (continueLoop) {
            accountOptions(currentAccount);
            userInput = input.nextLine();
            switch (userInput) {
                case "1":
                    depositBusinessAccount(currentAccount);
                    break;
                case "2":
                    withdrawBusinessAccount(currentAccount);
                    break;
                case "3":
                    sendMoneyFromBusinessAccount(currentAccount);
                    break;
                case "4":
                    businessAccountDetails(currentAccount);
                    break;
                case "5":
                    changeCustomerDetails(currentAccount);
                    break;
                case "6":
                    changeBusinessOverdraftLimit(currentAccount);
                    break;
                case "7":
                    changeBusinessLoanLimit(currentAccount);
                    break;
                case "8":
                    continueLoop = false;
                    break;
                default:
                    System.out.println("Invalid Input: input must be one of the numbers on screen");
            }
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void isaAccountActions(Account currentAccount) throws SQLException {
        boolean continueLoop = true;
        String userInput;
        Scanner input = new Scanner(System.in);

        while (continueLoop) {
            accountOptions(currentAccount);
            userInput = input.nextLine();
            switch (userInput) {
                case "1":
                    depositIsaAccount(currentAccount);
                    break;
                case "2":
                    withdrawIsaAccount(currentAccount);
                    break;
                case "3":
                    sendMoneyFromIsaAccount(currentAccount);
                    break;
                case "4":
                    isaAccountDetails(currentAccount);
                    break;
                case "5":
                    changeCustomerDetails(currentAccount);
                    break;
                case "6":
                    continueLoop = false;
                    break;
                default:
                    System.out.println("Invalid Input: input must be one of the numbers on screen");
            }
        }
    }
    /**
     * @param currentAccount The account currently being accessed
     * @throws SQLException
     */
    private static void existingCustomerMenu(Account currentAccount) throws SQLException, IOException {
        if (currentAccount.getAccountType().equals("PersonalAccount")) {
            personalAccountActions(currentAccount);
        } else if (currentAccount.getAccountType().equals("BusinessAccount")) {
            businessAccountActions(currentAccount);
        } else {
            isaAccountActions(currentAccount);
        }
    }
    /**
     * @return a string used for comparison in the main
     */
    private static String newOrExistingCustomerMenu() {
        String userInput = "";
        Scanner input = new Scanner(System.in);
        while (!userInput.equals("1") && !userInput.equals("2") && !userInput.equals("3")) {
            System.out.println("1. New Customer");
            System.out.println("2. Existing Customer");
            System.out.println("3. Exit System");
            System.out.print("Please enter a number corresponding to the above options: ");
            userInput = input.nextLine();
            if (!userInput.equals("1") && !userInput.equals("2") && !userInput.equals("3")) {
                System.out.println("Invalid input: please enter a number corresponding to the available options");
            }
        }
        if (userInput.equals("1")) {
            return "New Customer";
        } else if (userInput.equals("2")) {
            return "Existing Customer";
        } else {
            return "Exit System";
        }
    }
    /**
     * @return The account created using input details
     * @throws SQLException
     */
    private static Account createAccount() throws SQLException {
        InputVerifier verifier = new InputVerifier();
        AccountIdentifier identifier;
        AccountManager account = new AccountManager();
        String[] details;
        boolean continueLoop = true;
        Scanner input = new Scanner(System.in);
        String userInput;
        PhotoIDType type;
        int overdraftAmount;
        int loanAmount;
        int isaInitialBalance;
        int businessID = 0;

        while (continueLoop) {
            System.out.println("1. Create Personal Account");
            System.out.println("2. Create Business Account");
            System.out.println("3. Create ISA account");
            System.out.print("Please enter a number corresponding to the above options: ");
            userInput = input.nextLine();
            switch (userInput) {
                //create personal account
                case "1":
                    //get input overdraft limit
                    overdraftAmount = -1;
                    details = getCustomerDetails();
                    while (overdraftAmount < 0 || overdraftAmount > 2500) {
                        System.out.print("Please enter overdraft amount between £0 and £2,500: ");
                        userInput = input.nextLine();
                        if (!userInput.matches("[0-9]+") || (Integer.parseInt(userInput) < 0 || Integer.parseInt(userInput) > 2500)) {
                            System.out.println("Invalid input: overdraft amount can only be a number between 0 and 2500 for personal accounts");
                        } else {
                            overdraftAmount = Integer.parseInt(userInput);
                        }
                    }
                    //get correct enum
                    if (details[3].equals("passport")) {
                        type = passport;
                    } else {
                        type = driversLicense;
                    }
                    //create account
                    identifier = account.createPersonalAccount(details[0], details[1], details[2], BigDecimal.valueOf(1), type, details[4], BigDecimal.valueOf(overdraftAmount));
                    return account.getAccount(identifier.accountNumber(), identifier.sortCode(), details[0], details[1], details[2]);
                //create business account
                case "2":
                    //get input overdraft and loan limit
                    overdraftAmount = -1;
                    loanAmount = -1;
                    details = getCustomerDetails();
                    while (overdraftAmount < 0 || overdraftAmount > 5000) {
                        System.out.print("Please enter overdraft amount between £0 and £5,000: ");
                        userInput = input.nextLine();
                        if (!userInput.matches("[0-9]+") || (Integer.parseInt(userInput) < 0 || Integer.parseInt(userInput) > 10000)) {
                            System.out.println("Invalid input: overdraft amount can only be a number between 0 and 10000 for Business accounts");
                        } else {
                            overdraftAmount = Integer.parseInt(userInput);
                        }
                    }
                    while (loanAmount < 0 || loanAmount > 10000000) {
                        System.out.print("Please enter maximum loan amount between £0 and £10,000,000: ");
                        userInput = input.nextLine();
                        if (!userInput.matches("[0-9]+") || (Integer.parseInt(userInput) < 0 || Integer.parseInt(userInput) > 10000000)) {
                            System.out.println("Invalid input: maximum loan amount can only be a number between 0 and 10000000 for Business accounts");
                        } else {
                            loanAmount = Integer.parseInt(userInput);
                        }
                    }
                    //loop until valid business id
                    while (continueLoop) {
                        System.out.print("Please enter a business id: ");
                        userInput = input.nextLine();
                        if (verifier.verifyBusinessID(userInput)) {
                            businessID = Integer.parseInt(userInput);
                            continueLoop = false;
                        } else {
                            System.out.println("Invalid Input: input must be a 9 digit number");
                        }
                    }
                    continueLoop = true;
                    if (details[3].equals("passport")) {
                        type = passport;
                    } else {
                        type = driversLicense;
                    }
                    identifier = account.createBusinessAccount(details[0], details[1], details[2], BigDecimal.valueOf(1), type, details[4], businessID, BigDecimal.valueOf(overdraftAmount), BigDecimal.valueOf(loanAmount));
                    return account.getAccount(identifier.accountNumber(), identifier.sortCode(), details[0], details[1], details[2]);
                case "3":
                    //create ISA account
                    details = getCustomerDetails();
                    isaInitialBalance = -1;
                    while (isaInitialBalance < 0 || isaInitialBalance > 20000) {
                        System.out.print("Please enter customers initial deposit (Note, customer may only deposit a maximum of £20,000 per tax year: ");
                        userInput = input.nextLine();
                        if (!userInput.matches("[0-9]+") || (Integer.parseInt(userInput) < 0 || Integer.parseInt(userInput) > 20000)) {
                            System.out.println("Invalid input: deposit amount can only be a number between 0 and 20000 for ISA's");
                        } else {
                            isaInitialBalance = Integer.parseInt(userInput);
                        }
                    }
                    if (details[3].equals("passport")) {
                        type = passport;
                    } else {
                        type = driversLicense;
                    }

                    identifier = account.createISAAccount(details[0], details[1], details[2], BigDecimal.valueOf(isaInitialBalance), type, details[4]);
                    return account.getAccount(identifier.accountNumber(), identifier.sortCode(), details[0], details[1], details[2]);
                default:
                    System.out.println("Invalid input: please enter 1 to create a personal account" +
                            " 2 to create a business account and 3 to creat an ISA account");
            }
        }
        return null;
    }
    /**
     * @return string array contain all the details needed to create a new account
     */
    private static String[] getCustomerDetails() {
        String[] customerDetails = new String[5];
        boolean continueLoop = true;
        String userInput = "";
        Scanner input = new Scanner(System.in);
        InputVerifier verifier = new InputVerifier();

        //loop until valid name
        while (continueLoop) {
            System.out.print("please enter customers first name: ");
            userInput = input.nextLine();
            if (!userInput.matches("[a-zA-Z]+")) {
                System.out.println("Invalid input: Name cannot contain numbers or empty space");
            } else {
                customerDetails[0] = userInput;
                continueLoop = false;
            }
        }
        //loop until valid name
        continueLoop = true;
        while (continueLoop) {
            System.out.print("please enter customers last name: ");
            userInput = input.nextLine();
            if (!userInput.matches("[a-zA-Z]+")) {
                System.out.println("Invalid input: Name cannot contain numbers or empty space");
            } else {
                customerDetails[1] = userInput;
                continueLoop = false;
            }
        }
        //loop until valid postcode
        customerDetails[2] = customerAddress();
        //loop until valid input
        continueLoop = true;
        while (continueLoop) {
            System.out.print("What type of photo id has the customer used? press 1 for passport and 2 for driving license: ");
            userInput = input.nextLine();
            if (!userInput.equals("1") && !userInput.equals("2")) {
                System.out.println("Invalid input: press 1 for passport or 2 for driving license");
            } else if (userInput.equals("1")) {
                customerDetails[3] = "passport";
                continueLoop = false;
            } else {
                customerDetails[3] = "driversLicense";
                continueLoop = false;
            }
        }
        //loop until valid photoid
        continueLoop = true;
        while (continueLoop) {
            System.out.print("please enter customers photo id number: ");
            userInput = input.nextLine();
            if (customerDetails[3].equals("passport")) {
                if (verifier.verifyPhotoIDNumber(userInput, passport)) {
                    customerDetails[4] = userInput;
                    continueLoop = false;
                } else {
                    System.out.println("Invalid Input: input must be a 10 digit number only");
                }
            } else if (customerDetails[3].equals("driversLicense")) {
                if (verifier.verifyPhotoIDNumber(userInput, driversLicense)) {
                    customerDetails[4] = userInput;
                    continueLoop = false;
                } else {
                    System.out.println("Invalid input: input must be 10 characters long");
                }
            }
        }
        return customerDetails;
    }
    /**
     * @return a string containing the customers postcode
     */
    private static String customerAddress() {
        boolean continueLoop = true;
        String userInput;
        Scanner input = new Scanner(System.in);
        String address = "";
        InputVerifier verifier = new InputVerifier();
        //loop until valid postcode
        while (continueLoop) {
            System.out.print("please enter customers postcode: ");
            userInput = input.nextLine().toUpperCase();
            if (verifier.verifyAddress(userInput)) {
                address = address + userInput;
                continueLoop = false;

            } else {
                System.out.println("Invalid input: Input must be a valid uk postcode with the correct spacing");
            }
        }
        return address;
    }
}
