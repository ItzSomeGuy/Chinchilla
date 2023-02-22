package com.chipset.slash_commands;

import com.chipset.main.RestClient;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

public class Reddit extends SlashCommand {
    public Reddit() {
        this.name = "reddit";
        this.help = "fetches the top from reddit";

        this.children = new SlashCommand[] {
                new SubRedditWeekly(),
                new SubRedditMonthly(),
                new SubRedditRandom()
        };
    }

    @Override
    protected void execute(SlashCommandEvent event) {}

    private static class SubRedditWeekly extends SlashCommand {
        public SubRedditWeekly() {
            this.name = "weekly";
            this.help = "fetches the top post of the week";

            this.options = Collections.singletonList(
                    new OptionData(OptionType.STRING, "subreddit", "the subreddit to search").setRequired(true)
            );
        }
        @Override
        protected void execute(SlashCommandEvent event) {
            String subreddit = event.getOption("subreddit").getAsString();

            event.replyEmbeds(gatherData(subreddit, "top/.json?t=week")).setEphemeral(true).queue();
        }
    }

    private static class SubRedditMonthly extends SlashCommand {
        public SubRedditMonthly() {
            this.name = "monthly";
            this.help = "fetches the top post of the month";

            this.options = Collections.singletonList(
                    new OptionData(OptionType.STRING, "subreddit", "the subreddit to search").setRequired(true)
            );
        }

        @Override
        protected void execute(SlashCommandEvent event) {
            String subreddit = event.getOption("subreddit").getAsString();

            event.replyEmbeds(gatherData(subreddit, "top/.json?t=today")).setEphemeral(true).queue();
        }
    }

    private static class SubRedditRandom extends SlashCommand {
        public SubRedditRandom() {
            this.name = "random";
            this.help = "fetches a random reddit post";

            this.options = Arrays.asList(
                    new OptionData(OptionType.STRING, "subreddit", "the subreddit to search").setRequired(true),
                    new OptionData(OptionType.INTEGER, "limit", "pick from the last x posts")
            );
        }

        @Override
        protected void execute(SlashCommandEvent event) {
            String subreddit = event.getOption("subreddit").getAsString();
            int limit = 0;
            try {
                limit = event.getOption("limit").getAsInt();
            } catch (NullPointerException ignored) {}

            event.replyEmbeds(gatherRandom(subreddit, limit)).setEphemeral(true).queue();
        }
    }

    private static MessageEmbed gatherData(String subreddit, String sort) {
        String baseURL = "https://reddit.com/r/%s/" + sort; // sort => top/.json?t=[sort]
        String url;

        // default to reddit homepage json
        if (subreddit.isBlank()) {
            url = "";
        } else {
            url = String.format(baseURL, subreddit.replace("r/", ""));
        }

        // gather data
        JSONObject reddit = new JSONObject(RestClient.get(url));
        JSONArray data = reddit.getJSONObject("data").getJSONArray("children");

        if (data.length() == 0) {
            throw new IllegalArgumentException("This subreddit doesn't have any posts!");
        }

        // get post data
        JSONObject post = data.getJSONObject(0).getJSONObject("data");

        return generatePostEmbed(post).build();
    }

    private static MessageEmbed gatherRandom(String subreddit, int limit) {
        String baseURL = "https://reddit.com/r/%s/.json";
        String url;

        SecureRandom rand = new SecureRandom();

        // default to reddit homepage json
        if (subreddit.isBlank()) {
            url = "";
        } else {
            url = String.format(baseURL, subreddit.replace("r/", ""));
        }

        // gather data
        JSONObject reddit = new JSONObject(RestClient.get(url));
        JSONArray data = reddit.getJSONObject("data").getJSONArray("children");

        if (data.length() == 0) {
            throw new IllegalArgumentException("This subreddit doesn't have any posts!");
        }

        // get random post data
        if (limit == 0)
            limit = data.length();

        JSONObject post = data.getJSONObject(rand.nextInt(limit)).getJSONObject("data");

        return generatePostEmbed(post).build();
    }

    private static EmbedBuilder generatePostEmbed(JSONObject post) {
        boolean video = post.getBoolean("is_video");
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle(post.getString("title"), "https://reddit.com/"+post.getString("permalink"));
        embed.setAuthor("u/" + post.getString("author"), "https://reddit.com/u/" + post.getString("author"));
        embed.setTimestamp(Instant.ofEpochSecond(post.getLong("created_utc")));
        embed.addField("Upvotes", String.valueOf(post.getInt("score")), true);
        embed.addField("Comments", String.valueOf(post.getInt("num_comments")), true);
        if (post.has("description"))
            embed.setDescription(post.getString("description"));
        if (post.has("preview") && !video)
            embed.setImage(post.getString("url"));
        else if (post.has("preview") && video) {
            embed.setImage(post.getString("thumbnail").replace("\"", ""));
        }

        return embed;
    }
}
