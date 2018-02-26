package eu.logicbomb.discord.commands.admin;

import eu.logicbomb.discord.icommands.ICommand;
import net.dv8tion.jda.core.entities.Message;

public class Shutdown implements ICommand {

    @Override
    public void run(Message msg) {
        if (isBotOrFake(msg.getAuthor())) {

        }
    }

    @Override
    public String whatDoYouDo() {
        return "Ich diene als Beispiel";
    }

}
