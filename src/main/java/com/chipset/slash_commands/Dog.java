package com.chipset.slash_commands;

import com.chipset.main.RestClient;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONException;
import org.json.JSONObject;

public class Dog extends SlashCommand {
    public Dog() {
        this.name = "dog";
        this.help = "woof";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        event.replyEmbeds(generateDogEmbed()).setEphemeral(true).queue();
    }

    private MessageEmbed generateDogEmbed() {
        try {
            String showdog = new JSONObject(RestClient.get("https://random.dog/woof.json")).getString("url");

            String ext = showdog.split("\\.")[2];
            while (ext.equals("mp4")) {
                showdog = new JSONObject(RestClient.get("https://random.dog/woof.json")).getString("url");
                ext = showdog.split("\\.")[2];
            }

            return (new EmbedBuilder()
                    .setTitle("Adorable.", showdog)
                    .setImage(showdog)
                    .build());
        } catch (JSONException e) {
            return (new EmbedBuilder()
                    .setTitle("API down :c")
                    .setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSYnIb0itfxYhI4FEIJN0CSa51LRBhPpUzrzKmLOFszgIy_YkjuijID6Qdn0ZUvDKlmV66MOSB1RAw&usqp=CAU&ec=48600112")
                    .build());
        }
    }
}
