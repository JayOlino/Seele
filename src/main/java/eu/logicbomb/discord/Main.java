package eu.logicbomb.discord;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;

public class Main {

    public static void main(String[] args) {
        try {
            JDABuilder jdaB = new JDABuilder(AccountType.BOT);
            jdaB.setAutoReconnect(true);
            jdaB.setStatus(OnlineStatus.DO_NOT_DISTURB);
            jdaB.setToken("NDE2MjEwNDMyNzg5ODM5ODgz.DXBJ5g.Pxpo-F7ENk-o1-KPhPxZg5OEInE");
            JDA jda = jdaB.buildAsync();
        }
        catch(LoginException e) {
            e.printStackTrace();
        }

    }

}
