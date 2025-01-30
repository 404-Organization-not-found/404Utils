/*
//Copyright 404
//Licensed under Creative Commons Attribution-NonCommercial 4.0 International Public License
*/

package dev.blackdev.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends ListenerAdapter {

    public static CommandData buildCommand() {
        return Commands.slash("ping", "Check the bot's latency");
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            long time = System.currentTimeMillis();
            event.reply("Pong!").queue(response -> {
                long ping = System.currentTimeMillis() - time;
                response.editOriginal("Pong! Latency: " + ping + "ms").queue();
            });
        }
    }
}