package uk.co.jkinc.Vaultier;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HTTP {
    private HttpServer server;

    public void startServer() throws IOException {
        String _port = Vaultier.plugin.getConfig().getString("HTTP-Bind-port");
        if (_port == null) {
            _port = "8080";
        }
        int port = Integer.parseInt(_port);
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/transfer", (HttpExchange exchange) -> {
            if (!Objects.equals(exchange.getRequestMethod(), "POST")) {
                exchange.sendResponseHeaders(405, 0);
                OutputStream os = exchange.getResponseBody();
                os.close();
                return;
            }

            InputStream reqbody = exchange.getRequestBody();
            Gson gson = new Gson();

            try {
                InputStreamReader reader = new InputStreamReader(reqbody);

                JsonElement element = gson.fromJson(reader, JsonElement.class);
                JsonObject obj = element.getAsJsonObject();

                if (obj.get("Key") == null) {
                    String response = "{\"error\":\"Missing API key\"}";
                    exchange.sendResponseHeaders(400, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    return;
                }

                String apiKey = obj.get("Key").getAsString();

                // I almost forgot to do this...

                UUID playerUUID;

                playerUUID = Vaultier.database.db.APIKeys.get(Key.bytesToHex(Hash.hashAPIKey(apiKey)));
                if (playerUUID == null) {
                    String response = "{\"error\":\"Invalid API key\"}";
                    exchange.sendResponseHeaders(400, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    return;
                }
                Player Initator = Bukkit.getPlayer(playerUUID);


                if (obj.get("Player") == null || Bukkit.getPlayer(obj.get("Player").getAsString()) == null) {
                    String response = "{\"error\":\"Unknown or offline player\"}";
                    exchange.sendResponseHeaders(400, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    return;
                }
                String playerToSend = obj.get("Player").getAsString();


                if (obj.get("Value") == null) {
                    String response = "{\"error\":\"Invalid transfer amount\"}";
                    exchange.sendResponseHeaders(400, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    return;
                }
                int AmountToTransfer = obj.get("Value").getAsInt();

                reader.close();
                reqbody.close();

                Transaction transaction = new Transaction();

                String id = Key.genTransactionID();

                transaction.Payee = Initator;
                transaction.Payer = Bukkit.getPlayer(obj.get("Player").getAsString());
                transaction.value = AmountToTransfer;

                Vaultier.database.db.Transactions.put(id, transaction);

                String response = "{\"error\": null, \"id\": \"" + id + "\"}";
                exchange.sendResponseHeaders(200, response.length());

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

                Bukkit.getPlayer(obj.get("Player").getAsString()).spigot().sendMessage(new
                        TextComponent("----------------------------"));

                Bukkit.getPlayer(obj.get("Player").getAsString()).spigot().sendMessage(new
                        TextComponent(""));

                TextComponent message = new TextComponent( ChatColor.YELLOW + Initator.getDisplayName() +
                        ChatColor.AQUA + " is requesting " + ChatColor.YELLOW + "$" + AmountToTransfer +
                        ChatColor.AQUA + " from your account.");

                Bukkit.getPlayer(obj.get("Player").getAsString()).spigot().sendMessage(message);

                Bukkit.getPlayer(obj.get("Player").getAsString()).spigot().sendMessage(new
                        TextComponent(""));

                Bukkit.getPlayer(obj.get("Player").getAsString()).spigot().sendMessage(new
                        TextComponent("----------------------------"));

                Bukkit.getPlayer(obj.get("Player").getAsString()).spigot().sendMessage(new
                        TextComponent(""));

                TextComponent accept = new TextComponent( ChatColor.GREEN + " [ ACCEPT ] ");

                TextComponent deny = new TextComponent( ChatColor.RED + " [ DENY ] ");

                accept.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/accepttransfer " + id ) );
                deny.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/denytransfer " + id ) );

                Bukkit.getPlayer(obj.get("Player").getAsString()).spigot().sendMessage(accept);
                Bukkit.getPlayer(obj.get("Player").getAsString()).spigot().sendMessage(deny);

                Bukkit.getPlayer(obj.get("Player").getAsString()).spigot().sendMessage(new
                        TextComponent(""));

                Bukkit.getPlayer(obj.get("Player").getAsString()).spigot().sendMessage(new
                        TextComponent("----------------------------"));

                Bukkit.getScheduler().runTaskLater(Vaultier.plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (Vaultier.database.db.Transactions.get(id).state == Transaction.Status.PENDING) {
                            Bukkit.getPlayer(obj.get("Player").getAsString()).spigot().sendMessage(new
                                    TextComponent(ChatColor.RED + "Request timed out"));
                            Vaultier.database.db.Transactions.remove(id);
                        }
                    }
                }, 1200L);

            } catch (Exception e) {
                String response = "{\"error\":\"Bad request\"}";
                exchange.sendResponseHeaders(400, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }
        });
        server.setExecutor(null);
        server.start();
    }

    public void stopServer() {
        if(server != null) {
            server.stop(0);
        }
    }
}
