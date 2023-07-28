import discord
from discord.ext import commands
import discord.ext



def discord_bot(client_key):
    intents = discord.Intents.all()
    intents.members = True
    intents.messages = True
    client = discord.Client(intents=intents)



    @client.event
    async def on_ready():
        print(f"{client.user} IS READY")

    @client.event
    async def on_message(message):
        if(message.content == 'Salut'):
            await message.channel.send('Salut')


        print(f"{message.author} | {message.content}")

    client.run(client_key)
