import java.sql.ResultSet;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;
import java.time.LocalDateTime;
public class NexoraBank {
    private static final String url = "jdbc:mysql://localhost:3306/nexora_bank"; // this is the url of upto "nexora_bank"  jdbc:mysql://localhost:3306/ this is same all time...
    private static final String password = "Sonu@2002"; // this is the password of  sql server which is created at the first time of install the server in pc.
    private static final String username = "root";
    // in method, we pass the  Scanner instance and Connection instance as a parameter.. bcz if we create those in every method...
    // then the number of instances in our program will be huge... and also all instance should be closed before wrap up the program
    // Create Transaction History
    private static void transactionHistory(Scanner sc , Connection connection)
    {
        try {
            System.out.print("Enter Account Number: ");
            String accountNumber = sc.nextLine();
            System.out.println();



            String query = "select * from transactions where from_account = ? or to_account = ? order by transaction_date desc";
            PreparedStatement transactionQuery = connection.prepareStatement(query);
            transactionQuery.setString(1,accountNumber);
            transactionQuery.setString(2,accountNumber);

            ResultSet resultSet = transactionQuery.executeQuery();
            boolean found = false;
            while(resultSet.next())
            {
                found = true;
                int transaction_ID = resultSet.getInt("transaction_ID");
                String from_Account = resultSet.getString("from_account");
                String to_Account = resultSet.getString("to_account");
                double amount = resultSet.getDouble("amount");
                String transaction_type = resultSet.getString("transaction_type");
                Timestamp timestamp = resultSet.getTimestamp("transaction_date");
                String transaction_status = resultSet.getString("transaction_status");


                System.out.println("****************** Transactions Details ********************");

                System.out.println("Transaction ID : "+ transaction_ID);
                System.out.println("From Account: "+ from_Account);
                System.out.println("To Account: "+ to_Account);
                System.out.println("Amount: "+ amount);
                System.out.println("Transaction Type: "+transaction_type);
                System.out.println("Transaction Time: "+ timestamp);
                System.out.println("Transaction Status: "+ transaction_status);
            }
            if(!found)
            {
                System.out.println("Transaction Not Found...");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
//Check Balance
    private static void checkBalance(Scanner sc, Connection connection)
    {
        try {
            System.out.print("Enter Account Number: ");
            String accountNumber = sc.nextLine();
            System.out.println();

            System.out.print("Enter PIN number: ");
            int pinNumber = sc.nextInt();
            sc.nextLine();
            System.out.println();

            String checkBalanceQuery = "select account_balance from accounts where account_number = ? and account_pin = ?";
            PreparedStatement checkBalanceStatement = connection.prepareStatement(checkBalanceQuery);

            checkBalanceStatement.setString(1,accountNumber);
            checkBalanceStatement.setInt(2,pinNumber);

            ResultSet resultSet = checkBalanceStatement.executeQuery();

            if(resultSet.next())
            {
                double balance =  resultSet.getDouble("account_balance");
                System.out.println("Current Balance : "+ balance);
            }
            else
            {
                System.out.println("Invalid Account Number or PIN");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

    }
    //Create Transfer Money
    private static void transferMoney(Scanner sc , Connection connection) throws SQLException {
        try {
            connection.setAutoCommit(false);
            System.out.print("Enter From Account (Sender) Number: ");
            String from_accountNumber = sc.nextLine();
                if(from_accountNumber.trim().isEmpty())
                {
                    System.out.println("Sender Account Number Cannot Be Empty....");
                    return;
                }
            System.out.println();

            System.out.print("Enter to Account (Receiver) Number: ");
            String to_accountNumber = sc.nextLine();
            //.trim()- means... if input is just a vacant space... then it will also invalid.. so it will also return...
            if(to_accountNumber.trim().isEmpty())
            {
                System.out.println("Receiver Account Number Cannot Be Empty....");
                return;
            }
            System.out.println();

            System.out.println("Enter Transfer Amount: ");
            double transfer_amount = sc.nextDouble();
            sc.nextLine();
            System.out.println();
            if(transfer_amount<=0)
            {
                System.out.println("Invalid Amount....");
                return;
            }

            if(from_accountNumber.equals(to_accountNumber))
            {
                System.out.println("Sender and Receiver Acount Cannot Be Same...");
                return;
            }
            String senderUpdateQuery = "select account_balance from accounts where account_number = ?";

            PreparedStatement senderStatement = connection.prepareStatement(senderUpdateQuery);
            senderStatement.setString(1,from_accountNumber);

            ResultSet senderResult = senderStatement.executeQuery();
            if(!senderResult.next())
            {
                System.out.println("Sender Account Not Found...");
                connection.rollback();
                return;
            }

            String receiverUpdateQuery = "select account_balance from accounts where account_number =?";
            PreparedStatement receiverStatement = connection.prepareStatement(receiverUpdateQuery);
            receiverStatement.setString(1,to_accountNumber);
            ResultSet receiverResult = receiverStatement.executeQuery();
            if(!receiverResult.next())
            {
                System.out.println("Receiver Account NOT Found...");
                connection.rollback();
                return;
            }

                double currentBalance = senderResult.getDouble("account_balance");
                if(currentBalance<transfer_amount)
                {
                    System.out.println("Insufficient Balance");
                    connection.rollback();
                    return;
                }

                    double newBalance = currentBalance-transfer_amount;
                    String updateQuery = "update accounts set account_balance = ? where account_number = ? ";

                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setDouble(1,newBalance);
                    updateStatement.setString(2,from_accountNumber);
                    int rowsAffected = updateStatement.executeUpdate();
                    if(rowsAffected<=0)
                    {
                        connection.rollback();
                        System.out.println("Sender Balance Update Failed..");
                        return;
                    }
                    System.out.println("Sender Balance Update Successfully...");


                double receiverBalance = receiverResult.getDouble("account_balance");
                double newReceiverBalance = receiverBalance+transfer_amount;
                String receiverSetQuery = "update accounts set account_balance = ? where account_number = ?";
                PreparedStatement receiverSetStatement = connection.prepareStatement(receiverSetQuery);
                receiverSetStatement.setDouble(1,newReceiverBalance);
                receiverSetStatement.setString(2, to_accountNumber);
                int receiverRowsAffected = receiverSetStatement.executeUpdate();
                if(receiverRowsAffected>0)
                {
                    String transactionQuery = "insert into transactions (from_account,to_account,amount,transaction_type,transaction_status) values(?,?,?,?,?)";
                    PreparedStatement transactionStatement = connection.prepareStatement(transactionQuery);
                    transactionStatement.setString(1,from_accountNumber);
                    transactionStatement.setString(2,to_accountNumber);
                    transactionStatement.setDouble(3,transfer_amount);
                    transactionStatement.setString(4,"Transfer");
                    transactionStatement.setString(5,"Success");

                    int transactionRows = transactionStatement.executeUpdate();
                    if(transactionRows>0)
                    {
                        connection.commit();
                        System.out.println("Transfer Complete Successfully...");
                    }
                    else
                    {
                        connection.rollback();
                        System.out.println("Transaction Failed...");
                    }
                }
                else
                {
                    connection.rollback();
                    System.out.println("Account Not Found");
                }
        }
        catch (SQLException e)
        {
            connection.rollback();
            System.out.println(e.getMessage());
        }

    }
    //Create Withdraw Money
    private static void withdrawMoney(Scanner sc, Connection connection) throws SQLException {
        try
        {
            connection.setAutoCommit(false);
            System.out.print("Enter Account Number: ");
            String accountNumber = sc.nextLine();
            System.out.println();

            System.out.print("Enter Withdraw Amount : ");
            double withdrawAmount = sc.nextDouble();
            sc.nextLine();
            System.out.println();
            if(withdrawAmount<=0)
            {
                System.out.println("Invalid Amount...");
                return;
            }

            String query = "select account_balance from accounts where account_number = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,accountNumber);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                double currentBalance = resultSet.getDouble("account_balance");
                if(currentBalance>=withdrawAmount)
                {
                    double newBalance = currentBalance-withdrawAmount;
                    String updateQuery = "update accounts set account_balance  = ? where account_number = ? ";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                    updateStatement.setDouble(1, newBalance);
                    updateStatement.setString(2,accountNumber);

                    int rowsAffected = updateStatement.executeUpdate();

                    if(rowsAffected> 0)
                    {
                            String transactionQuery = "insert into transactions(from_account,to_account,amount,transaction_type,transaction_status) "+
                                    "values(?,?,?,?,?) ";
                            PreparedStatement transactionStatement = connection.prepareStatement(transactionQuery);
                            transactionStatement.setString(1,accountNumber);
                            transactionStatement.setNull(2,Types.VARCHAR);
                            transactionStatement.setDouble(3,withdrawAmount);
                            transactionStatement.setString(4,"Withdrawn");
                            transactionStatement.setString(5,"Success");


                        int transactionRows = transactionStatement.executeUpdate();
                        if(transactionRows> 0 )
                        {
                            connection.commit();
                            System.out.println("Withdrawn Complete Successfully...");

                        }
                        else
                        {
                            connection.rollback();
                            System.out.println("Transaction Failed....");
                        }
                    }
                    else
                    {
                        connection.rollback();
                        System.out.println("Withdraw failed... ");
                    }

                }
                else
                {
                    System.out.println("Insufficient Balance...");
                    return;
                }
            }
            else
            {
                connection.rollback();
                System.out.println("Account Not Found...");
            }

        }
        catch (SQLException e)
        {
            connection.rollback();
            System.out.println(e.getMessage());
        }
    }
    // Create Deposit Money
    private static void depositMoney(Scanner sc, Connection connection) throws SQLException
    {
        try {
            connection.setAutoCommit(false); // in default it is always true.. the reason behind it- when we deposit money from a account to another account
            // then by any chance if the payment failed then money will be deducted from the 'from account' but not send to the 'to account' this problem will come.
            // by sovling this problem I use commit and rollback through out the transaction process...
            System.out.print("Enter Account Number: ");
            String accountNumber = sc.nextLine();
            System.out.println();
            System.out.print("Deposit Amount: ");
            double depositAmount = sc.nextDouble();
            sc.nextLine();
            System.out.println();
            if(depositAmount<=0)
            {
                System.out.println("Invalid Amount...");
                return;
            }
            // Take the input from the users(account number and deposit amount) for write the sql query..
                String query = "select account_balance from accounts where account_number = ?"; // this is the SQL query.
            // it selects the account_balance column from the accounts table.. and matching the exact column by account number
                PreparedStatement preparedStatement = connection.prepareStatement(query); //PreparedStatement = SQL query + ? placeholder + setXXX() + execute()
            // PreparedStatement is a interface , it is used to create and execute the sql query..
            // here '?' mark is placeholder,, we can put value using these ? marks,,,
                preparedStatement.setString(1, accountNumber); // first ? means 1, second ? means 2....... upto 'n'
                ResultSet resultset = preparedStatement.executeQuery();// Result is used to hold the data comes form database and showing those data one by one row

                if (resultset.next()) // resultset.next() -- it helps to go to every row to hold the data
                {
                    double currentBalance = resultset.getDouble("account_balance");
                    double newBalance = currentBalance + depositAmount;
                    String updateQuery = "update accounts set account_balance = ? where account_number = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setDouble(1, newBalance);
                        updateStatement.setString(2, accountNumber);
                        int rowsAffected = updateStatement.executeUpdate();
                        if (rowsAffected > 0)
                        {
                            String transactionQuery = "insert into transactions (to_account,amount,transaction_type,transaction_status,from_account) values (?,?,?,?,?)";
                            PreparedStatement transactionStatement = connection.prepareStatement(transactionQuery);
                            transactionStatement.setString(1, accountNumber);
                            transactionStatement.setDouble(2, depositAmount);
                            transactionStatement.setString(3, "DEPOSIT");
                            transactionStatement.setString(4, "SUCCESS");
                            transactionStatement.setNull(5, Types.VARCHAR);
                            int transactionRows = transactionStatement.executeUpdate();
                            if(transactionRows>0)
                            {
                                connection.commit();
                                System.out.println("Deposit Complete Successfully");
                            }
                            else
                            {
                                connection.rollback();
                                System.out.println("Transaction Failed...");
                            }
                        }
                        else
                        {
                            connection.rollback();
                            System.out.println("Deposit Failed...");
                        }
                }
                else
                {
                    connection.rollback();
                    System.out.println("Account NOT Found....");
                }
        }
        catch(SQLException e)
        {
            connection.rollback();
            System.out.println(e.getMessage());
        }
    }
    // Create Account Table
    private static void createAccount(Scanner sc, Connection connection)
    {
        try
        {
            // Take Inputs
            System.out.print("Enter Customer ID: ");
            int customerId = sc.nextInt();
            sc.nextLine();

//            System.out.print("Enter Account Number: ");
//            String accountNumber = sc.nextLine();
//            System.out.println();
            String accountNumber = "";
            int currentYear = LocalDate.now().getYear();
            String customerCheckQuery = "select customer_ID from customers where customer_ID = ?";
            String customerIdFormatted = String.format("%3d",customerId); // customer id is formatted to 3 digits .. if customer id is of 1 digit.. then it will have 2 zeroes before the digit...
            String countAccountQuery = "select count(*) from accounts where customer_id = ?";
            PreparedStatement countStatement = connection.prepareStatement(countAccountQuery);
            countStatement.setInt(1,customerId);

            ResultSet countResultSet = countStatement.executeQuery();
            if(countResultSet.next())
            {
                int accCount = countResultSet.getInt(1);
                int accountSequence = accCount+1;
                String sequenceFormatted = String.format("%03d",accountSequence);
                accountNumber = "NXRBNK"+currentYear+customerIdFormatted+sequenceFormatted;
            }
            System.out.print("Enter Account Type: ");
            String accountType = sc.nextLine();
            System.out.println();
            System.out.print("Enter Account Balance: ");
            double accountBalance = sc.nextDouble();
            System.out.println();
            sc.nextLine();
            System.out.print("Enter Account Pin: ");
            int accountPin = sc.nextInt();
            System.out.println();
            sc.nextLine();
            System.out.print("Enter Account Status: ");
            String accountStatus = sc.nextLine();
            System.out.println();
            String query = "insert into accounts (customer_ID,account_number, account_type,account_balance,account_pin,account_status) values(?,?,?,?,?,?)";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query))
            {
                preparedStatement.setInt(1,customerId);
                preparedStatement.setString(2,accountNumber);
                preparedStatement.setString(3,accountType);
                preparedStatement.setDouble(4,accountBalance);
                preparedStatement.setInt(5, accountPin);
                preparedStatement.setString(6,accountStatus);
                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected>0)
                {
                    System.out.println("Data Inserted Successfully....");
                }
                else
                {
                    System.out.println("Data Not Inserted Successfully...");
                }
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    //  Create Customer  Table
    private static void createCustomer(Scanner sc , Connection connection)
    {
        try {
            // Take INPUTS
            System.out.print("Enter Customer's Name: ");
            String name = sc.nextLine();
            System.out.println();
            System.out.print("Enter Customer's Email ID : ");
            String email = sc.nextLine();
            System.out.println();
            System.out.print("Enter Customer's Phone number: ");
            String phone = sc.nextLine();
            System.out.println();
            System.out.print("Enter Customer's Address: ");
            String address = sc.nextLine();
            System.out.println();
            String query = "insert into customers (customer_Name,customer_email,customer_Phone,customer_Address) values (?,?,?,?)";
            try( PreparedStatement preparedStatement = connection.prepareStatement(query))
            {
                preparedStatement.setString(1,name);
                preparedStatement.setString(2,email);
                preparedStatement.setString(3,phone);
                preparedStatement.setString(4,address);

                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected>0)
                {
                    System.out.println("Data Inserted Successfully...");
                }
                else
                {
                    System.out.println("Data NoT inserted ...");
                }
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database Connected Successfully....");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
//        System.out.println("************************* Enter Details for Create Customer ********************");
//        createCustomer(sc,connection);
//        System.out.println("******************* Enter details for Create Account *********************");
//        createAccount(sc,connection);
        System.out.println("**************************** Enter details for deposit Money ********************");
        depositMoney(sc, connection);
//        System.out.println("************** Enter Details for Money Withdraw **************");
//        withdrawMoney(sc,connection);
        System.out.println("************************ Enter Details for Transfer Money ******************");
        transferMoney(sc,connection);
//
//        System.out.println("**************** Enter Details For Check Balance *****************");
//        checkBalance(sc,connection);

//            System.out.println("****************** Enter Details For  check Transaction History *******************");
//        transactionHistory(sc,connection);
    }
}
