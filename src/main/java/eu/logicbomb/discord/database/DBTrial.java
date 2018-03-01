package eu.logicbomb.discord.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import eu.logicbomb.discord.Start;
import eu.logicbomb.discord.listener.GuildListener;

public class DBTrial {

    public static boolean insertUserToTrial(Connection con, String statement, long id) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, id);
            ps.setLong(2, System.currentTimeMillis());
            ps.executeUpdate();
            return true;
        }
        catch(Exception e1) {
            Start.LOG.error("###ERROR###", e1);
        }
        finally {
            try {
                ps.close();
            }
            catch(Exception e) {
                Start.LOG.error("###ERROR###", e);
            }
        }
        return false;
    }

    public static boolean deleteUserToTrial(Connection con, String statement, long id) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(statement);
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
                Start.LOG.error("###ERROR###", e);
                Start.LOG.info("Kein User angekommen");
            }
        }
        return false;
    }

    public static HashMap<Long, Long> selectTrial(Connection con, String statement) {
        HashMap<Long, Long> map = new HashMap<>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection coni = con) {
            ps = coni.prepareStatement(statement);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getLong(1), rs.getLong(2));
            }

        }
        catch(Exception e) {
            Start.LOG.error("###ERROR###", e);
            return null;
        }
        return map;
    }

    public static boolean addOneWeekToTrial(Connection con, String statement, long id) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(statement);
            ps.setLong(1, GuildListener.getFinishDate().getTime());
            ps.setLong(2, id);

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
                Start.LOG.error("###ERROR###", e);
            }
        }
        return false;
    }

}
