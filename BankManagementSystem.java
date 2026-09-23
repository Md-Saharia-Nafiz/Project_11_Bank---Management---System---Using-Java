import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Bank Management System
 * Basic Java Project
 * No Database
 * Data is stored in a text file
 */

public class BankManagementSystem {

    // Scanner for taking input from user
    static Scanner input = new Scanner(System.in);

    // ArrayList to store all bank accounts
    static ArrayList<Account> accounts = new ArrayList<>();

    // File name where account information will be saved
    static final String FILE_NAME = "accounts.txt";


    // ============================================================
    // ACCOUNT CLASS
    // ============================================================

    static class Account {

        // Account information
        private String accountNumber;
        private String accountHolderName;
        private double balance;

        // Constructor
        public Account(String accountNumber, String accountHolderName, double balance) {
            this.accountNumber = accountNumber;
            this.accountHolderName = accountHolderName;
            this.balance = balance;
        }

        // Getter for account number
        public String getAccountNumber() {
            return accountNumber;
        }

        // Getter for account holder name
        public String getAccountHolderName() {
            return accountHolderName;
        }

        // Getter for balance
        public double getBalance() {
            return balance;
        }

        // Deposit money
        public void deposit(double amount) {

            if (amount > 0) {
                balance = balance + amount;

                System.out.println("Money deposited successfully.");
                System.out.println("New Balance: " + balance);
            } else {
                System.out.println("Invalid amount.");
            }
        }

        // Withdraw money
        public boolean withdraw(double amount) {

            if (amount <= 0) {
                System.out.println("Invalid amount.");
                return false;
            }

            if (amount > balance) {
                System.out.println("Insufficient balance.");
                return false;
            }

            balance = balance - amount;

            System.out.println("Money withdrawn successfully.");
            System.out.println("Remaining Balance: " + balance);

            return true;
        }

        // Display account information
        public void displayAccountInfo() {

            System.out.println("--------------------------------");
            System.out.println("Account Number : " + accountNumber);
            System.out.println("Account Holder : " + accountHolderName);
            System.out.println("Balance        : " + balance);
            System.out.println("--------------------------------");
        }
    }


    // ============================================================
    // FIND ACCOUNT
    // ============================================================

    // Find an account using account number
    public static Account findAccount(String accountNumber) {

        for (Account account : accounts) {

            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }

        return null;
    }


    // ============================================================
    // SAVE ACCOUNTS TO FILE
    // ============================================================

    public static void saveAccounts() {

        try {

            // FileWriter is used to write account information
            FileWriter writer = new FileWriter(FILE_NAME);

            for (Account account : accounts) {

                /*
                 * Data format:
                 * accountNumber|accountHolderName|balance
                 */
                writer.write(
                        account.getAccountNumber()
                        + "|"
                        + account.getAccountHolderName()
                        + "|"
                        + account.getBalance()
                        + "\n"
                );
            }

            // Close the file
            writer.close();

        } catch (IOException e) {

            System.out.println("Error while saving account data.");
        }
    }


    // ============================================================
    // LOAD ACCOUNTS FROM FILE
    // ============================================================

    public static void loadAccounts() {

        File file = new File(FILE_NAME);

        // If file does not exist, there is no previous data
        if (!file.exists()) {
            return;
        }

        try {

            // FileReader reads data from the file
            BufferedReader reader =
                    new BufferedReader(new FileReader(FILE_NAME));

            String line;

            // Read file line by line
            while ((line = reader.readLine()) != null) {

                // Split the line using |
                String[] data = line.split("\\|");

                // Make sure data contains 3 parts
                if (data.length == 3) {

                    String accountNumber = data[0];
                    String accountHolderName = data[1];
                    double balance = Double.parseDouble(data[2]);

                    // Create account object
                    Account account =
                            new Account(
                                    accountNumber,
                                    accountHolderName,
                                    balance
                            );

                    // Add account to ArrayList
                    accounts.add(account);
                }
            }

            // Close reader
            reader.close();

        } catch (IOException | NumberFormatException e) {

            System.out.println("Error while loading account data.");
        }
    }


    // ============================================================
    // CREATE ACCOUNT
    // ============================================================

    public static void createAccount() {

        System.out.println("\n===== Create New Account =====");

        // Take account number
        System.out.print("Enter Account Number: ");
        String accountNumber = input.nextLine();

        // Check whether account already exists
        if (findAccount(accountNumber) != null) {

            System.out.println("Account already exists.");
            return;
        }

        // Take account holder name
        System.out.print("Enter Account Holder Name: ");
        String name = input.nextLine();

        // Take initial deposit
        System.out.print("Enter Initial Deposit: ");
        double initialDeposit = input.nextDouble();
        input.nextLine();

        // Check initial deposit
        if (initialDeposit < 0) {

            System.out.println("Invalid initial deposit.");
            return;
        }

        // Create new account
        Account newAccount =
                new Account(
                        accountNumber,
                        name,
                        initialDeposit
                );

        // Add account to ArrayList
        accounts.add(newAccount);

        // Save account information to file
        saveAccounts();

        System.out.println("Account created successfully!");
        System.out.println("Account data has been saved.");
    }


    // ============================================================
    // DEPOSIT MONEY
    // ============================================================

