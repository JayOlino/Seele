package eu.logicbomb.discord.commands.staff;

import eu.logicbomb.discord.icommands.ICommand;
import eu.logicbomb.discord.listener.CommandListener;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class Say implements ICommand {
    MessageBuilder mb = new MessageBuilder();

    @Override
    public void run(String[] args, Message msg) {
        if (!isBotOrFake(msg.getAuthor())) {
            if (isOwnerOrAdmin(msg)) {
                mb.append(msg.getContentRaw().replaceFirst(CommandListener.PREFIX + "say ", "")).sendTo(msg.getChannel()).queue();
            }
            else {
                mb.append("Ich werde von " + usernick(msg) + " gezwungen zu sagen: " + msg.getContentRaw().replaceFirst(CommandListener.PREFIX + "say ", "")).sendTo(msg.getChannel()).queue();
            }
            msg.delete().queue();
            mb.clear();
        }
    }

    private String usernick(Message msg) {
        return msg.getGuild().getMember(msg.getAuthor()).getNickname();
    }

    @Override
    public String whatDoYouDo() {
        return "Ich diene als Sprachrohr für die User";
    }

}
