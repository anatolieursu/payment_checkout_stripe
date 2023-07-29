package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.Commands.CreateCommand;


public class Main extends ListenerAdapter {
    public static JDA jda;
    public static void main( String[] args )
    {
        jda = JDABuilder.createDefault("MTEzNDQwMTI0OTY1MjAwMjg2Ng.G5T4WQ.7tksZacBs8T1vc_eE5qv5A2JxM5joCLDAtDQH0")
                .setActivity(Activity.playing("orice"))
                .addEventListeners(new Main())
                .addEventListeners(new CreateCommand())
                .addEventListeners(new AllCommands())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        Guild guild = jda.getGuildById(1096672836153782362L);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMessage().getAuthor().isBot()) return;
        if(event.getMessage().getContentRaw() .equals("create")) {
            System.out.println("create");
        }
    }
}