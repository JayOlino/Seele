package eu.logicbomb.discord.commands.staff;

import eu.logicbomb.discord.icommands.ICommand;
import eu.logicbomb.discord.listener.CommandListener;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class LMGTFY implements ICommand {
    MessageBuilder mb = new MessageBuilder();

    @Override
    public void run(String[] args, Message msg) {
        if (!isBotOrFake(msg.getAuthor())) {
            String mess = msg.getContentRaw().replaceFirst(CommandListener.PREFIX + getName().toLowerCase(), "");
            mess = mess.replaceAll(" ", "+");

            mb.append("Hier für dich: http://lmgtfy.com/?q=" + mess);
            mb.sendTo(msg.getChannel()).queue();
            //            mb.setEmbed()
        }
    }

    @Override
    public String whatDoYouDo() {
        return "Zeige den anderen wie man \"Googelt\"";
    }

}
