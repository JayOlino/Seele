package eu.logicbomb.discord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;

public class Start {
    Properties properties = new Properties();

    private void initProperty() throws IOException {

        FileInputStream in = null;
        FileOutputStream out = null;
        File pfile = new File("Seele.properties");
        if (!pfile.exists()) {
            out = new FileOutputStream(pfile);
            properties.setProperty("BotToken", "NDE2MjEwNDMyNzg5ODM5ODgz.DXVoiw.oCKXMaG6D9XGoft6_SbgwenVFhY");

            try {
                properties.store(out, null);
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

    private void init() {

        try {
            initProperty();
            JDABuilder jdaB = new JDABuilder(AccountType.BOT);
            jdaB.setAutoReconnect(true);
            jdaB.setStatus(OnlineStatus.DO_NOT_DISTURB);
            jdaB.setToken(properties.getProperty("BotToken"));
            JDA jda = jdaB.buildAsync();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Start().init();
    }
}
