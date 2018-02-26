package eu.logicbomb.discord.commands.alluser;

import eu.logicbomb.discord.icommands.ICommand;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class Help implements ICommand {
    MessageBuilder mb = new MessageBuilder();

    @Override
    public void run(Message msg) {
        if (!isBotOrFake(msg.getAuthor())) {
            mb.append("Das ist mal ein Test ob der 'Classloader' das tut was ich will also ein Help :D ");
            mb.sendTo(msg.getChannel()).queue();
        }
    }

    @Override
    public String whatDoYouDo() {
        return "Ich helfe leuten";
    }

}
