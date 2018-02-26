package eu.logicbomb.discord.commands.staff;

import eu.logicbomb.discord.icommands.ICommand;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class Say implements ICommand {
    MessageBuilder mb = new MessageBuilder();

    @Override
    public void run(String[] args, Message msg) {
        if (!isBotOrFake(msg.getAuthor())) {
            mb.append("Ich werde von " + usernick(msg) + " gezwungen zu sagen: " + msg.getContentRaw()).sendTo(msg.getChannel()).queue();
            msg.delete().queue();
        }
    }

    private String usernick(Message msg) {
        return msg.getGuild().getMember(msg.getAuthor()).getNickname();
    }

    @Override
    public String whatDoYouDo() {
        return "Ich diene als Sprachrohr";
    }

}
