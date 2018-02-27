package eu.logicbomb.discord.commands.staff;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import eu.logicbomb.discord.Start;
import eu.logicbomb.discord.commands.admin.Shout;
import eu.logicbomb.discord.commands.admin.Shutdown;
import eu.logicbomb.discord.icommands.ICommand;
import eu.logicbomb.discord.listener.CommandListener;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

public class Help implements ICommand {
    MessageBuilder mb = new MessageBuilder();

    @Override
    public void run(String[] args, Message msg) {
        if (!isBotOrFake(msg.getAuthor())) {
            try {
                Set<Class<? extends ICommand>> allClasses = null;
                mb.append("```\n");
                mb.append("*** Die Hilfe ***");
                mb.append("```");
                mb.sendTo(msg.getChannel()).queue();
                if (isOwnerOrAdmin(msg)) {
                    User us = msg.getAuthor();
                    MessageBuilder mbA = new MessageBuilder();
                    mbA.append("```\n");
                    mbA.append("*** Die Hilfe ***");
                    mbA.append("```");
                    mbA.append("\n");

                    mbA.append("```");
                    mbA.append("\n");
                    mbA.append("-- Admin-Befehle --");
                    mbA.append("\n");
                    helpAdminOutput(mbA);
                    mbA.append("\n");
                    mbA.append("```");
                    us.openPrivateChannel().queue((channel) -> {
                        channel.sendMessage(mbA.build()).queue();
                    });
                    TimeUnit.MILLISECONDS.sleep(250);

                    mbA.clear();
                }
                mb.clear();

                TimeUnit.MILLISECONDS.sleep(250);

                mb.append("```");
                mb.append("\n");
                mb.append("-- Team-Befehle --");
                mb.append("\n");
                helpTeamOutput(mb);
                mb.append("```");
                mb.sendTo(msg.getChannel()).queue();
            }
            catch(InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void helpTeamOutput(MessageBuilder mb) {
        mb.append(CommandListener.PREFIX + Help.class.getSimpleName().toLowerCase() + "   " + Start.commandmap.get("help").whatDoYouDo());
        mb.append("\n");
        mb.append(CommandListener.PREFIX + Say.class.getSimpleName().toLowerCase() + "   " + Start.commandmap.get("say").whatDoYouDo());
        mb.append("\n");
        mb.append(CommandListener.PREFIX + LMGTFY.class.getSimpleName().toLowerCase() + "   " + Start.commandmap.get("lmgtfy").whatDoYouDo());
        mb.append("\n");
    }

    private void helpAdminOutput(MessageBuilder mbA) {
        mbA.append(CommandListener.PREFIX + Shout.class.getSimpleName().toLowerCase() + "   " + Start.commandmap.get("shout").whatDoYouDo());
        mbA.append("\n");
        mbA.append(CommandListener.PREFIX + Shutdown.class.getSimpleName().toLowerCase() + "   " + Start.commandmap.get("shutdown").whatDoYouDo());
        mbA.append("\n");
    }

    @Override
    public String whatDoYouDo() {
        return "Ich helfe Leuten";
    }

}
