/*
//Copyright 404
//Licensed under Creative Commons Attribution-NonCommercial 4.0 International Public License
*/

package dev.blackdev.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

public class AvatarCommand extends ListenerAdapter {

    public static CommandData buildCommand() {
        return Commands.slash("avatar", "Display a user's avatar")
                .addOption(OptionType.USER, "user", "The user to get the avatar of", true);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("avatar")) {
            var user = event.getOption("user").getAsUser();
            event.reply(user.getEffectiveAvatarUrl()).queue();
        }
    }
}