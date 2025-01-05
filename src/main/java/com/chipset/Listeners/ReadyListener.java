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

//        // Change nickname constantly
//        Guild guild = event.getJDA().getGuildById(193117152709050368L); assert guild != null;
//
//        // Define the array of names
//        List<String> fishList = Arrays.asList(
//                // Freshwater Fish
//                "Trout", "Bass (Largemouth)", "Bass (Smallmouth)", "Catfish", "Pike",
//                "Perch", "Salmon", "Carp", "Tilapia", "Bluegill", "Pumpkinseed", "Crappie",
//                "Sturgeon", "Gar", "Zander", "Arapaima", "Peacock Bass", "Mahseer", "Snakehead",
//                "Tigerfish",
//
//                // Saltwater Fish
//                "Tuna", "Mahi-mahi", "Cod", "Snapper", "Grouper", "Halibut", "Swordfish",
//                "Mackerel", "Flounder", "Sea Bass", "Shark", "Marlin", "Wahoo", "Trevally",
//                "Barracuda", "Triggerfish", "Pompano", "Parrotfish", "Amberjack", "Tilefish",
//
//                // Tropical Fish (Commonly Kept in Aquariums)
//                "Guppy", "Betta (Siamese Fighting Fish)", "Neon Tetra", "Cardinal Tetra",
//                "Angelfish", "Discus", "Clownfish", "Gourami", "Tiger Barb", "Cherry Barb",
//                "Rasboras", "Cory Catfish", "Swordtails", "Platies", "Molly Fish", "Pufferfish",
//                "Archerfish", "Arowana", "Killifish", "Oscars",
//
//                // Bottom-Dwelling Fish
//                "Corydoras Catfish", "Plecostomus", "Loaches", "Eels", "Gobies", "Flounders",
//                "Rays", "Electric Catfish", "Kuhli Loach", "Blind Cave Fish",
//
//                // Predatory Fish
//                "Piranha", "Wels Catfish", "Barramundi", "Wolffish", "Snakehead",
//
//                // Exotic or Unusual Fish
//                "Axolotl", "Jellyfish", "Flying Fish", "Lionfish", "Goblin Shark",
//                "Coelacanth", "Gulper Eel", "Oarfish", "Frilled Shark",
//
//                // Game Fish
//                "Sailfish", "Tarpon", "Bonefish", "Rainbow Trout", "Striped Bass",
//
//                // Other Marine Fish
//                "Flying Gurnard", "Wrasse", "Hogfish", "Sardine", "Herring", "Surgeonfish",
//                "Butterflyfish", "Trumpetfish", "Cowfish"
//        );
//
//        // Get the Member object for the specified member
//         Member member = guild.getMemberById(78953526977368064L); assert member != null;
//
//        // Create a new TimerTask
//        TimerTask renameTask = new TimerTask() {
//            @Override
//            public void run() {
//                // Generate a random index
//                int randomIndex = new Random().nextInt(fishList.size());
//
//                // Get a random name from the array
//                String newName = fishList.get(randomIndex);
//
//                // Rename the user
//                guild.modifyNickname(member, newName).queue();
//            }
//        };
//
//        // Schedule the task to run every 5 seconds, delayed by 5 seconds
//        new Timer().scheduleAtFixedRate(renameTask, 5*1000, 600 * 1000);
    }
}
