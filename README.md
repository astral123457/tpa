# Tpa Minecraft 1.21.4

Tpa posibilita teleportar ao jogador amigo mandando

solicitacao Aceitar ou recuzar

# Language

Support EN e BR Automatic

Support enable & disable Plugin

Comando: /tpa <name>



![image](https://github.com/user-attachments/assets/49b0a862-daa5-418b-8bea-14bf1c3dec4e)

```mermaid
graph TD;
    Inicio-->/tpa;
    /tpa-->Manda_uma_carta_Aceitar_Recusar;
    Manda_uma_carta_Aceitar_Recusar-->Aceitar;
    Manda_uma_carta_Aceitar_Recusar-->Recusar;

    Aceitar-->Fim;
    Recusar-->Fim;
```


