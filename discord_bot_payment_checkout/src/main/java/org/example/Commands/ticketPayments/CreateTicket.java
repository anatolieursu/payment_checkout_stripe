package org.example.Commands.ticketPayments;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.nio.channels.FileChannel;

public class CreateTicket extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("ticket_payment")) {
            event.getGuild().createTextChannel("payment-" + event.getUser().getName()).queue(
                    createdChannel -> {
                event.reply("Your channel!" + createdChannel.getAsMention()).queue();
//                Ticket configuration
                        this.ticketConfiguration(event, createdChannel);
            }
            );
        }
    }
    private void ticketConfiguration(SlashCommandInteractionEvent event, TextChannel channel) {
        EmbedBuilder messageToPaymentTicket = new EmbedBuilder()
                .setTitle("Payment Ticket")
                .setDescription("Make a payment with /create command")
                .setFooter("Payment for " + event.getUser().getName());

        channel.sendMessageEmbeds(messageToPaymentTicket.build()).queue();
    }
}
