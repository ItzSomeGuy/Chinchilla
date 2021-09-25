package com.chipset.main;


import com.chipset.commands.*;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import javax.security.auth.login.LoginException;


public class Bot {
    static Dotenv dotenv = Dotenv.load();
    static String token = dotenv.get("TOKEN");

    public static void main(String[] arguments) {
        JDABuilder jdaBuilder = JDABuilder.createDefault(token);
        jdaBuilder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        jdaBuilder.setActivity(Activity.watching("airplanes"));

        jdaBuilder.addEventListeners(
                new ReadyListener(),
                new MessageListener(),
                new SaySlash(),
                new GetAvatarSlash(),
                new BanSlash(),
                new PingSlash());

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
                            .addOption(OptionType.BOOLEAN, "stealth", "you trying to be sneaky?"))
                    .addCommands(new CommandData("ban", "bans the user specified")
                            .addOption(OptionType.USER, "target", "user you want to ban", true)
                            .addOption(OptionType.STRING, "reason", "why you are banning them"))
                    .addCommands(new CommandData("ping", "ping pong oo haha!")
                            .addOption(OptionType.USER, "target", "the person I want to annoy", true))
                    .queue();
        } catch (LoginException | InterruptedException e) {
            System.err.println("Couldn't log in.");
            e.printStackTrace();
        }
    }
}
