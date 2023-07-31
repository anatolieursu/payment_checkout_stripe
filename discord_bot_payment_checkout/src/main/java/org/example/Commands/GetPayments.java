package org.example.Commands;

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
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.event.ActionListener;
import java.util.Arrays;

public class GetPayments extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("get")) {
            String userName = event.getUser().getName();

            event.deferReply().queue();

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet getHTTP = new HttpGet("http://127.0.0.1:8000/api/get/" + userName);

                try (CloseableHttpResponse response = httpClient.execute(getHTTP)) {
                    HttpEntity responseEntity = response.getEntity();
                    if (responseEntity != null) {
                        String responseString = EntityUtils.toString(responseEntity);
                        Integer statusCode = response.getStatusLine().getStatusCode();
                        if(statusCode == 200) {
                            JSONArray data = new JSONArray(responseString);
                            JSONArray allCategories = data.getJSONArray(0);

                            String messageToSend = "";

                            for (int i = 0; i < allCategories.length(); i++) {
                                JSONObject category = allCategories.getJSONObject(i);
                                messageToSend += "Payment Name: " + category.getString("payment_name") + "   Link: http://127.0.0.1:8000/payment/" + category.getString("payment_id") + "   Status: "  + category.getString("status") + "\n";
                            }

                            event.getHook().sendMessage(messageToSend).queue();
                        } else {
                            event.getHook().sendMessage("ERROR: " + statusCode).queue();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
