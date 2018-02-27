package eu.logicbomb.discord.listener;

import eu.logicbomb.discord.Start;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class ReadyListener implements EventListener {

    @Override
    public void onEvent(Event event) {
        if (event instanceof ReadyEvent)
            Start.LOG.info("API is ready for Use!");
    }

}
