package dev.blackdev.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.stream.Collectors;

public class ServerInfoCommand extends ListenerAdapter {

    public static CommandData buildCommand() {
        return Commands.slash("serverinfo", "Display information about the server");
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("serverinfo")) {
            var guild = event.getGuild();
            var onlineMembers = guild.getMembers().stream().filter(member -> member.getOnlineStatus().getKey().equals("online")).count();
            var offlineMembers = guild.getMembers().stream().filter(member -> member.getOnlineStatus().getKey().equals("offline")).count();
            var totalChannels = guild.getChannels().size();
            var totalRoles = guild.getRoles().size();

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.BLACK);
            embed.setTitle("Server Info");
            embed.setThumbnail(guild.getIconUrl());
            embed.addField("Name", guild.getName(), false);
            embed.addField("ID", guild.getId(), false);
            embed.addField("Members", String.valueOf(guild.getMemberCount()), false);
            embed.addField("Online Members", String.valueOf(onlineMembers), false);
            embed.addField("Offline Members", String.valueOf(offlineMembers), false);
            embed.addField("Channels", String.valueOf(totalChannels), false);
            embed.addField("Roles", String.valueOf(totalRoles), false);
            event.replyEmbeds(embed.build()).queue();
        }
    }
}