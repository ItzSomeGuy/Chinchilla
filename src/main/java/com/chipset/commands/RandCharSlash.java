package com.chipset.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

/**
 *
 * opt: re-roll any totals under 70
 * party mode: re-roll min
 *
 */

public class RandCharSlash extends ListenerAdapter {
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("rc")) {
            Member author = event.getMember();
            int[] tempStat;
            int tempTotal, min;

            String line;

            assert author != null;
            StringBuilder msg = new StringBuilder(author.getAsMention() + " 🎲");
            msg.append("\nGenerated from random stats:\n");

            int[][] stats = generateStats();
            int total = 0;

            for (int i=0; i<6; i++) {
                tempStat = stats[i]; //get stats
                tempTotal = 0;
                min = 7;
                for (int v : tempStat) { // set total
                    tempTotal += v;
                    if (v < min) {
                        min = v;
                    }
                }
                tempTotal -= min;
                total += tempTotal;

                // format
                line = String.format("(%d, %d, %d, %d)", tempStat[0], tempStat[1], tempStat[2], tempStat[3]);
                line = line.replaceAll("6", "**6**"); // highlight max roll
                line = line.replaceAll("1", "**1**"); // highlight low roll
                line = line.replaceFirst(String.valueOf(min), "~~"+min+"~~"); //drop lowest

                line = String.format("4d6kh3 " + line + " = `%d`", tempTotal); // add prefix & suffix

                msg.append(line).append("\n");
            }
            msg.append(String.format("Total: `%d`", total));

            // reply to user
            event.reply(msg.toString()).queue();
        }
    }

    public int[][] generateStats() {
        int[][] stats = new int[6][4];
        int[] temp;

        for (int i=0; i<6; i++) {
            temp = generateStat();
            System.arraycopy(temp, 0, stats[i], 0, 4);
        }

        return stats;
    }

    public static int[] generateStat() {
        Random rand = new Random();

        int max = 6;

        int[] result = new int[4];

        for (int i=0; i<4; i++) {
            int res = rand.nextInt((max - 1) + 1) + 1;
            result[i] = res;
        }

        return result;
    }
}
