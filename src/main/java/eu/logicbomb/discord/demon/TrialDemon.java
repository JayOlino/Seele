package eu.logicbomb.discord.demon;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.logicbomb.discord.Start;
import eu.logicbomb.discord.database.DB;
import eu.logicbomb.discord.listener.GuildListener;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class TrialDemon implements Runnable {

    public static Logger LOG            = LoggerFactory.getLogger("TrialDemon");
    DB                   db             = null;
    int                  trialCheckDays = 1;

    public TrialDemon(DB db) throws SQLException {
        this.db = db;
        if (this.db.getCon() != null) {
            run();
        }
        else {
            LOG.info("Trialdemon init fehlgeschlagen....");
        }
    }

    private void checkTrialAfter() throws SQLException {
        LOG.info("Start check after " + trialCheckDays + "Days");
        HashMap<Long, Long> map = db.selectTrial();
        if (map != null) {
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Long, Long> trial = (Map.Entry) it.next();
                MessageBuilder mb = new MessageBuilder();
                User u = null;
                Guild g = null;
                Date start = new Date(trial.getKey());
                Date end = GuildListener.getFinishDate(trial.getKey());
                u = Start.jda.getUserById("" + trial.getValue());
                g = Start.jda.getGuildById(Start.properties.getProperty("BotServerID"));

                int hours = Hours.hoursBetween(new DateTime(start.getTime()), new DateTime(end.getTime())).getHours();

                writeMSG(mb, u, g, hours);

            }
        }

    }

    private void writeMSG(MessageBuilder mb, User u, Guild g, int hours) {
        TextChannel tx = g.getTextChannelById(GuildListener.tID);
        String mem = g.getMember(u).getNickname() != null ? g.getMember(u).getEffectiveName() : g.getMember(u).getNickname();
        if (hours < 0) {
            mb.append("Trial für den User:" + mem + "ist " + hours + "h überfällig");
        }
        else if (hours <= 24) {
            mb.append("Trial von User" + mem + " ");

        }
        tx.sendMessage(mb.build());
        mb.clear();
    }

    @Override
    public void run() {
        LOG.info("----------> ### Start Demon ### <----------");
        int i = 1;
        while (true) {
            try {
                LOG.info(" starte Check NR." + i);
                checkTrialAfter();
                i++;
                TimeUnit.DAYS.sleep(trialCheckDays);
            }
            catch(Exception e) {
                LOG.error("### ERROR ###", e);
            }
        }

    }
}
