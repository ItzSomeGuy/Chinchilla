package com.chipset.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class GetAvatarSlash extends ListenerAdapter {
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("avatar")) {
            User target = Objects.requireNonNull(event.getOption("target")).getAsUser();
            String url = target.getAvatarUrl();

            assert url != null;
            try {
                if (Objects.requireNonNull(event.getOption("stealth")).getAsBoolean())
                    event.reply(url).setEphemeral(true).queue(); // hidden reply
            } catch (NullPointerException e) {
                event.reply(url).queue(); // public reply
            }
        }
    }
}
