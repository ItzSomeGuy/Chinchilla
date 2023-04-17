package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class LoFi extends SlashCommand {
    public LoFi() {
        this.name = "lofi";
        this.help = "Call up LoFi Girl and get some vibes going";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        StringSelectMenu vibeCheck = StringSelectMenu.create("menu:vibeCheck")
                //.setPlaceholder("horny vibes are currently out of stock")
                .setRequiredRange(1,1)
                .addOption("relaxing", "relax")
                .addOption("chill", "chill")
                .addOption("studying", "study")
                .addOption("sleepy", "sleep")
                .addOption("gaming", "game")
                .build();

        event.reply("What's the vibe?")
                .addActionRow(vibeCheck)
                .setEphemeral(true)
                .queue();
    }
}
