package com.chipset.Listeners;

import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NicknameListener extends ListenerAdapter {

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        String userId = event.getUser().getId();

        String givenId = "78953526977368064"; // Replace with the ID you want to check
        List<String> cactusNames = Arrays.asList(
                "Saguaro", "Prickly Pear", "Barrel Cactus", "Cholla", "Pincushion Cactus",
                "Fishhook Cactus", "Bishop's Cap", "Christmas Cactus", "Easter Cactus",
                "Rat Tail Cactus", "Old Man Cactus", "Peanut Cactus", "Star Cactus",
                "Moon Cactus", "Fairy Castle Cactus", "Blue Columnar Cactus", "Golden Barrel Cactus",
                "Parodia Magnifica", "Hedgehog Cactus", "Sea Urchin Cactus", "Cactus"
        ); // Add more names as you like
        String givenName = cactusNames.get(new Random().nextInt(cactusNames.size()));

        if (userId.equals(givenId)) {
            event.getGuild().modifyNickname(event.getMember(), givenName).queue();
        }
    }
}
