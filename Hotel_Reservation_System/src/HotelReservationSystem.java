import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_mannat";
    private static final String password = "Sonu@2002";
    private static final String username = "root";
    public static void main(String[] args) throws ClassNotFoundException, SQLException
    {
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");

            }
            catch(ClassNotFoundException e)
            {
                System.out.println(e.getMessage());
            }
            try
            {
                Connection connection = DriverManager.getConnection(url,username,password);
                while(true)
                {
                    System.out.println();
                    System.out.println("******************- Welcome to HOTEL MANNAT Management System -******************");
                    Scanner sc = new Scanner(System.in);
                    System.out.println("1.Reserve a room");
                    System.out.println("2.View Reservations");
                    System.out.println("3.Get Room Number");
                    System.out.println("4.Update Reservations");
                    System.out.println("5.Delete Reservations");
                    System.out.println("0.Exit");
                    System.out.print("Select a valid option: ");
                    int choice = sc.nextInt();
                    System.out.println();
                    switch (choice)
                    {
                        case 1:
                            reserveRoom(connection,sc);
                            break;
                        case 2:
                            viewReservations(connection);
                            break;
                        case 3:
                            getRoomNumber(connection,sc);
                            break;
                        case 4:
                            updateReservation(connection,sc);
                            break;
                        case 5:
                            deleteReservation(connection,sc);
                            break;
                        case 0:
                            exit();
                            sc.close();
                            return;
                        default:
                            System.out.println("Invalid Choice. Try again...");
                    }
                }
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
            catch(InterruptedException e)
            {
                throw new RuntimeException(e);
            }
    }
    public static void exit() throws InterruptedException
    {
        System.out.print("Exit the program");
        for(int i = 0 ; i<5; i++)
        {
            System.out.print(".");
            Thread.sleep(500);
        }
        System.out.println();
    }
    private static void updateReservation(Connection connection, Scanner sc) {

        try
        {
            System.out.print("Enter Reservation ID for update: ");
            int reservationID = sc.nextInt();
            System.out.println();

            if(!reservationExists(connection,reservationID))
            {
                System.out.println("Reservation NOT found for given data...");
                return;
            }

            System.out.print("Enter new guestName: ");
            String newGuestName = sc.next();
            sc.nextLine();
            System.out.println();

            System.out.print("Enter new Room Number: ");
            String newRoomNumber = sc.next();
            sc.nextLine();
            System.out.println();

            System.out.print("Enter new Contact number: ");
            String newContactNumber = sc.next();
            sc.nextLine();
            System.out.println();


            String query = "update reservations set GUEST_NAME = "+newGuestName +","+
                    "ROOM_NUMBER = "+newRoomNumber +"," + "CONTACT_NUMBER = "+ newContactNumber +","+"where RESERVATION_ID = "+reservationID;


            try(Statement statement = connection.createStatement())
            {
                int affectedRows = statement.executeUpdate(query);

                if(affectedRows>0)
                {
                    System.out.println("Data Updated Successfully....");
                }
                else {
                    System.out.println("Data Not Updated...");
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection connection, Scanner sc) {

        try
        {
            System.out.print("Enter Reservation ID to Delete : ");
            int reservationID = sc.nextInt();
            System.out.println();

            if(!reservationExists(connection,reservationID))
            {
                System.out.println("Reservation NOT found for given data...");
                return;
            }
                String query = "delete from reservations where RESERVATION_ID ="+reservationID;
            try(Statement statement = connection.createStatement())
            {
                int affectedRows = statement.executeUpdate(query);
                if(affectedRows>0)
                {
                    System.out.println("Deleted Successfully...");
                }
                else
                {
                    System.out.println("Data NOT Deleted....");
                }

            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection connection, int reservationID) {
        try
        {
            String query = "select RESERVATION_ID from reservations where RESERVATION_ID = "+reservationID;
            try(Statement statement = connection.createStatement())
            {
                ResultSet resultSet = statement.executeQuery(query);
                return resultSet.next();
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return  false;
        }
    }

    private static void getRoomNumber(Connection connection, Scanner sc) {
    try{
        System.out.print("Enter Reservation ID: ");
        int reservationId = sc.nextInt();
            sc.nextLine();
        System.out.println();

        System.out.print("Enter Guest Name: ");
        String guestName = sc.nextLine();

        System.out.println();

        String query = "select ROOM_NUMBER from reservations "+
                "where RESERVATION_ID = "+reservationId+" AND GUEST_NAME = '"+guestName+"'" ;

            try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)){
                if(resultSet.next())
                {
                    int roomNumber = resultSet.getInt("ROOM_NUMBER");
                    System.out.println("Room Number of "+guestName+" is : "+roomNumber+" and the ID : "+reservationId);
                }
                else
                {
                    System.out.println("Reservation NOT FOUND!!!!!!!!!");
                }
            }


    }
    catch(SQLException e)
    {
        e.printStackTrace();
    }
    }

    private static void viewReservations(Connection connection) throws SQLException {
        String query = "select * from reservations";
        try(Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query))
        {
            System.out.println("************** Current Reservations **************");
            while(resultSet.next())
            {
                int reservationId = resultSet.getInt("RESERVATION_ID");
                String guestName = resultSet.getString("GUEST_NAME");
                int roomNumber = resultSet.getInt("ROOM_NUMBER");
                String contactNumber = resultSet.getString("CONTACT_NUMBER");
                String reservationTime = resultSet.getTimestamp("RESERVATION_DATE").toString();

                System.out.println("Reservation ID : "+reservationId);
                System.out.println("Guest Name: "+guestName);
                System.out.println("Room Number: "+roomNumber);
                System.out.println("Contact Number: "+contactNumber);
                System.out.println("Reservation Time: "+ reservationTime);
            }
        }
    }

    private static void reserveRoom(Connection connection, Scanner sc) {
        try{
            sc.nextLine();
            System.out.print("Enter guest name: ");
            String guestName = sc.nextLine();



            System.out.print("Enter room number: ");
            int roomNumber = sc.nextInt();
            sc.nextLine();
            System.out.println();

            System.out.print("Enter contact number: ");
            String contactNumber = sc.nextLine();

            System.out.println();
            String sql = "INSERT INTO reservations (GUEST_NAME,ROOM_NUMBER, CONTACT_NUMBER) " +
                    "values ('"+ guestName +"', "+roomNumber+",'"+ contactNumber +"')";


            String sql2 = "INSERT INTO reservations (GUEST_NAME, ROOM_NUMBER, CONTACT_NUMBER) " +
                    "VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";
            try (Statement statement = connection.createStatement())
            {
                int affectedRows = statement.executeUpdate(sql);
                if(affectedRows>0)
                {
                    System.out.println("Reservation Successfull....");
                }
                else
                {
                    System.out.println("Reservation Failed...");
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
