package Server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/***
 * Creates a connection for the server to the database.
 *
 * Much of this code can be attributed to CAB302 Prac 7
 */
public class DBConnection {

    // The single instance
    private static Connection connection = null;

    // CREATE statement for database
    private static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS trading_platform;";

    // CREATE statement for tables
    private static final String CREATE_UNITS =
            "CREATE TABLE IF NOT EXISTS units (" +
                    "unit_id INT NOT NULL AUTO_INCREMENT UNIQUE," +
                    "unit_name VARCHAR(70) NOT NULL," +
                    "credits INT NOT NULL," +
                    "PRIMARY KEY (unit_id)" +
                    ");";
    private static final String CREATE_USERS =
            "CREATE TABLE IF NOT EXISTS users (" +
                    "user_name VARCHAR(30) NOT NULL UNIQUE," +
                    "first_name VARCHAR(30) NOT NULL," +
                    "last_name VARCHAR(30) NOT NULL," +
                    "email VARCHAR(60) NOT NULL," +
                    "admin_status BOOLEAN NOT NULL," +
                    "unit INT NOT NULL," +
                    "password VARCHAR(260) NOT NULL," +
                    "PRIMARY KEY (user_name)," +
                    "FOREIGN KEY (unit) REFERENCES units(unit_id)" +
                    ");";
    private static final String CREATE_ASSETS_PRO =
            "CREATE TABLE IF NOT EXISTS assets_produced (" +
                    "asset_id INT NOT NULL AUTO_INCREMENT UNIQUE," +
                    "asset_name VARCHAR(100) NOT NULL," +
                    "quantity INT NOT NULL," +
                    "unit INT NOT NULL," +
                    "PRIMARY KEY (asset_id)," +
                    "FOREIGN KEY (unit) REFERENCES units(unit_id)" +
                    ");";
    private static final String CREATE_TRADES =
            "CREATE TABLE IF NOT EXISTS trades (" +
                    "trade_id INT NOT NULL AUTO_INCREMENT," +
                    "type ENUM('buy', 'sell') NOT NULL," +
                    "user VARCHAR(30) NOT NULL," +
                    "asset INT NOT NULL," +
                    "quantity INT NOT NULL," +
                    "price INT NOT NULL," +
                    "date DATETIME NOT NULL," +
                    "PRIMARY KEY (trade_id)," +
                    "FOREIGN KEY (user) REFERENCES users(user_name)," +
                    "FOREIGN KEY (asset) REFERENCES assets_produced(asset_id)" +
                    ");";
    private static final String CREATE_TRADES_HX =
            "CREATE TABLE IF NOT EXISTS trade_history (" +
                    "trade_id INT NOT NULL AUTO_INCREMENT," +
                    "status ENUM('complete', 'cancelled') NOT NULL," +
                    "asset INT NOT NULL," +
                    "qty INT NOT NULL," +
                    "date DATETIME," +
                    "seller VARCHAR(30) NOT NULL," +
                    "buyer VARCHAR(30) NOT NULL," +
                    "PRIMARY KEY (trade_id)," +
                    "FOREIGN KEY (seller) REFERENCES users(user_name)," +
                    "FOREIGN KEY (buyer) REFERENCES users(user_name)" +
                    ");";
    private static final String CREATE_ASSETS_PUR =
            "CREATE TABLE IF NOT EXISTS assets_purchased (" +
                    "asset_id INT NOT NULL," +
                    "unit INT NOT NULL," +
                    "quantity INT NOT NULL," +
                    "credits INT NOT NULL," +
                    "PRIMARY KEY (asset_id, unit)," +
                    "FOREIGN KEY (asset_id) REFERENCES assets_produced(asset_id)," +
                    "FOREIGN KEY (unit) REFERENCES units(unit_id)" +
                    ");";

    // Store statements in an array
    private static final String[] createTables = {CREATE_UNITS, CREATE_USERS, CREATE_ASSETS_PRO, CREATE_TRADES,
            CREATE_TRADES_HX, CREATE_ASSETS_PUR};

    // CREATE statements for first uni (IT Admin) and user (root)
    private static final String ADD_FIRST_UNIT = "INSERT INTO units(unit_id, unit_name, credits) VALUES (?, ?, ?)";
    private static final String ADD_FIRST_ADMIN = "INSERT INTO users(user_name, first_name, last_name, email, " +
            "admin_status, unit, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

    // TODO: Need to have the addRoot method run only if it needs to
    // TODO: Note to self - instead of having a try-catch in the create database method, have it throw an exception
    // TODO: that stops the createTable method and addRoot methods from running
    /***
     * Constructor initialises connection, creates database and tables if necessary
     * @param path
     */
    private DBConnection(String path) {
        Properties props = new Properties();
        FileInputStream details = null;

        try {
            // Read in details for database connection
            details = new FileInputStream(path);
            props.load(details);
            details.close();

            String url = props.getProperty("jdbc.url");
            String user = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");

            // Connect to MariaDB IP
            connection = DriverManager.getConnection(url + "/", user, password);
            // Create database if needed
            createDatabase();
            // Change connection to the database
            connection.setCatalog("trading_platform");
            // Create tables if needed
            createTables();


        } catch (SQLException sqle) {
            System.err.println(sqle);
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /***
     * Helper method to execute the CREATE DATABASE statement
     */
    private void createDatabase() {
        try {
            Statement stmnt = connection.createStatement();
            stmnt.execute(CREATE_DATABASE);
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    /***
     * Helper method to execute the CREATE TABLE statements
     */
    private void createTables() {
        try {
            Statement stmnt = connection.createStatement();
            for(String create : createTables) {
                stmnt.execute(create);
            }
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    /***
     * Helper method to create an initial unit and user
     * @param org The first organisation to be added (IT Admin)
     * @param root The first user (root)
     */
//    private void addRoot(Units org, User root) {
//        try {
//            PreparedStatement addUnit = connection.prepareStatement(ADD_FIRST_UNIT);
//            PreparedStatement addRoot = connection.prepareStatement(ADD_FIRST_ADMIN);
//
//            // Add unit details
//            addUnit.setString(1, org.getUnitID());
//            addUnit.setString(2, org.getUnitName());
//            addUnit.setInt(3, org.getCredits());
//            addUnit.execute();
//            //Add user details
//            addRoot.setString(1, root.getUsername());
//            addRoot.setString(2, root.getFirstName());
//            addRoot.setString(3, root.getLastName());
//            addRoot.setString(4, root.getEmail());
//            addRoot.setBoolean(5, root.getAdminStatus());
//            addRoot.setString(6, org.getUnitID());
//            addRoot.setString(7, root.getHashedPassword());
//            addRoot.execute();
//        } catch (SQLException sqle) {
//            System.err.println(sqle);
//        }
//    }

    //TODO: At this stage, I'm not sure how this works as far as differences between client and server go
    //TODO: Clients will have to have their own .prods file so will need to suss that out later
    /***
     * Public method to create a connection to the database
     *
     * @param path The location of the .props file
     *
     * @return A conenction to the database
     */
    public static Connection getConnection(String path) {
        if (connection  == null) {
            new DBConnection(path);
        }
        return connection;
    }
}
