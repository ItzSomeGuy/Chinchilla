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
        List<String> catBreeds = Arrays.asList(
                // Officially recognized domestic cat breeds
                "Abyssinian",
                "American Bobtail",
                "American Curl",
                "American Shorthair",
                "American Wirehair",
                "Balinese",
                "Bengal",
                "Birman",
                "Bombay",
                "British Shorthair",
                "Burmese",
                "Chartreux",
                "Cornish Rex",
                "Devon Rex",
                "Egyptian Mau",
                "Exotic Shorthair",
                "Havana Brown",
                "Himalayan",
                "Japanese Bobtail",
                "Javanese",
                "Korat",
                "LaPerm",
                "Maine Coon",
                "Manx",
                "Munchkin",
                "Nebelung",
                "Norwegian Forest Cat",
                "Ocicat",
                "Oriental",
                "Persian",
                "Peterbald",
                "Pixie-bob",
                "Ragamuffin",
                "Ragdoll",
                "Russian Blue",
                "Scottish Fold",
                "Selkirk Rex",
                "Siamese",
                "Siberian",
                "Singapura",
                "Snowshoe",
                "Somali",
                "Sphynx",
                "Tonkinese",
                "Toyger",
                "Turkish Angora",
                "Turkish Van",
                "York Chocolate",
                // Unofficially recognized breeds or mixed breeds
                "Calico",
                "Tabby",
                "Tuxedo",
                "Torbie",
                "Domestic Shorthair",
                "Domestic Longhair",
                "Domestic Medium Hair",
                // Designer breeds
                "Bengal x Siamese",
                "Maine Coon x Siamese",
                "Persian x Siamese",
                // Other popular mixes
                "Siamese x Domestic",
                "Maine Coon x American",
                // Wildcat species
                "African Wildcat",
                "European Wildcat",
                "Asian Wildcat",
                "Black-footed Cat",
                "Caracal",
                "Ocelot",
                "Serval",
                // Related animals
                "Lion",
                "Tiger",
                "Leopard",
                "Jaguar",
                "Cheetah",
                "Lynx",
                "Puma",
                "Panther",
                "Civet",
                "Genet",
                "Linsang",
                "Fossa",
                "Clouded Leopard",
                "Snow Leopard",
                "Bobcat",
                "Cougar",
                "Jungle Cat",
                "Sand Cat",
                "Marbled Cat",
                "Pallas's Cat",
                "Andean Mountain Cat",
                "Iberian Lynx",
                "Canadian Lynx",
                // Additional wildcat species
                "Bengalensis",
                "Leptailurus",
                "Felis margarita",
                "Felis nigripes",
                "Felis silvestris",
                // Additional related animals
                "Servaline Genet",
                "Binturong",
                "Tayra",
                "Asian Golden Cat",
                "Rusty-spotted Cat",
                "Flat-headed Cat",
                "Oncilla",
                "Geoffroy's Cat",
                "Bay Cat",
                "Kodkod"
                // Add more breeds, species, or related animals here if needed
                // ...
        );

        // Get the Member object for the specified member
         Member member = guild.getMemberById("226519889832050688"); assert member != null;

        // Create a new TimerTask
        TimerTask renameTask = new TimerTask() {
            @Override
            public void run() {
                // Generate a random index
                int randomIndex = new Random().nextInt(catBreeds.size());

                // Get a random name from the array
                String newName = catBreeds.get(randomIndex);

                // Rename the user
                guild.modifyNickname(member, newName).queue();
            }
        };

        // Schedule the task to run every 5 seconds, delayed by 5 seconds
        new Timer().scheduleAtFixedRate(renameTask, 5*1000, 600 * 1000);
    }
}
