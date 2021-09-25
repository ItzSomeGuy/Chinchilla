package com.chipset.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class PingSlash extends ListenerAdapter {
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("ping")) {
            Member target = Objects.requireNonNull(event.getOption("target")).getAsMember();

            assert target != null;
            event.reply(target.getAsMention()).queue();
        }
    }
}
