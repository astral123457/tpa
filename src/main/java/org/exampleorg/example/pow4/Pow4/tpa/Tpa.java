package org.exampleorg.example.pow4.Pow4.tpa;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Tpa extends JavaPlugin {

    // Mapa de solicitações de TPA: UUID do jogador alvo -> UUID do jogador que pediu
    private final HashMap<UUID, UUID> tpaRequests = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Plugin TPA habilitado!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin TPA desabilitado!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("tpa")) {
            // Comando /tpa
            if (args.length < 1) {
                player.sendMessage("§cPor favor, especifique o nome de um jogador.");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage("§cJogador não encontrado ou está offline.");
                return true;
            }

            if (player.getUniqueId().equals(target.getUniqueId())) {
                player.sendMessage("§cVocê não pode enviar uma solicitação de TPA para si mesmo!");
                return true;
            }

            // Armazena a solicitação
            tpaRequests.put(target.getUniqueId(), player.getUniqueId());
            player.sendMessage("§aSolicitação de TPA enviada para " + target.getName() + ".");

            // Envia mensagem interativa para o jogador alvo
            sendInteractiveMessage(target, player);
            return true;

        } else if (label.equalsIgnoreCase("tpaccept")) {
            // Comando /tpaccept
            if (args.length < 1) {
                player.sendMessage("§cEspecifique quem enviou a solicitação.");
                return true;
            }

            Player requester = Bukkit.getPlayer(args[0]);
            if (requester == null || !requester.isOnline()) {
                player.sendMessage("§cO jogador não está mais online.");
                return true;
            }

            // Verifica se há uma solicitação pendente
            if (tpaRequests.get(player.getUniqueId()) == null || !tpaRequests.get(player.getUniqueId()).equals(requester.getUniqueId())) {
                player.sendMessage("§cNenhuma solicitação pendente de " + requester.getName() + ".");
                return true;
            }

            // Teletransporta o jogador que pediu
            Location targetLocation = player.getLocation();
            requester.teleport(targetLocation);
            player.sendMessage("§aVocê aceitou a solicitação de TPA de " + requester.getName() + ".");
            requester.sendMessage("§aSua solicitação de TPA foi aceita por " + player.getName() + ".");

            // Remove a solicitação do mapa
            tpaRequests.remove(player.getUniqueId());
            return true;

        } else if (label.equalsIgnoreCase("tpdeny")) {
            // Comando /tpdeny
            if (args.length < 1) {
                player.sendMessage("§cEspecifique quem enviou a solicitação.");
                return true;
            }

            Player requester = Bukkit.getPlayer(args[0]);
            if (requester == null || !requester.isOnline()) {
                player.sendMessage("§cO jogador não está mais online.");
                return true;
            }

            // Verifica se há uma solicitação pendente
            if (tpaRequests.get(player.getUniqueId()) == null || !tpaRequests.get(player.getUniqueId()).equals(requester.getUniqueId())) {
                player.sendMessage("§cNenhuma solicitação pendente de " + requester.getName() + ".");
                return true;
            }

            // Notifica o jogador
            player.sendMessage("§cVocê recusou a solicitação de TPA de " + requester.getName() + ".");
            requester.sendMessage("§cSua solicitação de TPA foi recusada por " + player.getName() + ".");

            // Remove a solicitação do mapa
            tpaRequests.remove(player.getUniqueId());
            return true;
        }

        return false;
    }

    private void sendInteractiveMessage(Player target, Player requester) {
        // Texto inicial
        TextComponent message = new TextComponent("§e" + requester.getName() + " quer se teletransportar para você. Aceitar? ");

        // Botão Aceitar
        TextComponent acceptButton = new TextComponent("\n§b§l  [ACEITAR §2✔§b§l] §e| ");
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + requester.getName()));

        // Botão Recusar
        TextComponent denyButton = new TextComponent(" §4§l[RECUSAR §c✘§4§l]");
        denyButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny " + requester.getName()));

        // Combina os componentes
        message.addExtra(acceptButton);
        message.addExtra(denyButton);

        // Envia a mensagem para o jogador alvo
        target.spigot().sendMessage(message);
    }
}
