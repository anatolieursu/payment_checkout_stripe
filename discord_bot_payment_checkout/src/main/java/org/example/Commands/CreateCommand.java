package org.example.Commands;

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

import java.util.Arrays;

public class CreateCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("create")) {
            event.deferReply().queue();
            if(event.getChannel().getName().contains("waiting")) {
                event.getHook().sendMessage("Payment already exist").queue();
            } else {
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
                            if(statusCode == 200) {
                                JSONObject data = new JSONObject(responseString);
                                String paymentId = data.getString("payment_id");
                                event.getHook().sendMessage("Your Payment Link: http://127.0.0.1:8000/payment/"+paymentId).queue();
                            } else {
                                event.getHook().sendMessage("ERROR: " + statusCode).queue();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(event.getChannel().getName().contains("payment")) {
                    event.getChannel().getName().equals("wating-" + event.getChannel().getId());
                }
            }
        }
    }
}
