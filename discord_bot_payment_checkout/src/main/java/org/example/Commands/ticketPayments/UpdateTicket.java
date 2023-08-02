package org.example.Commands.ticketPayments;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.example.Commands.CreateCommand;
import org.example.Main;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.channels.FileChannel;
import java.util.Arrays;

public class UpdateTicket extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("ticket_update")) {
            event.deferReply().queue();
            System.out.println("CHANNEL: " + event.getChannel().getName());
            if(event.getChannel().getName().contains("waiting") || event.getChannel().getName().contains("paid")) {
//                GET THE PAYMENT ID BY THE FCKING CHANNEL ID
                Integer paymentId = this.getPaymentId(event.getChannel().getIdLong());
                Integer buyers = this.getBuyers(paymentId);
                if(buyers >= 1) {
                    Main.changeChannelName(event.getGuild().getTextChannelById(event.getChannel().getId()), "paid-" + buyers);
                }
                event.getHook().sendMessage("Ok. Buyers: " + buyers).queue();
            } else {
                event.getHook().sendMessage("Make first an payment ticket!").queue();
            }
        }
    }
    private Integer getPaymentId(long channelId) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet getHTTP = new HttpGet("http://127.0.0.1:8000/api/ticket/certain?channelId=" + channelId);

            try (CloseableHttpResponse response = httpClient.execute(getHTTP)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String responseString = EntityUtils.toString(responseEntity);
                    Integer statusCode = response.getStatusLine().getStatusCode();
                    if(statusCode == 200) {
                        JSONArray data = new JSONArray(responseString);
                        JSONObject jsonObject = data.getJSONObject(0);

                        int paymentId = jsonObject.getInt("payment_id");
                        return paymentId;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private Integer getBuyers(int paymentId) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet getHTTP = new HttpGet("http://127.0.0.1:8000/api/payment/get/" + paymentId);

            try (CloseableHttpResponse response = httpClient.execute(getHTTP)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String responseString = EntityUtils.toString(responseEntity);
                    Integer statusCode = response.getStatusLine().getStatusCode();
                    if(statusCode == 200) {
                        JSONArray data = new JSONArray(responseString);
                        JSONObject jsonObject = data.getJSONObject(0);

                        int buyers = jsonObject.getInt("buyers");
                        return buyers;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
