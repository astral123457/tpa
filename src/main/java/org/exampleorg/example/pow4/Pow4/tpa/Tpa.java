package org.exampleorg.example.pow4.Pow4.tpa;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.UUID;

public final class Tpa extends JavaPlugin {

    // Mapa de solicitações de TPA: UUID do jogador alvo -> UUID do jogador que pediu
    private final HashMap<UUID, UUID> tpaRequests = new HashMap<>();

    private static final String FOLDER_PATH = "plugins/Tpa";
    private static final String CONFIG_FILE = FOLDER_PATH + "/config.json";
    private static final String MESSAGES_FILE = FOLDER_PATH + "/messages.json";

    @Override
    public void onEnable() {
        getLogger().info("Plugin TPA habilitado!");
        createFolderAndFiles();
        if (!isPluginEnabled()) {
            getLogger().warning("Plugin desativado via configuração.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // Carregar idioma
        String language = loadLanguage();
        getLogger().info("Idioma configurado: " + language);
    }

    private void createFolderAndFiles() {
        File folder = new File(FOLDER_PATH);
        if (!folder.exists() && folder.mkdirs()) {
            getLogger().info("Pasta de configuração criada em: " + FOLDER_PATH);
        }

        // Criação de config.json
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            try (FileWriter writer = new FileWriter(configFile)) {
                JsonObject defaultConfig = new JsonObject();
                defaultConfig.addProperty("enabled", true);
                defaultConfig.addProperty("language", "br");

                // Gson com Pretty Printing
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(defaultConfig, writer);

                getLogger().info("Arquivo config.json criado com configurações padrão.");
            } catch (IOException e) {
                getLogger().severe("Erro ao criar config.json: " + e.getMessage());
            }
        }

        // Criação de messages.json

        File messagesFile = new File(MESSAGES_FILE);
        if (!messagesFile.exists()) {
            try (FileWriter writer = new FileWriter(messagesFile)) {
                JsonObject messages = new JsonObject();
                

// Mensagem para aceitar TPA
                JsonObject acceptMessages = new JsonObject();
                acceptMessages.addProperty("br", "§b§l  [ACEITAR][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("en", "§b§l  [ACCEPT][§2§l✔§b§l]   §e§l|   ");
                messages.add("accept", acceptMessages);

// Mensagem para recusar TPA
                JsonObject refuseMessages = new JsonObject();
                refuseMessages.addProperty("br", "§4§l[RECUSAR][§c§l✘§4§l]");
                refuseMessages.addProperty("en", "§4§l[REFUSE][§c§l✘§4§l]");
                messages.add("refuse", refuseMessages);

// Mensagem para jogador offline
                JsonObject playerOfflineMessages = new JsonObject();
                playerOfflineMessages.addProperty("br", "§cJogador não encontrado ou está offline.");
                playerOfflineMessages.addProperty("en", "§cPlayer not found or is offline.");
                messages.add("player_offline", playerOfflineMessages);

// Mensagem para envio de solicitação
                JsonObject requestSentMessages = new JsonObject();
                requestSentMessages.addProperty("br", "§aSolicitação de TPA enviada para {player}.");
                requestSentMessages.addProperty("en", "§aTPA request sent to {player}.");
                messages.add("tpa_request_sent", requestSentMessages);

// Mensagem para aceitar solicitação com sucesso
                JsonObject acceptSuccessMessages = new JsonObject();
                acceptSuccessMessages.addProperty("br", "§aVocê aceitou a solicitação de §f§lTPA§a de §d§l{player}.");
                acceptSuccessMessages.addProperty("en", "§aYou accepted the §f§lTPA§a request from §d§l{player}.");
                messages.add("accept_success", acceptSuccessMessages);

// Mensagem para recusar solicitação com sucesso
                JsonObject denySuccessMessages = new JsonObject();
                denySuccessMessages.addProperty("br", "§cVocê recusou a solicitação de §f§lTPA§c de §d§l{player}.");
                denySuccessMessages.addProperty("en", "§cYou denied the §f§lTPA§c request from §d§l{player}.");
                messages.add("deny_success", denySuccessMessages);

                // Gson com Pretty Printing
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(messages, writer);

                getLogger().info("Arquivo messages.json criado com mensagens padrão.");
            } catch (IOException e) {
                getLogger().severe("Erro ao criar messages.json: " + e.getMessage());
            }
        }
    }



    private boolean isPluginEnabled() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));
            JsonObject config = new Gson().fromJson(content, JsonObject.class);
            return config.get("enabled").getAsBoolean();
        } catch (IOException e) {
            getLogger().severe("Erro ao ler config.json: " + e.getMessage());
        }
        return false;
    }

    private String loadLanguage() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));
            JsonObject config = new Gson().fromJson(content, JsonObject.class);
            return config.get("language").getAsString();
        } catch (IOException e) {
            getLogger().severe("Erro ao carregar o idioma do config.json: " + e.getMessage());
        }
        return "br";
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

        MessageManager messageManager = new MessageManager();

        Player player = (Player) sender;

        String language = player.getLocale().toLowerCase();
        if (language.startsWith("pt")) {
            language = "br";
        } else if (language.startsWith("en")) {
            language = "en";
        } else if (language.startsWith("es")) {
            language = "es";
        } else if (language.startsWith("fr")) {
            language = "fr";
        } else if (language.startsWith("de")) {
            language = "de";
        } else {
            language = "default"; // Idioma padrão caso não seja reconhecido
        }

        if (label.equalsIgnoreCase("tpa")) {
            // Comando /tpa
            if (args.length < 1) {
                player.sendMessage(messageManager.getMessage("player_specify", language));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(messageManager.getMessage("player_offline", language));
                return true;
            }

            if (player.getUniqueId().equals(target.getUniqueId())) {
                player.sendMessage(messageManager.getMessage("self_request", language));
                return true;
            }

            // Armazena a solicitação
            tpaRequests.put(target.getUniqueId(), player.getUniqueId());
            player.sendMessage(messageManager.getMessage("tpa_request_sent", language, "player", target.getName() + "."));

            // Envia mensagem interativa para o jogador alvo
            sendInteractiveMessage(target, player);
            return true;

        } else if (label.equalsIgnoreCase("tpaccept")) {
            // Comando /tpaccept
            if (args.length < 1) {
                player.sendMessage(messageManager.getMessage("player_specify", language));
                return true;
            }

            Player requester = Bukkit.getPlayer(args[0]);
            if (requester == null || !requester.isOnline()) {
                player.sendMessage(messageManager.getMessage("player_offline", language));
                return true;
            }

            // Verifica se há uma solicitação pendente
            if (tpaRequests.get(player.getUniqueId()) == null || !tpaRequests.get(player.getUniqueId()).equals(requester.getUniqueId())) {
                player.sendMessage(messageManager.getMessage("no_request_pending", language, "player", requester.getName() + "..."));
                return true;
            }

            // Teletransporta o jogador que pediu
            Location targetLocation = player.getLocation();
            requester.teleport(targetLocation);

            player.sendMessage(messageManager.getMessage("accept_success", language, "player", requester.getName() + "."));
            requester.sendMessage(messageManager.getMessage("accept_notify", language, "player", player.getName() + "."));

            // Remove a solicitação do mapa
            tpaRequests.remove(player.getUniqueId());
            return true;

        } else if (label.equalsIgnoreCase("tpdeny")) {
            // Comando /tpdeny
            if (args.length < 1) {
                player.sendMessage(messageManager.getMessage("player_specify", language));
                return true;
            }

            Player requester = Bukkit.getPlayer(args[0]);
            if (requester == null || !requester.isOnline()) {
                player.sendMessage(messageManager.getMessage("player_offline", language));
                return true;
            }

            // Verifica se há uma solicitação pendente
            if (tpaRequests.get(player.getUniqueId()) == null || !tpaRequests.get(player.getUniqueId()).equals(requester.getUniqueId())) {
                player.sendMessage(messageManager.getMessage("no_request_pending", language, "player", requester.getName()));
                return true;
            }

            // Notifica o jogador
            player.sendMessage(messageManager.getMessage("deny_success", language, "player", requester.getName()));

            requester.sendMessage(messageManager.getMessage("deny_notify", language, "player", player.getName()));

            // Remove a solicitação do mapa
            tpaRequests.remove(player.getUniqueId());
            return true;
        }

        return false;
    }





    private void sendInteractiveMessage(Player target, Player requester) {
        MessageManager messageManager = new MessageManager();
        String language = target.getLocale().toLowerCase();
        if (language.startsWith("pt")) {
            language = "br";
        } else if (language.startsWith("en")) {
            language = "en";
        } else if (language.startsWith("es")) {
            language = "es";
        } else if (language.startsWith("fr")) {
            language = "fr";
        } else if (language.startsWith("de")) {
            language = "de";
        } else {
            language = "default"; // Idioma padrão caso não seja reconhecido
        }

        // Mensagem inicial
        String initialMessage = "§e§l☾☼§d§l" + requester.getName() + " §6" +
                messageManager.getMessage("tpa_request", language); // Traduzido conforme idioma

        TextComponent message = new TextComponent(initialMessage);

        // Botão Aceitar com \n antes
        TextComponent acceptButton = new TextComponent("\n" +
                messageManager.getMessage("accept", language));
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + requester.getName()));

        // Botão Recusar com \n\n no final
        TextComponent denyButton = new TextComponent(
                messageManager.getMessage("refuse", language) + "\n");
        denyButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny " + requester.getName()));

        // Combina os componentes
        message.addExtra("\n"); // Para organizar visualmente
        message.addExtra(acceptButton);
        message.addExtra(denyButton);

        // Envia a mensagem interativa ao jogador alvo
        target.spigot().sendMessage(message);
    }


}
