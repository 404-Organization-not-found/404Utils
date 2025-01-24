/*
//Copyright 404
//Licensed under MIT License
*/


package dev.blackdev.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

public class RoleInfoCommand extends ListenerAdapter {

    public static CommandData buildCommand() {
        return Commands.slash("roleinfo", "Display information about a specific role")
                .addOption(OptionType.ROLE, "role", "The role to get information about", true);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("roleinfo")) {
            var role = event.getOption("role").getAsRole();
            event.reply("Role Info:\n" +
                    "Name: " + role.getName() + "\n" +
                    "ID: " + role.getId() + "\n" +
                    "Color: " + role.getColor()).queue();
        }
    }
}