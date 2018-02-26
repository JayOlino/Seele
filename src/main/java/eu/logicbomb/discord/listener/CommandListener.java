package eu.logicbomb.discord.listener;

import java.util.HashMap;
import java.util.Properties;

import eu.logicbomb.discord.icommands.ICommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

    Properties                properties = null;
    HashMap<String, ICommand> commandmap = new HashMap<>();

    public static String      PREFIX     = "&";

    public void onMessageReceived(MessageReceivedEvent event) {

        String rawMSG = event.getMessage().getContentRaw();
        System.out.println(rawMSG);
        if (rawMSG.startsWith(PREFIX)) {
            String[] argsMSG = rawMSG.replaceFirst(PREFIX, "").split(" ");
            String command = argsMSG[0];
            if (command != null && commandmap.containsKey(argsMSG[0])) {
                commandmap.get(command).run(event.getMessage());

            }
        }
    }

    public CommandListener(Properties properties, HashMap<String, ICommand> commandmap) {
        this.properties = properties;
        this.commandmap = commandmap;
    }

}
