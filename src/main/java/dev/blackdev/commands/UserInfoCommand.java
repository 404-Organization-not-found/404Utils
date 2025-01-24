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

public class UserInfoCommand extends ListenerAdapter {

    public static CommandData buildCommand() {
        return Commands.slash("userinfo", "Display information about a user")
                .addOption(OptionType.USER, "user", "The user to get information about", true);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("userinfo")) {
            var user = event.getOption("user").getAsUser();
            var member = event.getGuild().getMember(user);
            event.reply("User Info:\n" +
                    "Name: " + user.getName() + "\n" +
                    "ID: " + user.getId() + "\n" +
                    "Joined: " + (member != null ? member.getTimeJoined() : "N/A")).queue();
        }
    }
}