package eu.logicbomb.discord.commands.admin;

import java.io.File;

import eu.logicbomb.discord.Start;
import eu.logicbomb.discord.icommands.ICommand;
import net.dv8tion.jda.core.entities.Message;

public class Shutdown implements ICommand {

    @Override
    public void run(String[] args, Message msg) {
        if (!isBotOrFake(msg.getAuthor())) {
            if (isOwnerOrAdmin(msg)) {
                msg.getJDA().shutdownNow();
                File file = new File(Start.properties.getProperty("BotHomeDir") + "seele.pid");
                if (file.exists()) {
                    file.deleteOnExit();
                }
            }
        }
    }

    @Override
    public String whatDoYouDo() {
        return "Ich fahre den Bot unverzüglich runter, der Bot muss auf dem Server neu ausgeführt werden";
    }

}
