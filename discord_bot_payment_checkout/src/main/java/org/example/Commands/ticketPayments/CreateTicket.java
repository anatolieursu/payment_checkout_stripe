package org.example.Commands.ticketPayments;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.nio.channels.FileChannel;
import java.util.Arrays;

public class CreateTicket extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("ticket_payment")) {
            event.getGuild().createTextChannel("payment-" + event.getUser().getName()).queue(
                    createdChannel -> {
                        event.reply("Your channel!" + createdChannel.getAsMention()).queue();
//                Ticket configuration
                        this.ticketConfiguration(event, createdChannel);
                        this.createTicketData(createdChannel);
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
    private Integer createTicketData(TextChannel channel) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://127.0.0.1:8000/api/ticket/create");

//              THE PARAMETERS
            httpPost.setEntity(new UrlEncodedFormEntity(Arrays.asList(
                    new BasicNameValuePair("channelId", channel.getId().toString())
            )));
//               THE PARAMeters

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String responseString = EntityUtils.toString(responseEntity);
                    Integer statusCode = response.getStatusLine().getStatusCode();
                    return statusCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
