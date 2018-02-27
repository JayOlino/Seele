package eu.logicbomb.discord.listener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    DB     db     = null;
    String mainID;

    String chatID = "295963007769509888";
    String tID    = "370873772619857921";

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
                    Start.LOG.info(event.getMember().getEffectiveName() + " add Roll : " + role.getName());
                    if (db.insertUserToTrial(event.getMember().getUser().getIdLong())) {
                        MessageBuilder mb = new MessageBuilder();
                        SimpleDateFormat sdf = new SimpleDateFormat("EE dd.MM.yyyy' um 'HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);

                        // manipulate date
                        c.add(Calendar.HOUR, (24 * 7));
                        date = c.getTime();

                        mb.append("@everyone\n");
                        mb.append("Wir haben einen Neuen der unserem Wahnsinn standhalten will.\n");
                        mb.append("Sein Name lautet: " + event.getMember().getAsMention() + " und seine Probezeit sollte ungefähr am __**" + sdf.format(date) + "**__ enden.");
                        Message msg = mb.build();
                        TextChannel tx = guild.getTextChannelById(tID);
                        tx.sendMessage(msg).queue();
                    }
                }
                else if (role.getName().equals("Member MAIN")) {
                    MessageBuilder mb = new MessageBuilder();
                    mb.append("@everyone heute hat wiedereinmal jemand die Probezeit mit uns Überstanden. Herzlichen Glückwunsch " + event.getMember().getAsMention());
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
                    Start.LOG.info(event.getMember().getEffectiveName() + " remove Roll : " + role.getName());
                    db.deleteUserToTrial(event.getMember().getUser().getIdLong());
                }
            }

        }
    }
}
