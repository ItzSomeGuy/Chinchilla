package com.chipset.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.Objects;


public class BanSlash extends ListenerAdapter {
    private Member target;
    private String reason;

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("ban")) {

            try {
                reason = Objects.requireNonNull(event.getOption("event")).getAsString();
            } catch (NullPointerException ignored) {}

            target = Objects.requireNonNull(event.getOption("target")).getAsMember();
            assert target != null;

            // confirmation
            event.reply("do they really deserve it?")
                    .addActionRow(
                            Button.secondary("abort", "forgive them"),
                            Button.danger("ban", "ban them")
                    )
                    .setEphemeral(true)
                    .queue();
        }
    }

    public void onButtonClick(ButtonClickEvent event) {
        if (event.getComponentId().equals("ban")) {
            if (reason != null) {
                target.ban(0, reason).queue();
            } else {
                target.ban(0).queue();
            }

            event.editComponents().setActionRow(
                    Button.danger("ban", "banned").asDisabled()
            ).queue();
        } else if (event.getComponentId().equals("abort")) {
            event.editComponents().setActionRow(
                    Button.secondary("abort", "forgiven").asDisabled()
            ).queue();
        }
    }
}
