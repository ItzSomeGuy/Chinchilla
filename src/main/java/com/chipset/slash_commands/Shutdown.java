package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.JDA;

public class Shutdown extends SlashCommand {

    public Shutdown() {
        this.name = "shutdown";
        this.help = "Shuts down the bot";
        this.ownerCommand = true;
    }
    @Override
    protected void execute(SlashCommandEvent event) {
        event.reply("peace out then").setEphemeral(true).queue();

        event.getJDA().shutdown();
    }
}
