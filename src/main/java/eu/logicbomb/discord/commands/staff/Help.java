package eu.logicbomb.discord.commands.staff;

import java.lang.reflect.Method;
import java.util.Set;

import org.reflections.Reflections;

import eu.logicbomb.discord.icommands.ICommand;
import eu.logicbomb.discord.listener.CommandListener;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class Help implements ICommand {
    MessageBuilder mb = new MessageBuilder();

    @Override
    public void run(String[] args, Message msg) {
        if (!isBotOrFake(msg.getAuthor())) {
            Reflections reflections = null;
            Set<Class<? extends ICommand>> allClasses = null;
            mb.append("```\n");
            mb.append("*** Die Hilfe ***");
            if (isOwnerOrAdmin(msg)) {
                mb.append("\n");
                mb.append("-- Admin-Befehle --");
                mb.append("\n");
                mb.append("\n");
                reflections = new Reflections("eu.logicbomb.discord.commands.admin");
                allClasses = reflections.getSubTypesOf(ICommand.class);
                for (Class<? extends ICommand> class1 : allClasses) {
                    helpOutput(class1);
                }
            }
            mb.append("```");
            mb.sendTo(msg.getChannel()).queue();
            mb.clear();
            mb.append("```");
            reflections = new Reflections("eu.logicbomb.discord.commands.staff");
            allClasses = reflections.getSubTypesOf(ICommand.class);
            mb.append("\n");
            mb.append("-- Team-Befehle --");
            mb.append("\n");
            for (Class<? extends ICommand> class1 : allClasses) {
                helpOutput(class1);
            }
            mb.append("```");
            mb.sendTo(msg.getChannel()).queue();
        }
    }

    private void helpOutput(Class<? extends ICommand> class1) {
        System.out.println(CommandListener.PREFIX + class1.getSimpleName().toLowerCase());
        try {
            Method[] m = class1.getMethods();
            for (int i = 0; i < m.length; i++) {
                //                            System.out.println(m[i].getName());
                if (m[i].getName().equalsIgnoreCase("whatdoyoudo")) {
                    mb.append(CommandListener.PREFIX + class1.getSimpleName().toLowerCase() + "        " + (m[i].invoke(class1.newInstance(), null)));
                    mb.append("\n");
                }
            }
            //                                                String test = (String) m.invoke(class1, null);
        }
        catch(Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String whatDoYouDo() {
        return "Ich helfe Leuten";
    }

}
