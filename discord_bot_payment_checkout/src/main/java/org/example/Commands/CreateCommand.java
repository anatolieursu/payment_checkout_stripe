package org.example.Commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.channel.ChannelManager;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.example.Main;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.channels.Channel;
import java.util.Arrays;

public class CreateCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("create")) {
            event.deferReply().queue();
            Integer newPaymentID = 1;
//            here the function that check if the current channel is or no a ticket payment channel
            String payment_name = event.getOption("payment_name").getAsString(); // Primul parametru POST
            String payment_price = event.getOption("payment_price").getAsString();
            String user = event.getMember().getUser().getName();
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost("http://127.0.0.1:8000/api/create");
//              THE PARAMETERS
                httpPost.setEntity(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("payment_name", payment_name),
                        new BasicNameValuePair("payment_price", payment_price),
                        new BasicNameValuePair("user", user)
                )));
//               THE PARAMS

                try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                    HttpEntity responseEntity = response.getEntity();
                    if (responseEntity != null) {
                        String responseString = EntityUtils.toString(responseEntity);
                        Integer statusCode = response.getStatusLine().getStatusCode();
                        System.out.println("STATUS CODE: " + statusCode);
                        if(statusCode == 200) {
                            JSONObject data = new JSONObject(responseString);
                            String paymentId = data.getString("payment_id");
                            try {
                                newPaymentID = data.getInt("id");
                            } catch (Exception e) {
                                System.out.println("ERORR - " + e);
                            }

                            event.getHook().sendMessage("Your Payment Link: http://127.0.0.1:8000/payment/"+paymentId).queue();
                        } else {
                            event.getHook().sendMessage("ERROR: " + statusCode).queue();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(event.getChannel().getName().contains("payment-")) {
//                Here the api will update the respective ticket with id from uppp
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpPost httpPost = new HttpPost("http://127.0.0.1:8000/api/ticket/payment");

//              THE PARAMETERS
                    httpPost.setEntity(new UrlEncodedFormEntity(Arrays.asList(
                            new BasicNameValuePair("paymentId", newPaymentID.toString()),
                            new BasicNameValuePair("channelId", event.getChannel().getId().toString())
                    )));
//               THE PARAMS

                    try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                        HttpEntity responseEntity = response.getEntity();
                        if (responseEntity != null) {
                            String responseString = EntityUtils.toString(responseEntity);
                            Integer statusCode = response.getStatusLine().getStatusCode();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//              here btw i'll CHANGE THE NAME OF CHANNEL IN 'WAITING'
                Main.changeChannelName(event.getGuild().getTextChannelById(event.getChannel().getId()), "waiting");
            }
        }
    }
    public static Boolean checkChannel(String user, long theChannel, String channelName) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://127.0.0.1:8000/api/ticket/get");

//              THE PARAMETERS
            httpPost.setEntity(new UrlEncodedFormEntity(Arrays.asList(
                    new BasicNameValuePair("user", user)
            )));
//               THE PARAMS

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String responseString = EntityUtils.toString(responseEntity);
                    Integer statusCode = response.getStatusLine().getStatusCode();
                    if(statusCode == 200) {
                        JSONArray data = new JSONArray(responseString);
                        JSONArray allCategories = data.getJSONArray(0);
                        Boolean theCategoryIs = false;
                        for (int i = 0; i < allCategories.length(); i++) {
                            JSONObject category = allCategories.getJSONObject(i);
                            if(category.getString("channelId").equals(theChannel)) {
                                theCategoryIs = true;
                            }
                        }
                        if(theCategoryIs) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
