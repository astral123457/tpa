package org.exampleorg.example.pow4.Pow4.tpa;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    // cores ANSI mais comuns que você pode usar no console Java
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_DARK_BLUE = "\u001B[94m";

    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m"; // Cor aqua
    private static final String ANSI_DARK_CYAN = "\u001B[96m";


    private static final String ANSI_WHITE = "\u001B[37m";

    private static final String ANSI_DARK_GRAY = "\u001B[90m";// (Cinza escuro/Negrito)
    private static final String ANSI__GRAY = "\u001B[37m";// (Cinza claro/Branco)

    @Override
    public void onEnable() {
        getLogger().info(ANSI_CYAN + "Plugin TPA habilitado!" + ANSI_RESET);
        //getLogger().info("Plugin TPA habilitado!");
        getLogger().info(ANSI_CYAN+"  ___   __    __"+ ANSI_RESET);
        getLogger().info(ANSI_CYAN+"   |   "+ANSI_DARK_BLUE+"|"+ANSI_BLUE+"__)  |__|  "+ANSI_GREEN+"Tpa v1.6" + ANSI_RESET);
        getLogger().info(ANSI_CYAN+"   |   "+ANSI_DARK_BLUE+"|     |  |  "+ANSI_YELLOW+"Spigot on Bukkit - CraftBukkit" + ANSI_RESET);
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

                getLogger().info("Arquivo config.json criado.");
            } catch (IOException e) {
                getLogger().severe("Erro ao criar config.json: "+ e.getMessage());
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
                acceptMessages.addProperty("es", "§b§l  [ACEPTAR][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("fr", "§b§l  [ACCEPTER][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("de", "§b§l  [AKZEPTIEREN][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("ru", "§b§l  [ПРИНЯТЬ][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("zh", "§b§l  [接受][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("zh-tw", "§b§l  [接受][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("ja", "§b§l  [承認][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("ko", "§b§l  [수락][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("it", "§b§l  [ACCETTARE][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("nl", "§b§l  [ACCEPTEREN][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("pl", "§b§l  [AKCEPTUJ][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("sv", "§b§l  [ACCEPTERA][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("cs", "§b§l  [PŘIJMOUT][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("hu", "§b§l  [ELFOGAD][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("tr", "§b§l  [KABUL][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("ar", "§b§l  [قبول][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("fi", "§b§l  [HYVÄKSY][§2§l✔§b§l]   §e§l|   ");
                acceptMessages.addProperty("da", "§b§l  [ACCEPTER][§2§l✔§b§l]   §e§l|   ");
                messages.add("accept", acceptMessages);


// Mensagem para recusar TPA
                JsonObject refuseMessages = new JsonObject();
                refuseMessages.addProperty("br", "§4§l[RECUSAR][§c§l✘§4§l]");
                refuseMessages.addProperty("en", "§4§l[REFUSE][§c§l✘§4§l]");
                refuseMessages.addProperty("es", "§4§l[RECHAZAR][§c§l✘§4§l]");
                refuseMessages.addProperty("fr", "§4§l[REFUSER][§c§l✘§4§l]");
                refuseMessages.addProperty("de", "§4§l[ABLEHNEN][§c§l✘§4§l]");
                refuseMessages.addProperty("ru", "§4§l[ОТКАЗАТЬ][§c§l✘§4§l]");
                refuseMessages.addProperty("zh", "§4§l[拒绝][§c§l✘§4§l]");
                refuseMessages.addProperty("zh-tw", "§4§l[拒絕][§c§l✘§4§l]");
                refuseMessages.addProperty("ja", "§4§l[拒否][§c§l✘§4§l]");
                refuseMessages.addProperty("ko", "§4§l[거절][§c§l✘§4§l]");
                refuseMessages.addProperty("it", "§4§l[RIFIUTARE][§c§l✘§4§l]");
                refuseMessages.addProperty("nl", "§4§l[WEIGEREN][§c§l✘§4§l]");
                refuseMessages.addProperty("pl", "§4§l[ODRZUCIĆ][§c§l✘§4§l]");
                refuseMessages.addProperty("sv", "§4§l[AVBÖJA][§c§l✘§4§l]");
                refuseMessages.addProperty("cs", "§4§l[ODMÍTNOUT][§c§l✘§4§l]");
                refuseMessages.addProperty("hu", "§4§l[ELUTASÍT][§c§l✘§4§l]");
                refuseMessages.addProperty("tr", "§4§l[REDDET][§c§l✘§4§l]");
                refuseMessages.addProperty("ar", "§4§l[رَفْض][§c§l✘§4§l]");
                refuseMessages.addProperty("fi", "§4§l[KIELTÄYTYÄ][§c§l✘§4§l]");
                refuseMessages.addProperty("da", "§4§l[AFSLÅ][§c§l✘§4§l]");
                messages.add("refuse", refuseMessages);


// Mensagem para jogador offline
                JsonObject playerOfflineMessages = new JsonObject();
                playerOfflineMessages.addProperty("br", "§cJogador não encontrado ou está offline.");
                playerOfflineMessages.addProperty("en", "§cPlayer not found or is offline.");
                playerOfflineMessages.addProperty("es", "§cJugador no encontrado o está desconectado.");
                playerOfflineMessages.addProperty("fr", "§cJoueur introuvable ou hors ligne.");
                playerOfflineMessages.addProperty("de", "§cSpieler nicht gefunden oder offline.");
                playerOfflineMessages.addProperty("ru", "§cИгрок не найден или находится оффлайн.");
                playerOfflineMessages.addProperty("zh", "§c未找到玩家或处于离线状态。");
                playerOfflineMessages.addProperty("zh-tw", "§c未找到玩家或處於離線狀態。");
                playerOfflineMessages.addProperty("ja", "§cプレイヤーが見つからないか、オフラインです。");
                playerOfflineMessages.addProperty("ko", "§c플레이어를 찾을 수 없거나 오프라인 상태입니다.");
                playerOfflineMessages.addProperty("it", "§cGiocatore non trovato o è offline.");
                playerOfflineMessages.addProperty("nl", "§cSpeler niet gevonden of is offline.");
                playerOfflineMessages.addProperty("pl", "§cGracz nie znaleziony lub jest offline.");
                playerOfflineMessages.addProperty("sv", "§cSpelaren hittades inte eller är offline.");
                playerOfflineMessages.addProperty("cs", "§cHráč nenalezen nebo je offline.");
                playerOfflineMessages.addProperty("hu", "§cA játékos nem található vagy offline állapotban van.");
                playerOfflineMessages.addProperty("tr", "§cOyuncu bulunamadı veya çevrimdışı.");
                playerOfflineMessages.addProperty("ar", "§cاللاعب غير موجود أو غير متصل.");
                playerOfflineMessages.addProperty("fi", "§cPelaajaa ei löytynyt tai hän on offline-tilassa.");
                playerOfflineMessages.addProperty("da", "§cSpilleren blev ikke fundet eller er offline.");
                messages.add("player_offline", playerOfflineMessages);


// Mensagem para envio de solicitação
                JsonObject requestSentMessages = new JsonObject();
                requestSentMessages.addProperty("br", "§aSolicitação de §f§lTPA§a enviada para §d§l{player}.");
                requestSentMessages.addProperty("en", "§f§lTPA§a request sent to §d§l{player}.");
                requestSentMessages.addProperty("es", "§aSolicitud de §f§lTPA§a enviada a §d§l{player}.");
                requestSentMessages.addProperty("fr", "§aRequête de §f§lTPA§a envoyée à §d§l{player}.");
                requestSentMessages.addProperty("de", "§aTPA-Anfrage §f§lversendet an §d§l{player}.");
                requestSentMessages.addProperty("ru", "§aЗапрос §f§lTPA§a отправлен игроку §d§l{player}.");
                requestSentMessages.addProperty("zh", "§a已向 §d§l{player} 发送 §f§lTPA§a 请求。");
                requestSentMessages.addProperty("zh-tw", "§a已向 §d§l{player} 發送 §f§lTPA§a 請求。");
                requestSentMessages.addProperty("ja", "§a§f§lTPA§a リクエストが §d§l{player} に送信されました。");
                requestSentMessages.addProperty("ko", "§a§f§lTPA§a 요청이 §d§l{player} 에게 전송되었습니다.");
                requestSentMessages.addProperty("it", "§aRichiesta di §f§lTPA§a inviata a §d§l{player}.");
                requestSentMessages.addProperty("nl", "§aTPA-aanvraag §f§lverzonden naar §d§l{player}.");
                requestSentMessages.addProperty("pl", "§aProśba o §f§lTPA§a została wysłana do §d§l{player}.");
                requestSentMessages.addProperty("sv", "§a§f§lTPA§a-förfrågan skickad till §d§l{player}.");
                requestSentMessages.addProperty("cs", "§aŽádost o §f§lTPA§a byla odeslána hráči §d§l{player}.");
                requestSentMessages.addProperty("hu", "§aA §f§lTPA§a kérés elküldve §d§l{player} számára.");
                requestSentMessages.addProperty("tr", "§a§f§lTPA§a isteği §d§l{player}'a gönderildi.");
                requestSentMessages.addProperty("ar", "§aتم إرسال طلب §f§lTPA§a إلى §d§l{player}.");
                requestSentMessages.addProperty("fi", "§a§f§lTPA§a-pyyntö lähetetty §d§l{player}.");
                requestSentMessages.addProperty("da", "§a§f§lTPA§a-anmodning sendt til §d§l{player}.");
                messages.add("tpa_request_sent", requestSentMessages);


// Mensagem para aceitar solicitação com sucesso
                JsonObject acceptSuccessMessages = new JsonObject();
                acceptSuccessMessages.addProperty("br", "§aVocê aceitou a solicitação de §f§lTPA§a de §d§l{player}.");
                acceptSuccessMessages.addProperty("en", "§aYou accepted the §f§lTPA§a request from §d§l{player}.");
                acceptSuccessMessages.addProperty("es", "§aAceptaste la solicitud de §f§lTPA§a de §d§l{player}.");
                acceptSuccessMessages.addProperty("fr", "§aVous avez accepté la demande de §f§lTPA§a de §d§l{player}.");
                acceptSuccessMessages.addProperty("de", "§aSie haben die §f§lTPA§a-Anfrage von §d§l{player} akzeptiert.");
                acceptSuccessMessages.addProperty("ru", "§aВы приняли запрос §f§lTPA§a от §d§l{player}.");
                acceptSuccessMessages.addProperty("zh", "§a您已接受 §f§lTPA§a 请求来自 §d§l{player}。");
                acceptSuccessMessages.addProperty("zh-tw", "§a您已接受 §f§lTPA§a 請求來自 §d§l{player}。");
                acceptSuccessMessages.addProperty("ja", "§a§f§lTPA§a リクエストを §d§l{player} から承認しました。");
                acceptSuccessMessages.addProperty("ko", "§a§f§lTPA§a 요청을 §d§l{player} 에게서 수락했습니다.");
                acceptSuccessMessages.addProperty("it", "§aHai accettato la richiesta di §f§lTPA§a da §d§l{player}.");
                acceptSuccessMessages.addProperty("nl", "§aJe hebt het §f§lTPA§a-verzoek van §d§l{player} geaccepteerd.");
                acceptSuccessMessages.addProperty("pl", "§aZaakceptowałeś prośbę §f§lTPA§a od §d§l{player}.");
                acceptSuccessMessages.addProperty("sv", "§aDu har accepterat §f§lTPA§a-förfrågan från §d§l{player}.");
                acceptSuccessMessages.addProperty("cs", "§aPřijali jste požadavek §f§lTPA§a od §d§l{player}.");
                acceptSuccessMessages.addProperty("hu", "§aElfogadtad a §f§lTPA§a kérést §d§l{player}-től.");
                acceptSuccessMessages.addProperty("tr", "§a§f§lTPA§a isteğini §d§l{player}'dan kabul ettiniz.");
                acceptSuccessMessages.addProperty("ar", "§aلقد قبلت طلب §f§lTPA§a من §d§l{player}.");
                acceptSuccessMessages.addProperty("fi", "§aHyväksyit §f§lTPA§a-pyynnön henkilöltä §d§l{player}.");
                acceptSuccessMessages.addProperty("da", "§aDu har accepteret §f§lTPA§a-anmodningen fra §d§l{player}.");
                messages.add("accept_success", acceptSuccessMessages);


// Mensagem para recusar solicitação com sucesso
                JsonObject denySuccessMessages = new JsonObject();
                denySuccessMessages.addProperty("br", "§cVocê recusou a solicitação de §f§lTPA§c de §d§l{player}."); // Português (BR)
                denySuccessMessages.addProperty("en", "§cYou denied the §f§lTPA§c request from §d§l{player}."); // Inglês (EN)
                denySuccessMessages.addProperty("es", "§cRechazaste la solicitud de §f§lTPA§c de §d§l{player}."); // Espanhol (ES)
                denySuccessMessages.addProperty("fr", "§cVous avez refusé la demande de §f§lTPA§c de §d§l{player}."); // Francês (FR)
                denySuccessMessages.addProperty("de", "§cSie haben die §f§lTPA§c-Anfrage von §d§l{player} abgelehnt."); // Alemão (DE)
                denySuccessMessages.addProperty("ru", "§cВы отклонили запрос §f§lTPA§c от §d§l{player}."); // Russo (RU)
                denySuccessMessages.addProperty("zh", "§c您拒绝了来自 §d§l{player} 的 §f§lTPA§c 请求。"); // Chinês Simplificado (ZH)
                denySuccessMessages.addProperty("zh-tw", "§c您拒絕了來自 §d§l{player} 的 §f§lTPA§c 請求。"); // Chinês Tradicional (ZH-TW)
                denySuccessMessages.addProperty("ja", "§c{player} からの §f§lTPA§c リクエストを拒否しました。"); // Japonês (JA)
                denySuccessMessages.addProperty("ko", "§c{player}의 §f§lTPA§c 요청을 거부했습니다."); // Coreano (KO)
                denySuccessMessages.addProperty("it", "§cHai rifiutato la richiesta di §f§lTPA§c da §d§l{player}."); // Italiano (IT)
                denySuccessMessages.addProperty("nl", "§cJe hebt het §f§lTPA§c-verzoek van §d§l{player} geweigerd."); // Holandês (NL)
                denySuccessMessages.addProperty("pl", "§cOdrzuciłeś prośbę o §f§lTPA§c od §d§l{player}."); // Polonês (PL)
                denySuccessMessages.addProperty("sv", "§cDu nekade §f§lTPA§c-begäran från §d§l{player}."); // Sueco (SV)
                denySuccessMessages.addProperty("cs", "§cOdmítli jste žádost §f§lTPA§c od §d§l{player}."); // Tcheco (CS)
                denySuccessMessages.addProperty("hu", "§cElutasítottad a(z) §f§lTPA§c kérést a(z) §d§l{player} nevű játékostól."); // Húngaro (HU)
                denySuccessMessages.addProperty("tr", "§c{player} adlı kişiden gelen §f§lTPA§c talebini reddettiniz."); // Turco (TR)
                denySuccessMessages.addProperty("ar", "§cلقد رفضت طلب §f§lTPA§c من §d§l{player}."); // Árabe (AR)
                denySuccessMessages.addProperty("fi", "§cHylkäsit §f§lTPA§c-pyynnön käyttäjältä §d§l{player}."); // Finlandês (FI)
                denySuccessMessages.addProperty("da", "§cDu afviste §f§lTPA§c-anmodningen fra §d§l{player}."); // Dinamarquês (DA)
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
                    language = "en"; // Inglês
                } else if (language.startsWith("es")) {
                    language = "es"; // Espanhol
                } else if (language.startsWith("fr")) {
                    language = "fr"; // Francês
                } else if (language.startsWith("de")) {
                    language = "de"; // Alemão
                } else if (language.startsWith("ru")) {
                    language = "ru"; // Russo
                } else if (language.startsWith("zh")) {
                    language = "zh"; // Chinês Simplificado
                } else if (language.startsWith("zh-tw")) {
                    language = "zh-tw"; // Chinês Tradicional
                } else if (language.startsWith("ja")) {
                    language = "ja"; // Japonês
                } else if (language.startsWith("ko")) {
                    language = "ko"; // Coreano
                } else if (language.startsWith("it")) {
                    language = "it"; // Italiano
                } else if (language.startsWith("nl")) {
                    language = "nl"; // Holandês
                } else if (language.startsWith("pl")) {
                    language = "pl"; // Polonês
                } else if (language.startsWith("sv")) {
                    language = "sv"; // Sueco
				} else if (language.startsWith("cs")) {
                    language = "cs"; // Tcheco
				} else if (language.startsWith("hu")) {
                    language = "hu"; // Húngaro
				} else if (language.startsWith("tr")) {
                    language = "tr"; // Turco
				} else if (language.startsWith("ar")) {
                    language = "ar"; // Árabe
				} else if (language.startsWith("fi")) {
                    language = "fi"; // Finlandês
				} else if (language.startsWith("da")) {
                    language = "da"; // Dinamarquês
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
                    language = "en"; // Inglês
                } else if (language.startsWith("es")) {
                    language = "es"; // Espanhol
                } else if (language.startsWith("fr")) {
                    language = "fr"; // Francês
                } else if (language.startsWith("de")) {
                    language = "de"; // Alemão
                } else if (language.startsWith("ru")) {
                    language = "ru"; // Russo
                } else if (language.startsWith("zh")) {
                    language = "zh"; // Chinês Simplificado
                } else if (language.startsWith("zh-tw")) {
                    language = "zh-tw"; // Chinês Tradicional
                } else if (language.startsWith("ja")) {
                    language = "ja"; // Japonês
                } else if (language.startsWith("ko")) {
                    language = "ko"; // Coreano
                } else if (language.startsWith("it")) {
                    language = "it"; // Italiano
                } else if (language.startsWith("nl")) {
                    language = "nl"; // Holandês
                } else if (language.startsWith("pl")) {
                    language = "pl"; // Polonês
                } else if (language.startsWith("sv")) {
                    language = "sv"; // Sueco
				} else if (language.startsWith("cs")) {
                    language = "cs"; // Tcheco
				} else if (language.startsWith("hu")) {
                    language = "hu"; // Húngaro
				} else if (language.startsWith("tr")) {
                    language = "tr"; // Turco
				} else if (language.startsWith("ar")) {
                    language = "ar"; // Árabe
				} else if (language.startsWith("fi")) {
                    language = "fi"; // Finlandês
				} else if (language.startsWith("da")) {
                    language = "da"; // Dinamarquês
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
