package com.example.demo;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import javafx.scene.control.ChoiceBox;

import java.sql.*;
import java.util.ArrayList;

public class Database {

    //Retrieves table data from the database
    //User defined table name
    //Must supply SQL command
    //opCode is a number indication which function to call after receiving data
    public static int getDatabaseData(String sql, String opCode, Login login_info, Connectable connectable){
        System.out.println("Starting Database Connection");

        //Display Database Connection Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
        alert.setContentText("Connecting to Database");

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            alert.show();

            String jdbcUrl = "jdbc:mysql://localhost:3306/School";
            String username = login_info.getUser_name(); //"root";
            String password = login_info.getPass(); //"12345678";

            connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connected to MySQL!");

            // Perform database operations using the connection...
            stmt = connection.createStatement();
            System.out.println("Executing statement: " + sql);
            //String sql = "SELECT * FROM "+ tableName + ";"; //SQL Command

            //stmt.execute - returns true if the first result is a ResultSet object;
            //false if it is an update count or there are no results
            if (stmt.execute(sql)) {
                rs = stmt.getResultSet();
                connectable.ProcessData(rs, opCode);
            }

            /*
            try {
                // Pause for 3 seconds (3000 milliseconds)
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

             */


            //Close the Alert Dialog
            //alert.close();
        } catch (CommunicationsException e) {
            //Failed at connection constructor
            connectable.getSchoolController().updateStatusTextFlow("Connection to database failed");
            //System.err.println("CommunicationsException: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the connection when done
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            //Close the Alert Dialog
            alert.close();
        }

        return 0;
    }

    public static void initialize_Section_ChoiceBox(ChoiceBox targetChoiceBox, ObservableList<Section> sectionList, ArrayList<String> sectionIdList) {
        System.out.println("About to clear enrolmentSectionChoiceBox");
        //CLear values already in ChoiceBox
        targetChoiceBox.getItems().clear(); //Calls the listener - treats this as change in selection in the box

        System.out.println("Cleared enrolmentSectionChoiceBox");
        if(sectionList.size() != 0) {
            for (Section section : sectionList) {
                targetChoiceBox.getItems().add("Id:" + section.getSectionId() +
                        " " + section.getSectionSubject() +
                        " " + section.getSectionInstructor() +
                        " " + section.getSectionDays() +
                        " Start:" + section.getSectionStartTime() +
                        " End:" + section.getSectionEndTime() +
                        " Room: " + section.getSectionRoom());
                sectionIdList.add(section.getSectionId()); //Keeps a separate list of section ids
            }
        }
    }
}

/* Enclose related database operations within a transaction
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionExample {
    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USERNAME = "your_username";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        Connection connection = null;

        try {
            // Connect to the database
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Disable auto-commit to start a transaction
            connection.setAutoCommit(false);

            try {
                // Perform related database operations within the transaction
                updateDataOperation1(connection);
                updateDataOperation2(connection);

                // If all operations are successful, commit the transaction
                connection.commit();
            } catch (SQLException e) {
                // If an error occurs, roll back the transaction
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Always close the connection in the finally block
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void updateDataOperation1(Connection connection) throws SQLException {
        // Perform the first database operation within the transaction
        String updateQuery = "UPDATE your_table SET column1 = value1 WHERE condition1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.executeUpdate();
        }
    }

    private static void updateDataOperation2(Connection connection) throws SQLException {
        // Perform the second database operation within the transaction
        String updateQuery = "UPDATE your_table SET column2 = value2 WHERE condition2";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.executeUpdate();
        }
    }
}
 */

/* Notes
When multiple users are concurrently modifying data in a database, a situation known as a "concurrency issue" or "race condition" may arise. There are various strategies and mechanisms employed to handle such scenarios and maintain data consistency. Here are some common approaches:

Locking:
Pessimistic Locking: This approach involves locking the data during the modification process, preventing other users from accessing or modifying the same data until the lock is released. This is typically implemented using explicit lock statements.
Optimistic Locking: Instead of locking the data during the entire modification process, this approach involves checking if the data has been modified by someone else before saving the changes. This can be done by comparing timestamps or version numbers. If a conflict is detected, appropriate actions can be taken (e.g., notifying the user, merging changes, or aborting the update).
Isolation Levels:
Databases often support different isolation levels, which define the degree to which transactions are isolated from each other. Isolation levels such as Read Uncommitted, Read Committed, Repeatable Read, and Serializable offer different trade-offs between performance and data consistency.
Transactions:
Enclose related database operations within a transaction. Transactions provide atomicity, consistency, isolation, and durability (ACID properties). If an error occurs during the transaction, the changes are rolled back, ensuring that the database remains in a consistent state.
Versioning:
Maintain version information for each record in the database. When a user modifies a record, the version is updated. Before committing changes, the system checks if the version in the database matches the version the user started with. If not, it indicates that another user has modified the data in the meantime.
Conflict Resolution:
Implement conflict resolution mechanisms to handle situations where multiple users attempt to modify the same data simultaneously. This may involve notifying users of conflicts and allowing them to resolve the differences manually or automatically merging changes when possible.
Auditing and Logging:
Keep detailed logs and audit trails of database activities. This helps in identifying when and by whom changes were made. In case of conflicts, logs can be useful for understanding the sequence of events.
 */

/*
Optimistic Locking

Using a version column is a common approach for optimistic locking in databases. The version column is incremented whenever a record is updated, allowing the system to detect whether the data has changed since it was last read. Here's an example using a version column:

Let's assume you have a table your_table with columns id, data, and version:

sql
Copy code
CREATE TABLE your_table (
    id INT PRIMARY KEY,
    data VARCHAR(255),
    version INT
);
Here's how you can perform optimistic locking using a version column:

sql
Copy code
-- Update the data with optimistic locking
UPDATE your_table
SET data = 'new_data', version = version + 1
WHERE id = 1 AND version = 1;
In this example:

id: The primary key of the record you want to update.
data: The data you want to set for the record.
version: The current version of the record.
Before executing the UPDATE statement, the system checks whether the version in the WHERE clause matches the version in the database. If they match, the update is allowed, and the version is incremented. If they don't match, it means that someone else has updated the record since it was last read, and the update won't take place.

This method helps prevent conflicts by ensuring that updates only occur if the data hasn't changed since it was last read. You can use this approach within your application's code or stored procedures to handle concurrent modifications.

Remember to adjust the table and column names based on your actual schema. Also, consider adding error handling and notifying users about conflicts in a real-world application.
 */