    public static void depositMoney() {

        System.out.println("\n===== Deposit Money =====");

        System.out.print("Enter Account Number: ");
        String accountNumber = input.nextLine();

        // Find account
        Account account = findAccount(accountNumber);

        if (account == null) {

            System.out.println("Account not found.");
            return;
        }

        // Take deposit amount
        System.out.print("Enter Deposit Amount: ");
        double amount = input.nextDouble();
        input.nextLine();

        // Deposit money
        account.deposit(amount);

        // Save updated balance
        saveAccounts();
    }


    // ============================================================
    // WITHDRAW MONEY
    // ============================================================

    public static void withdrawMoney() {

        System.out.println("\n===== Withdraw Money =====");

        System.out.print("Enter Account Number: ");
        String accountNumber = input.nextLine();

        // Find account
        Account account = findAccount(accountNumber);

        if (account == null) {

            System.out.println("Account not found.");
            return;
        }

        // Take withdrawal amount
        System.out.print("Enter Withdraw Amount: ");
        double amount = input.nextDouble();
        input.nextLine();

        // Withdraw money
        boolean success = account.withdraw(amount);

        // Save only if withdrawal was successful
        if (success) {
            saveAccounts();
        }
    }


    // ============================================================
    // CHECK BALANCE
    // ============================================================

    public static void checkBalance() {

        System.out.println("\n===== Check Balance =====");

        System.out.print("Enter Account Number: ");
        String accountNumber = input.nextLine();

        // Find account
        Account account = findAccount(accountNumber);

        if (account == null) {

            System.out.println("Account not found.");
            return;
        }

        // Show account information
        System.out.println("Account Holder: "
                + account.getAccountHolderName());

        System.out.println("Current Balance: "
                + account.getBalance());
    }


    // ============================================================
    // ACCOUNT INFORMATION
    // ============================================================

    public static void accountInformation() {

        System.out.println("\n===== Account Information =====");

        System.out.print("Enter Account Number: ");
        String accountNumber = input.nextLine();

        // Find account
        Account account = findAccount(accountNumber);

        if (account == null) {

            System.out.println("Account not found.");
            return;
        }

        // Display account information
        account.displayAccountInfo();
    }


    // ============================================================
    // TRANSFER MONEY
    // ============================================================

    public static void transferMoney() {

        System.out.println("\n===== Transfer Money =====");

        // Sender account
        System.out.print("Enter Sender Account Number: ");
        String senderNumber = input.nextLine();

        Account sender = findAccount(senderNumber);

        if (sender == null) {

            System.out.println("Sender account not found.");
            return;
        }

        // Receiver account
        System.out.print("Enter Receiver Account Number: ");
        String receiverNumber = input.nextLine();

        Account receiver = findAccount(receiverNumber);

        if (receiver == null) {

            System.out.println("Receiver account not found.");
            return;
        }

        // Check same account
        if (senderNumber.equals(receiverNumber)) {

            System.out.println(
                    "Sender and receiver cannot be the same."
            );

            return;
        }

        // Take transfer amount
        System.out.print("Enter Transfer Amount: ");
        double amount = input.nextDouble();
        input.nextLine();

        // Validate amount
        if (amount <= 0) {

            System.out.println("Invalid transfer amount.");
            return;
        }

        // Check balance
        if (amount > sender.getBalance()) {

            System.out.println("Insufficient balance.");
            return;
        }

        /*
         * Directly change balance here so that
         * unnecessary messages are not displayed.
         */

        // Deduct money from sender
        sender.balance = sender.balance - amount;

        // Add money to receiver
        receiver.balance = receiver.balance + amount;

        // Save updated data
        saveAccounts();

        System.out.println("Transfer completed successfully.");
        System.out.println("Transferred Amount: " + amount);
        System.out.println(
                "Sender New Balance: " + sender.getBalance()
        );
    }


    // ============================================================
    // DISPLAY ALL ACCOUNTS
    // ============================================================

    public static void displayAllAccounts() {

        System.out.println("\n===== All Bank Accounts =====");

        // Check whether there are accounts
        if (accounts.isEmpty()) {

            System.out.println("No accounts available.");
            return;
        }

        // Display every account
        for (Account account : accounts) {

            account.displayAccountInfo();
        }
    }


    // ============================================================
    // MAIN METHOD
    // ============================================================

    public static void main(String[] args) {

        int choice;

        /*
         * First load old account data from file.
         * This means previously created accounts
         * will remain available after restarting
         * the program.
         */
        loadAccounts();

        // Welcome message
        System.out.println("======================================");
        System.out.println("       BANK MANAGEMENT SYSTEM");
        System.out.println("======================================");

        do {

            // Display main menu
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Create New Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. Account Information");
            System.out.println("6. Transfer Money");
            System.out.println("7. Display All Accounts");
            System.out.println("8. Exit");
            System.out.println("===============================");

            // Take user choice
            System.out.print("Enter your choice: ");
            choice = input.nextInt();
            input.nextLine();

            // Perform operation according to choice
            switch (choice) {

                case 1:
                    createAccount();
                    break;

                case 2:
                    depositMoney();
                    break;

                case 3:
                    withdrawMoney();
                    break;

                case 4:
                    checkBalance();
                    break;

                case 5:
                    accountInformation();
                    break;

                case 6:
                    transferMoney();
                    break;

                case 7:
                    displayAllAccounts();
                    break;

                case 8:
                    System.out.println(
                            "\nThank you for using Bank Management System."
                    );
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }

        } while (choice != 8);

        // Close scanner
        input.close();
    }
}