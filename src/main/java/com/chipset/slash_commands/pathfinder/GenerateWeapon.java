package com.chipset.slash_commands.pathfinder;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collections;
import java.util.List;

public class GenerateWeapon extends SlashCommand {
    public GenerateWeapon() {
        this.name = "generatewepon";
        this.help = "generate code for adding a new pathfinder weapon";
        this.options = List.of(
                new OptionData(OptionType.STRING, "name", "the name of the weapon").setRequired(true),
                new OptionData(OptionType.STRING, "modifier", "strength_modifier or dexterity_modifier").setRequired(true)
                        .addChoice("strength_modifier", "strength_modifier")
                        .addChoice("dexterity_modifier", "dexterity_modifier"),
                new OptionData(OptionType.INTEGER, "attackmod", "(-) attack modifier").setRequired(true),
                new OptionData(OptionType.INTEGER, "bonus", "(+) attack bonus").setRequired(true),
                new OptionData(OptionType.STRING, "damage", "#d# for rolling damage").setRequired(true),
                new OptionData(OptionType.STRING, "damagetype", "type of damage").setRequired(true)
                        .addChoice("Bludgeoning", "Bludgeoning")
                        .addChoice("Slashing", "Slashing")
                        .addChoice("Piercing", "Piercing")
                        .addChoice("Acid", "Acid")
                        .addChoice("Cold", "Cold")
                        .addChoice("Electricity", "Electricity")
                        .addChoice("Fire", "Fire")
                        .addChoice("Sonic", "Sonic")
                        .addChoice("Positive", "Positive")
                        .addChoice("Negative", "Negative")
                        .addChoice("Force", "Force")
                        .addChoice("Precision", "Precision")
                        .addChoice("Unarmed", "Unarmed")
                        .addChoice("Poison", "Poison")
                        .addChoice("Bleed", "Bleed")
                        .addChoice("Psychic", "Psychic")
                        .addChoice("Negative", "Negative")
                        .addChoice("Positive", "Positive")
                        //.addChoice("Good", "Good")
                        //.addChoice("Evil", "Evil")
                        //.addChoice("Lawful", "Lawful")
                        //.addChoice("Chaotic", "Chaotic")
                        .addChoice("Silver", "Silver")
                        //.addChoice("Cold Iron", "Cold Iron")
                        .addChoice("Adamantine", "Adamantine")
                        .addChoice("Mithral", "Mithral")
                        //.addChoice("Byeshk", "Byeshk")
                        //.addChoice("Alignment", "Alignment")
                        .addChoice("Nonlethal", "Nonlethal")
                        .addChoice("Death", "Death")
                        .addChoice("Rending", "Rending")
                        .addChoice("Curse", "Curse")
                        //.addChoice("True", "True")
                        //.addChoice("Cosmic", "Cosmic")
                        //.addChoice("Blood", "Blood")
                        //.addChoice("Ferocity", "Ferocity")
        );
    }
    @Override
    protected void execute(SlashCommandEvent event) {
        String baseString = "|%s, {{roll01=To Hit\n" +
                "[[1d20cs20 + (@{selected|proficiency}) + (@{selected|%s}) - %d + %d]]\n" +
                "Dealing\n" +
                "[[%s + (@{selected|strength_modifier})]]\n" +
                "%s&#125;&#125; {{header=%s&#125;&#125;";

        String name = event.getOption("name").getAsString();
        String modifier = event.getOption("modifier").getAsString();
        int attackmod = event.getOption("attackmod").getAsInt();
        int bonus = event.getOption("bonus").getAsInt();
        String damage = event.getOption("damage").getAsString();
        String damageType = event.getOption("damagetype").getAsString();

        String out = String.format(baseString, name, modifier, attackmod, bonus, damage, damageType, name);

        event.reply("```\n"+out+"\n```").setEphemeral(true).queue();
    }
}
