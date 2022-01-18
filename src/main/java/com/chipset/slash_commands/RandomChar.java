package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Objects;

public class RandomChar extends SlashCommand {
    public RandomChar() {
        this.name = "randomchar";
        this.help = "Generates a random character";
        this.aliases = new String[]{"rc"};

        this.options = Collections.singletonList(new OptionData(OptionType.BOOLEAN, "epic", "re-roll 1's")
                .setRequired(false));
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        boolean epic;

        try {
            epic = Objects.requireNonNull(event.getOption("epic")).getAsBoolean();
        } catch (NullPointerException e) {
            epic = false;
        }

        SecureRandom rand = new SecureRandom(); // more random but slower

        // roll 4 dice with 6 sides and drop the lowest die, 6 times
        int total;
        int t;

        StringBuilder msg = new StringBuilder();

        do {
            // reset vars
            total = 0;
            msg.setLength(0);
            msg.append("\n");

            for (int d = 0; d < 6; d++) {
                t = 0;

                msg.append("[ ");

                // roll 4 dice
                int[] dice = new int[4];
                for (int i = 0; i < 4; i++) {
                    dice[i] = rand.nextInt(6) + 1;

                    if (epic && dice[i] == 1) {
                        dice[i] = rand.nextInt(6) + 1;
                    }
                }
                // get min
                int min = Integer.MAX_VALUE;
                int minIndex = -1;

                // calc min
                for (int i = 0; i < 4; i++) {
                    if (dice[i] < min) {
                        min = dice[i];
                        minIndex = i;
                    }
                }

                // print rolls
                for (int i = 0; i < 4; i++) {
                    if (i != minIndex) {
                        msg.append(dice[i]).append(" ");
                    } else
                        msg.append("~~").append(dice[i]).append("~~").append(" ");
                }

                // drop min
                int[] rolls = dice.clone();
                for (int i = 0; i < 4; i++) {
                    if (rolls[i] == min) {
                        rolls[i] = 0;
                        break;
                    }
                }
                // add rolls to total
                for (int r : rolls) {
                    total += r;
                    t += r;
                }
                msg.append("]: ").append("**").append(t).append("**").append("\n");
            }

            msg.append("**").append("Total: ").append(total).append("**");

        } while (total < 70);
        // add formatting and shit
        event.reply(msg.toString()).queue();
    }
}
