package com.chipset.slash_commands.pathfinder;

import com.chipset.main.RestClient;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pathfinder extends SlashCommand {
    public Pathfinder() {
        this.name = "pathfinder";
        this.help = "wip";
        this.children = new SlashCommand[] {
                new SubPathfinderSpells()
        };
    }
    @Override
    protected void execute(SlashCommandEvent event) {}

    private static class SubPathfinderSpells extends SlashCommand {
        public SubPathfinderSpells() {
            this.name = "spells";
            this.help = "wip";

            this.options = Arrays.asList(
                    new OptionData(
                            OptionType.STRING,
                            "name",
                            "name of the spell"
                            ).setRequired(true)
            );
        }
        @Override
        protected void execute(SlashCommandEvent event) {
            String baseUrl = "https://api.pathfinder2.fr/v1/pf2/spell?name=";
            String input = event.getOption("name").getAsString();
            String url = baseUrl+input;

            String key = "5cfe0fea-a504-4ee4-8f88-baf84aeabef2";

            JSONObject pfAPI = new JSONObject(RestClient.get(url, key));
            JSONArray results = pfAPI.getJSONArray("results");
            
            // check all results for perfect match
            JSONObject res = null;
            
            for (int i=0; i<results.length(); i++) {
                JSONObject temp = results.getJSONObject(i);
                
                if (temp.getString("name").toLowerCase().equals(input.toLowerCase()))
                    res = temp;
            }
            if (res == null)
                res = results.getJSONObject(0);
            System.out.println(results);


            event.replyEmbeds(generateEmbed(res, "spell"))
                    .setEphemeral(true)
                    .queue();
        }

        private MessageEmbed generateEmbed(JSONObject res, String field) {
            JSONObject system = res.getJSONObject("system");
            String desc = system.getJSONObject("description").getString("value");
            String urlName = res.getString("name").toLowerCase().replace(" ", "-");

            // replace @UUID substrings with their term
            Pattern pattern = Pattern.compile("@UUID\\[[^]]*\\]\\{([^{}]*)\\}");
            Matcher matcher = pattern.matcher(desc);
            desc = matcher.replaceAll("$1");

            // replace @Localize substrings with the contained #d#
            pattern = Pattern.compile("@Localize\\[.*?(\\d+d\\d+).*?\\]");
            matcher = pattern.matcher(desc);
            desc = matcher.replaceAll("$1");

            // Replace <li> tags with "-"
            desc = desc.replace("<li>", "-");
            // Replace <strong> and </strong> tags with "**"
            desc = desc.replace("<strong>", "**").replace("</strong>", "**");
            // Remove the remaining HTML tags
            desc = desc.replaceAll("\\<.*?\\>", "");


            return new EmbedBuilder()
                    .setTitle(res.getString("name"), "https://pf2.d20pfsrd.com/"+field+"/"+urlName)
                    .setDescription(desc)
                    .build();
        }
    }
}
