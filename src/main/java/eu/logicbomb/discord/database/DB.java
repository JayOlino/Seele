package eu.logicbomb.discord.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import eu.logicbomb.discord.Start;

public class DB {
    String     host;
    String     database;
    String     port;
    String     user;
    String     password;

    Connection con;

    Properties statements = new Properties();

    public DB(Properties properties) {
        this.host = properties.getProperty("BotDBHost");
        this.database = properties.getProperty("BotDB");
        this.port = properties.getProperty("BotDBPort");
        this.user = properties.getProperty("BotDBUser");
        this.password = properties.getProperty("BotDBPW");

        if (host.equalsIgnoreCase("-") || database.equalsIgnoreCase("-") ||
                port.equalsIgnoreCase("-") || user.equalsIgnoreCase("-") || password.equalsIgnoreCase("-")) {
            Start.LOG.info("DB-Parameter nicht gefüllt, Datenbank betrieb nicht möglich.");
        }
        else {
            Start.LOG.info("DB-Parameter gefüllt, Datenbank betrieb möglich.");
            initConnection();
        }

    }

    private void fillProperties() throws Exception {
        try (InputStream resourceStream = DB.class.getResourceAsStream("/resources/eu/logicbomb/discord/database/DB.properties")) {
            statements.load(resourceStream);
        }
    }

    private boolean checkforTables() {
        boolean found = false;
        PreparedStatement ps = null;
        try {
            ps = getCon().prepareStatement(statements.getProperty("checkUp"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Start.LOG.info("Tabelle gefunden, Datenbank existiert");
                found = true;
            }
        }
        catch(Exception e1) {
            found = false;
        }
        finally {
            try {
                ps.close();
            }
            catch(Exception e) {
                Start.LOG.error("###ERROR###", e);
            }
        }
        return found;
    }

    private void initConnection() {
        try {
            fillProperties();

            setCon(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password));
            if (!checkforTables()) {
                createDB();
            }

        }
        catch(Exception e) {
            Start.LOG.error("###ERROR###", e);
        }

    }

    private void createDB() {
        String sqlCreate = statements.getProperty("createTrial");
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute(sqlCreate);

        }
        catch(SQLException e) {
            Start.LOG.error("###ERROR###", e);
        }
        finally {
            try {
                stmt.close();
            }
            catch(Exception e) {
                Start.LOG.error("###ERROR###", e);
            }
        }
    }

    public boolean insertTrial(long id) throws SQLException {
        return DBTrial.insertUserToTrial(getCon(), statements.getProperty("insertTrial"), id);
    }

    public boolean deleteTrial(long id) throws SQLException {
        return DBTrial.deleteUserToTrial(getCon(), statements.getProperty("deleteTrial"), id);
    }

    public HashMap<Long, Long> selectTrial() throws SQLException {
        return DBTrial.selectTrial(getCon(), statements.getProperty("selectallTrial"));
    }

    public Connection getCon() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
    }

    public void setCon(Connection con) {
        this.con = con;
    }

}
