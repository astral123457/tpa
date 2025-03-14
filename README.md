# Tpa Minecraft 1.21.4

Tpa posibilita teleportar ao jogador amigo mandando 
solicitacao Aceitar ou recuzar
Comando: /tpa <name>

```mermaid
graph TD;
    Inicio-->/tpa;
    /tpa-->Manda_uma_carta_[Aceitar]_[Recusar];
    Manda_uma_carta_[Aceitar]_[Recusar]-->[Aceitar];
    Manda_uma_carta_[Aceitar]_[Recusar]-->[Recusar];

    [Aceitar]-->Fim;
    [Recusar]-->Fim;
```

![image](https://github.com/user-attachments/assets/49b0a862-daa5-418b-8bea-14bf1c3dec4e)


