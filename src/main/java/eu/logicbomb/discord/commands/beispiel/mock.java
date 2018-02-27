package eu.logicbomb.discord.commands.beispiel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import eu.logicbomb.discord.icommands.ICommand;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.managers.GuildController;

public class mock implements ICommand {

    Vector<String> vecMok = new Vector<>();
    int            time   = 10;

    @Override
    public void run(String[] args, Message msg) {
        msg.delete().queue();

        if (!isBotOrFake(msg.getAuthor())) {
            try {
                fillMock();

                if (isOwnerOrAdmin(msg)) {
                    Guild g = msg.getGuild();

                    GuildController gc = new GuildController(g);
                    HashMap<String, String> umap = new HashMap<>();
                    for (Member mem : g.getMembers()) {
                        if (!mem.getUser().isBot() && !mem.getOnlineStatus().equals(OnlineStatus.OFFLINE)
                                && !mem.hasPermission(Permission.ADMINISTRATOR)) {
                            umap.put(mem.getEffectiveName(), mem.getNickname());
                            getRandNick(gc, mem);
                        }
                    }
                    TimeUnit.SECONDS.sleep(3);
                    getNormNick(gc, g.getMembers(), umap);
                }
            }
            catch(Exception e) {
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
        }
        finally {
            reader.close();
        }

    }

    private void getRandNick(GuildController gc, Member mem) {
        Random generator = new Random();
        int rnd = generator.nextInt(vecMok.size());
        gc.setNickname(mem, vecMok.get(rnd)).queue();
    }

    private void getNormNick(GuildController gc, List<Member> mem, HashMap<String, String> umap) {
        for (Member member : mem) {
            if (umap.containsKey(member.getEffectiveName())) {
                gc.setNickname(member, umap.get(member.getEffectiveName())).queue();
            }
        }
    }

    @Override
    public String whatDoYouDo() {
        return "Ich Ärger die Leute";
    }

}
