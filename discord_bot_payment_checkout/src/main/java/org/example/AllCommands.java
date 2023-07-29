package org.example;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
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

public class AllCommands extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        Guild guild = Main.jda.getGuildById(1096672836153782362L);
        guild.updateCommands().addCommands(
                Commands.slash("create", "Create a payment.")
                        .addOption(OptionType.STRING, "payment_name", "Add a name for your payment", true)
                        .addOption(OptionType.INTEGER, "payment_price", "The payment quote.", true)
        ).queue();
    }
}
