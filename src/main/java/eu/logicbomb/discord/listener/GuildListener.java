package eu.logicbomb.discord.listener;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.logicbomb.discord.Start;
import eu.logicbomb.discord.database.DB;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildListener extends ListenerAdapter {
    public static Logger LOG    = LoggerFactory.getLogger("GuildListenerLog");

    DB                   db     = null;
    /**
     * MAIN-ServerID 
     */
    static String        mainID;
    /**
     * Main-ChatID 
     */
    static String        chatID = "295963007769509888";

    /**
     * LeitungsChannelID 
     */
    public static String tID    = "370873772619857921";

    public GuildListener(DB db, String mainID) {
        this.db = db;
        this.mainID = mainID;
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        super.onGuildMemberRoleAdd(event);
        Guild guild = event.getGuild();

        if (guild.getId().equals(mainID)) {
            for (Role role : event.getRoles()) {
                if (role.getName().equals("Main-Probe")) {
                    LOG.info(event.getMember().getEffectiveName() + " add Roll : " + role.getName());
                    try {

                        if (db.insertTrial(event.getMember().getUser().getIdLong())) {
                            MessageBuilder mb = new MessageBuilder();

                            mb.append("@everyone\n");
                            mb.append("Wir haben einen Neuen der unserem Wahnsinn standhalten will.\n");
                            mb.append("Sein Name lautet: " + event.getMember().getAsMention() + " und seine Probezeit sollte ungef�hr am __**" + Start.sdf.format(getFinishDate()) + "**__ enden.");
                            Message msg = mb.build();
                            TextChannel tx = guild.getTextChannelById(tID);
                            tx.sendMessage(msg).queue();
                        }
                    }
                    catch(Exception e) {
                        LOG.error("###ERROR###", e);
                    }
                }
                else if (role.getName().equals("Member MAIN")) {
                    MessageBuilder mb = new MessageBuilder();
                    mb.append("@everyone heute hat wiedereinmal jemand die Probezeit mit uns �berstanden. Herzlichen Gl�ckwunsch " + event.getMember().getAsMention());
                    Message msg = mb.build();
                    TextChannel tx = guild.getTextChannelById(chatID);
                    tx.sendMessage(msg).queue();
                }
            }

        }
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        super.onGuildMemberRoleRemove(event);
        Guild guild = event.getGuild();

        if (guild.getId().equals(mainID)) {
            for (Role role : event.getRoles()) {

                if (role.getName().equals("Main-Probe")) {
                    LOG.info(event.getMember().getEffectiveName() + " remove Roll : " + role.getName());
                    try {
                        db.deleteTrial(event.getMember().getUser().getIdLong());
                    }
                    catch(SQLException e) {
                        LOG.error("###ERROR###", e);
                    }
                }
            }

        }
    }

    public static Date getFinishDate() {
        Date date = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // manipulate date
        c.add(Calendar.HOUR, (24 * 7));
        date = c.getTime();
        return date;
    }

    public static Date getFinishDate(long l) {
        Date date = new Date(l);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // manipulate date
        c.add(Calendar.HOUR, (24 * 7));
        date = c.getTime();
        return date;
    }
}
