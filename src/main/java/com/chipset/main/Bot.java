package com.chipset.main;


import com.chipset.commands.GetAvatarSlash;
import com.chipset.commands.MessageListener;
import com.chipset.commands.SaySlash;
import com.chipset.main.ReadyListener;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import javax.security.auth.login.LoginException;


public class Bot {
    public static void main(String[] arguments) {
        JDABuilder jdaBuilder = JDABuilder.createDefault("ODQ3NTIwMzUwMjkxNDkyOTc0.YK_Q1Q.OgHFgN3JALul_Nr3hgqK0KNStZM");
        jdaBuilder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        jdaBuilder.setActivity(Activity.watching("airplanes"));

        jdaBuilder.addEventListeners(
                new ReadyListener(),
                new MessageListener(),
                new SaySlash(),
                new GetAvatarSlash());

        try {
            JDA bot = jdaBuilder.build();
            bot.awaitReady();

            Guild guild = bot.getGuildById("847520841217343488");
            assert guild != null;
            guild.updateCommands()
                    .addCommands(new CommandData("say", "says the contents of the message")
                            .addOption(OptionType.STRING, "content", "repeats your message", true))
                    .addCommands(new CommandData("avatar", "retrieves a user's avatar")
                            .addOption(OptionType.USER, "target", "user you want the avatar of", true)
                            .addOption(OptionType.BOOLEAN, "stealth", "you trying to be sneaky?", false))
                    .queue();
        } catch (LoginException | InterruptedException e) {
            System.err.println("Couldn't log in.");
            e.printStackTrace();
        }
    }
}
