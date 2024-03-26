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

        // Change J's nickname constantly
        Guild guild = event.getJDA().getGuildById(193117152709050368L); assert guild != null;

        // Define the array of names
        List<String> names = Arrays.asList(
                "Mallard", "Wood Duck", "Muscovy Duck", "Pekin Duck", "Rouen Duck",
                "Khaki Campbell", "Indian Runner Duck", "Crested Duck", "Saxony Duck",
                "Swedish Blue Duck", "Buff Duck", "Cayuga Duck", "Appleyard Duck",
                "Aylesbury Duck", "Black Swedish Duck", "Blue Swedish Duck", "Call Duck",
                "Magpie Duck", "Welsh Harlequin Duck", "Indian Runner Duck", "Canvasback Duck",
                "Gadwall Duck", "Northern Pintail", "Northern Shoveler", "American Wigeon",
                "Redhead Duck", "Canvasback Duck", "Greater Scaup", "Lesser Scaup",
                "Tufted Duck", "Ring-necked Duck", "Common Eider", "King Eider",
                "Harlequin Duck", "Common Goldeneye", "Barrow's Goldeneye",
                "Bufflehead Duck", "Common Merganser", "Hooded Merganser",
                "Red-breasted Merganser", "Ruddy Duck", "Falcated Duck",
                "Baikal Teal", "Garganey Duck", "Eurasian Wigeon", "American Black Duck",
                "Marbled Teal", "Green-winged Teal", "Blue-winged Teal", "Cinnamon Teal",
                "European Teal", "Yellow-billed Duck", "Pacific Black Duck",
                "Australian Shelduck", "Paradise Shelduck", "Radjah Shelduck",
                "Pink-eared Duck", "Wandering Whistling Duck", "West Indian Whistling Duck",
                "White-faced Whistling Duck", "Plumed Whistling Duck", "Spotted Whistling Duck",
                "Black-bellied Whistling Duck", "Fulvous Whistling Duck", "White-backed Duck",
                "Freckled Duck", "Comb Duck", "Crested Shelduck", "Baer's Pochard",
                "Madagascar Pochard", "Common Pochard", "Hardhead Duck", "Ringed Teal",
                "Andean Teal", "Chestnut Teal", "Silver Teal", "Speckled Teal",
                "Yellow-billed Pintail", "White-cheeked Pintail", "Chiloe Wigeon",
                "American White Pelican", "Australian Shoveler", "Black-headed Duck",
                "Black-necked Swan", "Blue-billed Duck", "Blue-winged Goose",
                "Bronze-winged Duck", "Cape Barren Goose", "Cape Teal", "Chiloe Wigeon",
                "Cotton Pygmy Goose", "Crested Screamer", "Eurasian Teal", "Fulvous Whistling Duck",
                "Garganey Teal", "Gray Teal", "Greater White-fronted Goose",
                "Green Pygmy Goose", "Kelp Goose", "Kerguelen Goose", "Knob-billed Duck",
                "Laysan Duck", "Lesser White-fronted Goose", "Magellanic Flightless Steamer Duck",
                "Magellanic Flightless Steamer Duck", "Madagascar Teal", "Mallard Duck",
                "Masked Duck", "Meller's Duck", "Muscovy Duck", "New Zealand Scaup",
                "Northern Shoveler", "Orinoco Goose", "Pacific Black Duck", "Puna Teal",
                "Red Shoveler", "Red-billed Duck", "Red-billed Pintail", "Red-billed Teal",
                "Red-crested Pochard", "Ring-necked Duck", "Rosy-billed Pochard",
                "Royal Spoonbill", "Ruddy Duck", "Silver Teal", "Southern Wigeon",
                "Spectacled Duck", "Spot-billed Duck", "Spot-billed Duck", "Spur-winged Goose",
                "Steamer Duck", "Steller's Eider", "Subantarctic Teal", "Swan Goose",
                "Tadorna Duck", "Torrent Duck", "Turquoise Duck", "Vulturine Guineafowl",
                "Wandering Whistling Duck", "West Indian Whistling Duck", "White-backed Duck",
                "White-cheeked Pintail", "White-faced Whistling Duck", "White-headed Duck",
                "White-winged Duck", "Yellow-billed Duck", "Yellow-billed Pintail", "Yellow-billed Teal"
        );

        // Get the Member object for the specified member
         Member member = guild.getMemberById("78953526977368064"); assert member != null;

        // Create a new TimerTask
        TimerTask renameTask = new TimerTask() {
            @Override
            public void run() {
                // Generate a random index
                int randomIndex = new Random().nextInt(names.size());

                // Get a random name from the array
                String newName = names.get(randomIndex);

                // Rename the user
                guild.modifyNickname(member, newName).queue();
            }
        };

        // Schedule the task to run every 5 seconds, delayed by 5 seconds
        new Timer().scheduleAtFixedRate(renameTask, 5*1000, 5 * 1000);
    }
}
