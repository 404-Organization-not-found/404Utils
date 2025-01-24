package dev.blackdev.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class Gateway extends ListenerAdapter {

    private static final long CHANNEL_ID = 1332088643304685579L;

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Member member = event.getMember();
        TextChannel channel = event.getGuild().getTextChannelById(CHANNEL_ID);
        if (channel != null) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.GREEN);
            embed.setTitle("Welcome!");
            embed.setDescription("Welcome " + member.getAsMention() + " to the server!");
            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        Member member = event.getMember();
        TextChannel channel = event.getGuild().getTextChannelById(CHANNEL_ID);
        if (channel != null) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.RED);
            embed.setTitle("Goodbye!");
            embed.setDescription(member.getEffectiveName() + " has left the server.");
            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }
}