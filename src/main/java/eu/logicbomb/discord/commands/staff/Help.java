package eu.logicbomb.discord.commands.staff;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.reflections.Reflections;

import eu.logicbomb.discord.icommands.ICommand;
import eu.logicbomb.discord.listener.CommandListener;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;

public class Help implements ICommand {
	MessageBuilder mb = new MessageBuilder();

	@Override
	public void run(String[] args, Message msg) {
		if (!isBotOrFake(msg.getAuthor())) {
			try {
				Reflections reflections = null;
				Set<Class<? extends ICommand>> allClasses = null;
				mb.append("```\n");
				mb.append("*** Die Hilfe ***");
				mb.append("```");
				mb.sendTo(msg.getChannel()).queue();
				if (isOwnerOrAdmin(msg)) {
					User us = msg.getAuthor();
					MessageBuilder mbA = new MessageBuilder();
					mbA.append("```\n");
					mbA.append("*** Die Hilfe ***");
					mbA.append("```");
					mbA.append("\n");

					mbA.append("```");
					mbA.append("\n");
					mbA.append("-- Admin-Befehle --");
					mbA.append("\n");
					mbA.append("\n");
					reflections = new Reflections("eu.logicbomb.discord.commands.admin");
					allClasses = reflections.getSubTypesOf(ICommand.class);
					for (Class<? extends ICommand> class1 : allClasses) {
						helpOutput(class1);
					}
					mbA.append("```");
					us.openPrivateChannel().queue((channel) -> {
						channel.sendMessage(mbA.build()).queue();
					});
					TimeUnit.MILLISECONDS.sleep(250);

					mbA.clear();
				}
				mb.clear();

				TimeUnit.MILLISECONDS.sleep(250);

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
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void helpOutput(Class<? extends ICommand> class1) {
		System.out.println(CommandListener.PREFIX + class1.getSimpleName().toLowerCase());
		try {
			Method[] m = class1.getMethods();
			for (int i = 0; i < m.length; i++) {
				// System.out.println(m[i].getName());
				if (m[i].getName().equalsIgnoreCase("whatdoyoudo")) {
					mb.append(CommandListener.PREFIX + class1.getSimpleName().toLowerCase() + "        "+ (m[i].invoke(class1.newInstance(), null)));
					mb.append("\n");
				}
			}
			// String test = (String) m.invoke(class1, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("###ERROR###", e);
		}
	}

	@Override
	public String whatDoYouDo() {
		return "Ich helfe Leuten";
	}

}
