import discord
from discord.ext import commands
import discord.ext
import requests
from discord import app_commands


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
        if(message.author == client.user):
            return

        if(message.content == 'Salut'):
            create_params = {
                'payment_name': 'Salut',
                'payment_price': 10,
                'user': str(message.author)
            }
            response = requests.post('http://127.0.0.1:8000/api/create', json=create_params)

            if response.status_code == 200:
                data = response.json()
                paymentId = data['payment_id']

            await message.channel.send(f'Am facut tot posibilul si mi-a iesit: {response}. PaymentID: {paymentId} \nLink: http://127.0.0.1:8000/payment/{paymentId}')

        if(message.content == '!get'):
            response = requests.get(f'http://127.0.0.1:8000/api/get/{message.author}')
            if response.status_code == 200:
                data = response.json()
                theContent = ''
                thePaymentsList = data[0]

                count = 1
                if(thePaymentsList):
                    for payment in thePaymentsList:
                        theContent += f"\n{count}. {payment['payment_name']}"
                        count += 1
                await message.channel.send('Total Payments: ' + theContent);


        print(f"{message.author} | {message.content}")

    client.run(client_key)
