package eu.logicbomb.discord.icommands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

public interface ICommand {

    public void run(String[] args, Message msg);

    public String whatDoYouDo();

    default public String getName() {
        return getClass().getSimpleName().toLowerCase();
    }

    default public boolean isBotOrFake(User user) {
        if (user.isBot() || user.isFake()) {
            return true;
        }
        return false;
    }

    default public boolean isOwnerOrAdmin(Message msg) {
        Member member = msg.getGuild().getMember(msg.getAuthor());
        if (!isBotOrFake(member.getUser())) {
            if (member.isOwner() || member.hasPermission(Permission.ADMINISTRATOR)) {
                return true;
            }
        }
        return false;
    }

    default public boolean isSpeacialUser(Message msg) {
        Member member = msg.getGuild().getMember(msg.getAuthor());
        if (!isBotOrFake(member.getUser())) {
            if (member.isOwner() || member.hasPermission(Permission.ADMINISTRATOR)) {
                return true;
            }
        }
        return false;
    }

}
