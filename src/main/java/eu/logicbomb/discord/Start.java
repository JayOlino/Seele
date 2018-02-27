package eu.logicbomb.discord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.logicbomb.discord.database.DB;
import eu.logicbomb.discord.icommands.ICommand;
import eu.logicbomb.discord.listener.CommandListener;
import eu.logicbomb.discord.listener.GuildListener;
import eu.logicbomb.discord.listener.ReadyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

public class Start {
    Properties                properties = new Properties();
    HashMap<String, ICommand> commandmap = new HashMap<>();
    public static Logger      LOG        = LoggerFactory.getLogger(Start.class.getSimpleName());

    private DB                db;

    private void init() {

        try {
            initProperty();
            db = new DB(properties);
            initCommands();
            JDABuilder jdaB = new JDABuilder(AccountType.BOT);
            jdaB.addEventListener(new ReadyListener());
            jdaB.addEventListener(new GuildListener(db, (String) properties.get("BotServerID")));
            jdaB.addEventListener(new CommandListener(properties, commandmap));
            jdaB.setAutoReconnect(true);
            jdaB.setStatus(OnlineStatus.DO_NOT_DISTURB);
            jdaB.setToken(properties.getProperty("BotToken"));
            jdaB.setGame(Game.watching("Wie Chio an meinen gedärmen spielt...."));

            @SuppressWarnings("unused")
            JDA jda = jdaB.buildAsync();

        }
        catch(Exception e) {
            LOG.error("###ERROR###", e);
        }

    }

    private void initProperty() throws IOException {
        String filename = "Seele.properties";
        FileOutputStream out = null;
        FileInputStream in = null;

        File pfile = new File(filename);
        if (!pfile.exists()) {
            out = new FileOutputStream(pfile);
            properties.setProperty("BotToken", "-key-");
            properties.setProperty("BotServerID", "-");
            properties.setProperty("BotDB", "-");
            properties.setProperty("BotDBHost", "-");
            properties.setProperty("BotDBUser", "-");
            properties.setProperty("BotDBPW", "-");
            properties.setProperty("BotDBPort", "-");

            try {
                properties.store(out, "Initial creation of the File");
            }
            finally {
                if (null != out) {
                    out.close();
                }
            }
        }
        try {

            in = new FileInputStream(pfile);
            properties.load(in);
        }
        finally {
            if (null != in) {
                in.close();
            }
        }

    }

    private void initCommands() throws InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections("eu.logicbomb.discord.commands");
        Set<Class<? extends ICommand>> allClasses = reflections.getSubTypesOf(ICommand.class);
        for (Class<? extends ICommand> class1 : allClasses) {
            if (!class1.getSimpleName().equalsIgnoreCase("sample")) {
                commandmap.put(class1.getSimpleName().toLowerCase(), class1.newInstance());
            }
        }
        LOG.info("Added " + commandmap.size() + " commands!");
    }

    public static void main(String[] args) {
        LOG.info("Pray that the Necronomicron will not make evil things...");
        LOG.info("Klatu Veratu Nec..*cough* *cough* *cough*");

        new Start().init();
    }
}
