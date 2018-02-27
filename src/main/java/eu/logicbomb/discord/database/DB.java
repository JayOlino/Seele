package eu.logicbomb.discord.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            System.out.println("DB-Parameter nicht gefüllt, Datenbank betrieb nicht möglich.");
        }
        else {
            Start.LOG.info("DB-Parameter gefüllt, Datenbank betrieb möglich.");
            System.out.println("DB-Parameter gefüllt, Datenbank betrieb möglich.");
            initConnection();
        }

    }

    private void fillProperties() throws Exception {
        try (InputStream resourceStream = DB.class.getResourceAsStream("/eu/logicbomb/discord/database/DB.properties")) {
            statements.load(resourceStream);
        }
    }

    private boolean checkforTables() {
        boolean found = false;
        PreparedStatement ps = null;
        try {
            ps = this.con.prepareStatement(statements.getProperty("checkUp"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Start.LOG.info("Tabelle gefunden, Datenbank existiert");
                System.out.println("Tabelle gefunden, Datenbank existiert");
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
                Start.LOG.error("###EXCEPTION###", e);
            }
        }
        return found;
    }

    private void initConnection() {
        try {
            fillProperties();
            Connection dbConnection = null;

            dbConnection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
            this.con = dbConnection;
            if (!checkforTables()) {
                createDB();
            }

        }
        catch(Exception e) {
            Start.LOG.error("###EXCEPTION###", e);
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
            Start.LOG.error("###EXCEPTION###", e);
        }
        finally {
            try {
                stmt.close();
            }
            catch(Exception e) {
                Start.LOG.error("###EXCEPTION###", e);
            }
        }
    }

    public boolean insertUserToTrial(long id) {
        PreparedStatement ps = null;
        try {
            ps = this.con.prepareStatement(statements.getProperty("insertTrial"), Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, id);
            ps.setLong(2, System.currentTimeMillis());
            ps.executeUpdate();
            return true;
        }
        catch(Exception e1) {
        }
        finally {
            try {
                ps.close();
            }
            catch(Exception e) {
                Start.LOG.error("###EXCEPTION###", e);
            }
        }
        return false;
    }

    public boolean deleteUserToTrial(long id) {
        PreparedStatement ps = null;
        try {
            ps = this.con.prepareStatement(statements.getProperty("deleteTrial"));
            ps.setLong(1, id);
            ps.executeUpdate();
            return true;
        }
        catch(Exception e1) {
        }
        finally {
            try {
                ps.close();
            }
            catch(Exception e) {
                Start.LOG.error("###EXCEPTION###", e);
            }
        }
        return false;
    }

}
