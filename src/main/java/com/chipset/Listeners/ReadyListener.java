package com.chipset.Listeners;

import com.chipset.slash_commands.Free;
import net.dv8tion.jda.api.entities.*;
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
        date.set(Calendar.HOUR_OF_DAY, 12);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        // If today is Thursday, and it's already past noon, schedule for next week instead
        if (date.getTime().before(new Date())) {
            date.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // Schedule the task to run every Thursday at 12pm
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

        // Change nickname constantly
        Guild guild = event.getJDA().getGuildById(193117152709050368L); assert guild != null;

        // Define the array of names
        List<String> wormTypes = Arrays.asList(
                "Earthworm",
                "Roundworm",
                "Tapeworm",
                "Hookworm",
                "Flatworm",
                "Bloodworm",
                "Ragworm",
                "Pinworm",
                "Whipworm",
                "Filarial worm",
                "Guinea worm",
                "Leech",
                "Horsehair worm",
                "Threadworm",
                "Ribbon worm",
                "Arrowworm",
                "Velvet worm",
                "Peanut worm",
                "Beard worm",
                "Spaghetti worm",
                "Palolo worm",
                "Eelworm",
                "Vinegar eel",
                "Arrowhead worm",
                "Bristleworm",
                "Sandworm",
                "Moon jellyfish worm",
                "Horsehair worm",
                "Arrow worm",
                "Beard worm",
                "Horsehair worm",
                "Blackworm",
                "Mermithid worm",
                "Gordian worm",
                "Acorn worm",
                "Arrow worm",
                "Marine worm"
        );

        // Get the Member object for the specified member
         Member member = guild.getMemberById(226519889832050688L); assert member != null;

        // Create a new TimerTask
        TimerTask renameTask = new TimerTask() {
            @Override
            public void run() {
                // Generate a random index
                int randomIndex = new Random().nextInt(wormTypes.size());

                // Get a random name from the array
                String newName = wormTypes.get(randomIndex);

                // Rename the user
                guild.modifyNickname(member, newName).queue();
            }
        };

        // Schedule the task to run every 5 seconds, delayed by 5 seconds
        new Timer().scheduleAtFixedRate(renameTask, 5*1000, 600 * 1000);
    }
}
