package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;

import java.util.Random;

public class Flip extends SlashCommand {
    public Flip() {
        this.name = "flip"; // must be lowercase
        this.help = "flips a coin";
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (event.getName().equals("flip")) {
            Random rand = new Random();

            boolean res = rand.nextBoolean();
            String result = (res) ? "heads" : "tails";

            event.reply(result).queue();
        }}
}
