package eu.logicbomb.discord.commands.admin;

import eu.logicbomb.discord.icommands.ICommand;
import net.dv8tion.jda.core.entities.Message;

public class Shutdown implements ICommand {

    @Override
    public void run(String[] args, Message msg) {
        if (!isBotOrFake(msg.getAuthor())) {
            if (isOwnerOrAdmin(msg)) {
                msg.getJDA().shutdownNow();
            }
        }
    }

    @Override
    public String whatDoYouDo() {
        return "Ich diene als Beispiel";
    }

}
