package com.chipset.slash_commands;

import com.chipset.main.RestClient;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONException;
import org.json.JSONObject;

public class Cat extends SlashCommand {
    public Cat() {
        this.name = "cat";
        this.help = "meow";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        event.replyEmbeds(generateCatEmbed()).setEphemeral(true).queue();
    }

    private MessageEmbed generateCatEmbed() {
        try {
            String showcat = new JSONObject(RestClient.get("https://aws.random.cat/meow")).getString("file");

            return (new EmbedBuilder()
                    .setTitle("Adorable.", showcat)
                    .setImage(showcat)
                    .build());
        } catch (JSONException e) {
            return (new EmbedBuilder()
                    .setTitle("API down :c")
                    .setImage("https://as1.ftcdn.net/v2/jpg/01/10/90/34/1000_F_110903477_Zfa2H8RDqGEgVaIEbqqmGVTKUSOh5GSU.jpg")
                    .build());
        }
    }
}
