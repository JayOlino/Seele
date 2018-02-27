package eu.logicbomb.discord.commands.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.TimeLimiter;

import eu.logicbomb.discord.icommands.ICommand;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Guild.Timeout;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.GuildManager;

public class mock implements ICommand {

	Vector<String> vecMok = new Vector<>();
	int time = 10;
	@Override
	public void run(String[] args, Message msg) {
		msg.delete().queue();

		if (!isBotOrFake(msg.getAuthor())) {
			try {
				fillMock();

				if (isOwnerOrAdmin(msg)) {
					Guild g = msg.getGuild();

					GuildController gc = new GuildController(g);
					for (Member mem : g.getMembers()) {
						if (!mem.getUser().isBot() && !mem.getOnlineStatus().equals(OnlineStatus.OFFLINE)
								&& !mem.hasPermission(Permission.ADMINISTRATOR)) {

							String name = null;
							TimeUnit.MILLISECONDS.sleep(250);
							if (mem.getNickname() != null) {
								name = mem.getNickname();
								TimeUnit.MILLISECONDS.sleep(250);
								gc.setNickname(mem, getRandNick()).queue();
								TimeUnit.SECONDS.sleep(time);
								gc.setNickname(mem, name).queue();
							} else {
								name = mem.getEffectiveName();
								TimeUnit.MILLISECONDS.sleep(250);
								gc.setNickname(mem, getRandNick()).queue();
								TimeUnit.SECONDS.sleep(time);
								gc.setNickname(mem, name).queue();

							}

						}
					}

				}
			} catch (Exception e) {
				LOG.error("###EXCEPTION###", e);
			}
		}
	}

	private void fillMock() throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("mock.txt");
		InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
		BufferedReader reader = new BufferedReader(streamReader);
		try {
			for (String line; (line = reader.readLine()) != null;) {
				if (line != null || line.length() > 1) {
					vecMok.add(line);
				}
			}
		} finally {
			reader.close();
		}

	}

	private String getRandNick() {
		Random generator = new Random();
		int rnd = generator.nextInt(vecMok.size());
		return vecMok.get(rnd);
	}

	@Override
	public String whatDoYouDo() {
		return "Ich Ärger die Leute";
	}

}
