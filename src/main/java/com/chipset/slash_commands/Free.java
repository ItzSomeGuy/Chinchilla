package com.chipset.slash_commands;

import com.chipset.main.RestClient;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Free extends SlashCommand {
    public Free() {
        name = "free";
        help = "shows the free games from epic";
    }
    @Override
    protected void execute(SlashCommandEvent event) {
        ArrayList<JSONObject> results = getPromotions();
        if (results == null) {
            event.reply("No free games found").queue();
        } else {
            ArrayList<MessageEmbed> embeds = generateEmbeds(results);
            event.replyEmbeds(embeds).setEphemeral(true).queue();
        }
    }

    public static ArrayList<JSONObject> getPromotions() {
        JSONObject data;
        JSONArray res = null;
        try {
            data = new JSONObject(RestClient.get("https://store-site-backend-static.ak.epicgames.com/freeGamesPromotions"));
            res = data.getJSONObject("data")
                    .getJSONObject("Catalog")
                    .getJSONObject("searchStore")
                    .getJSONArray("elements");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // make sure there are results
        if (res == null) {
            return null;
        }

        ArrayList<JSONObject> results = new ArrayList<>();
        // for each JSONObject in res, check if res.price.totalPrice.discountPrice = 0 and res.promotions is not null => if true, add to results
        for (int i = 0; i < res.length(); i++) {
            JSONObject temp = res.getJSONObject(i);
            if (temp.getJSONObject("price")
                    .getJSONObject("totalPrice")
                    .getInt("discountPrice") == 0 &&
                    !temp.get("promotions").toString().equals("null")) {
                results.add(temp);
            }
        }
        return results;
    }

    public static ArrayList<MessageEmbed> generateEmbeds(ArrayList<JSONObject> promotions) {
        ArrayList<MessageEmbed> embeds = new ArrayList<>();

        for (JSONObject promotion : promotions) {
            EmbedBuilder builder = new EmbedBuilder();

            // Set the title
            builder.setTitle(promotion.getString("title"));

            // Add the game's link
            String link = "https://store.epicgames.com/en-US/p/" + promotion.getString("productSlug");
            builder.addField("Claim Promotion:", "[epicgames.com](" + link + ")", true);

            // Add the end date of the promotion
            String endDate = promotion.getJSONObject("promotions")
                    .getJSONArray("promotionalOffers")
                    .getJSONObject(0)
                    .getJSONArray("promotionalOffers")
                    .getJSONObject(0)
                    .getString("endDate");
            LocalDateTime inputDateTime = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME);
            long dateTime = inputDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
            builder.addField("Offer Expires:", "<t:" + dateTime + ":R> \uD83D\uDC40", false);

            // Add the original price
            builder.addField("Original Price:", "More than I am getting paid for this.", false);

            // Set the image
            String imageUrl = promotion.getJSONArray("keyImages").getJSONObject(0).getString("url");
            builder.setImage(imageUrl);

            // Set the color
            builder.setColor(0x48581a);

            // Build the embed and add it to the list
            embeds.add(builder.build());
        }

        return embeds;
    }
}
