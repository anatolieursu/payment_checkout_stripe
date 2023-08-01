package org.example.Commands.ticketPayments;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class UpdateTicket extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("ticket_update")) {
            event.deferReply();
            if(event.getChannel().getName().contains("waiting")) {
                String userName = event.getUser().getName();
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

                                Integer buyers = 0;
                                event.getHook().sendMessage(buyers.toString()).queue();
                            } else {
                                event.getHook().sendMessage("ERROR: " + statusCode).queue();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                event.getHook().sendMessage("Done!").queue();
            } else {
                event.getHook().sendMessage("This command is just for payment in tickets!").queue();
            }
        }
    }
}
