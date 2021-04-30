package Server;

import Client.Units;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/***
 * A wrapper class for accessing the units table
 */
public class UnitDBSource {
    // SELECT statements
    private static final String SELECT = "SELECT * from UNITS;";
    private static final String GET_UNIT = "SELECT * FROM units WHERE unit_id=?";
    private static final String GET_UNIT_BY_NAME = "SELECT * FROM units WHERE unit_name=?;";
    private static final String UPDATE = "UPDATE units SET unit_name = ?, credits = ? WHERE unit_id = ?;";

    // Prepared statements
    private PreparedStatement select;
    private PreparedStatement getUnit;
    private PreparedStatement update;
    private PreparedStatement getUnitByName;

    private Connection connection;

    /***
     * Constructor
     *
     * @param connection A connection to the trading_platform database
     */
    public UnitDBSource(Connection connection) {
        this.connection = connection;
        try{
            select = connection.prepareStatement(SELECT);
            getUnit = connection.prepareStatement(GET_UNIT);
            getUnitByName = connection.prepareStatement(GET_UNIT_BY_NAME);
            update = connection.prepareStatement(UPDATE);
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    /***
     * Retrieves the details of a unit based on its id from the database and creates an instance of that class
     * @param id The unit id as a String
     * @return An instance of the Units class
     */
    public Units getUnit(int id) {
        Units unit = new Units();
        ResultSet rs = null;

        try {
            getUnit.setInt(1, id);
            rs = getUnit.executeQuery();
            rs.next();

            unit.setUnitID(rs.getInt("unit_id"));
            unit.setUnitName(rs.getString("unit_name"));
            unit.setCredits(rs.getInt("credits"));
        } catch(SQLException sqle){
            System.err.println(sqle);
        }

        return unit;
    }

    /**
     * Update some (or all) of the details of a unit (except the unit_id)
     *
     * @param unit The unit to be updated
     */
    public void update(Units unit) {
        try{
            update.setString(1, unit.getUnitName());
            update.setInt(2, unit.getCredits());
            update.setInt(3, unit.getUnitID());
            update.execute();
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    public String[] getUnitNames(){
        ArrayList<String> names = new ArrayList<String>();
        ResultSet rs = null;

        try {
            rs = select.executeQuery();
            while (rs.next()) {
                names.add(rs.getString("unit_name"));
            }
        } catch(SQLException sqle){
            System.err.println(sqle);
        }

        return names.toArray(new String[0]);
    }


    /**
     * Get unit based on its name
     *
     * @param name The name of the unit
     * @return An instance of the Units class
     */
    public Units getUnit(String name) {
        Units unit = new Units();
        ResultSet rs = null;

        try {
            getUnitByName.setString(1, name);
            rs = getUnitByName.executeQuery();
            rs.next();

            unit.setUnitID(rs.getInt("asset_id"));
            unit.setUnitName(rs.getString("asset_name"));
            unit.setCredits(rs.getInt("quantity"));
        } catch(SQLException sqle){
            System.err.println(sqle);
        }

        return unit;
    }
}
