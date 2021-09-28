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
                new SaySlash(),
                new GetAvatarSlash(),
                new BanSlash(),
                new RenameSlash(),
                new BanSlash(),
                new FlipSlash(),
                new RollSlash(),
                new RandCharSlash());

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
                    .addCommands(new CommandData("rename", "change your nickname or someone else's")
                            .addOption(OptionType.USER, "target", "the person whom you want to rename")
                            .addOption(OptionType.STRING, "new_nickname", "the cool nickname you thought of"))
                    .addCommands(new CommandData("flip", "flips a coin"))
                    .addCommands(new CommandData("roll", "rolls some amount of die/dice")
                            .addOption(OptionType.INTEGER, "count", "number of dice you are rolling", true)
                            .addOption(OptionType.INTEGER, "sides", "how many sides your die has", true))
                    .addCommands(new CommandData("rc", "generates random character stats"))
                    .queue();
        } catch (LoginException | InterruptedException e) {
            System.err.println("Couldn't log in.");
            e.printStackTrace();
        }
    }
}
