package eu.logicbomb.discord.commands.admin;

import eu.logicbomb.discord.icommands.ICommand;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class Shout implements ICommand {
    MessageBuilder mb = new MessageBuilder();

    @Override
    public void run(String[] args, Message msg) {
        if (!isBotOrFake(msg.getAuthor())) {
            String id = args[1].replaceFirst("<#", "");
            id = id.replaceFirst(">", "").trim();
            TextChannel tx = msg.getGuild().getTextChannelById(id);
            mb.append(msg.getContentRaw().substring(msg.getContentRaw().indexOf(">") + 1)).sendTo(tx).queue();
            mb.clear();
        }
    }

    @Override
    public String whatDoYouDo() {
        return "Ich diene als Sprachrohr irgendwann";
    }

}
