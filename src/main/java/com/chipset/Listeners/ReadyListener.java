package com.chipset.Listeners;

import com.chipset.slash_commands.Free;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.*;

import static com.chipset.slash_commands.Free.generateEmbeds;

public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Message.suppressContentIntentWarning();
        System.out.println(event.getJDA().getSelfUser().getName() + " is online!");

       // Free Shit Reminder :)
        Timer timer = new Timer();
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        date.set(Calendar.HOUR_OF_DAY, 16);
        date.set(Calendar.MINUTE, 20);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        // If today is Thursday, and it's already past noon, schedule for next week instead
        if (date.getTime().before(new Date())) {
            date.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // Schedule the task to run every Thursday at 4:20pm
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ArrayList<JSONObject> results = Free.getPromotions();
                if (results != null) {
                    ArrayList<MessageEmbed> embeds = generateEmbeds(results);

                    Guild guild = event.getJDA().getGuildById(193117152709050368L); assert guild != null;
                    TextChannel tc = guild.getTextChannelById(724847703208755231L); assert tc != null;

                    tc.sendMessageEmbeds(embeds).queue();
                }
            }
        }, date.getTime(), 7 * 24 * 60 * 60 * 1000L); // repeat every week
    }
}
