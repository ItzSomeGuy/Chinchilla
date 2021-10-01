package com.chipset.main;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class MessageHandler extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Guild guild = event.getGuild();
        Member author = event.getMember();
        assert author != null;
        String authorID = author.getId();
        Role egg = guild.getRolesByName("golden egg", true).get(0);

        Random rand = new Random();
        double mult = 0;

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        List<List<String>> data = DataHandler.getCSV(guild);
        for (List<String> user : data) {
            if (user.get(1).equals(authorID)) {
                mult = Double.parseDouble(user.get(1)); // set mult to user's multiplier
            }
        }

        int res = rand.nextInt(101);
        double check = rand.nextDouble();

        if (res <= 5 && check <= mult) { // 5% chance (minus multiplier) to get golden egg
            guild.addRoleToMember(author, egg).queue(); // give golden egg
            DataHandler.updateCSV(guild, authorID, 5, String.valueOf(mult / 2.0)); // reduce multiplier
            scheduler.schedule(() -> { // take golden egg
                guild.removeRoleFromMember(author, egg).queue();
            }, 1, TimeUnit.DAYS);
        }
    }
}
