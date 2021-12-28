package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PFP extends SlashCommand {
    public PFP() {
        this.name = "pfp"; // must be lowercase
        this.help = "fetches a user's pfp";

        List<OptionData> options = new ArrayList<>();
        this.options = Collections.singletonList(new OptionData(OptionType.USER, "target", "user you want the avatar of")
                .setRequired(true));
    }

    @Override
    public void execute(SlashCommandEvent event) {
        OptionMapping target = event.getOption("target");

        String url = target != null ? target.getAsUser().getAvatarUrl() : null;
        assert url != null;

        event.reply(url).setEphemeral(true).queue();
    }
}
