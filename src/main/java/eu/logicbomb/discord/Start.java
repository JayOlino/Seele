package eu.logicbomb.discord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import org.reflections.Reflections;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import eu.logicbomb.discord.icommands.ICommand;
import eu.logicbomb.discord.listener.CommandListener;
import eu.logicbomb.discord.listener.ReadyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

public class Start {
    Properties                properties = new Properties();
    HashMap<String, ICommand> commandmap = new HashMap<>();

    private void init() {

        try {
            createDB();
            initProperty();
            initCommands();
            JDABuilder jdaB = new JDABuilder(AccountType.BOT);
            jdaB.addEventListener(new ReadyListener());
            jdaB.addEventListener(new CommandListener(properties, commandmap));
            jdaB.setAutoReconnect(true);
            jdaB.setStatus(OnlineStatus.DO_NOT_DISTURB);
            jdaB.setToken(properties.getProperty("BotToken"));
            jdaB.setGame(Game.watching("Wie Chio an meinen gedärmen spielt...."));
            JDA jda = jdaB.buildAsync();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void initProperty() throws IOException {

        FileInputStream in = null;
        FileOutputStream out = null;
        File pfile = new File("Seele.properties");
        if (!pfile.exists()) {
            out = new FileOutputStream(pfile);
            properties.setProperty("BotToken", "-key-");

            try {
                properties.store(out, "Initial creation of the File");
            }
            finally {
                if (null != out) {
                    try {
                        out.close();
                    }
                    catch(IOException ex) {
                    }
                }
            }
        }
        try {

            in = new FileInputStream(pfile);
            properties.load(in);
        }
        finally {
            if (null != in) {
                try {
                    in.close();
                }
                catch(IOException ex) {
                }
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
        System.out.println("Added " + commandmap.size() + " commands!");
    }

    private void createDB() {
        try {
            MongoClient mc = new MongoClient("localhost", 27017);
            MongoDatabase db = mc.getDatabase("Seele");

            System.out.println("Connection established");
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Pray that the Necronomicron will not make evil things...");
        System.out.println("Klatu Veratu Nec..*cough*");
        new Start().init();
    }
}
