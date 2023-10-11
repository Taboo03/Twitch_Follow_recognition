
package org.masu1129.Twitch_Follow_recognition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpdateBorderCommand implements CommandExecutor {
    private final Twitch_Test plugin;

    public UpdateBorderCommand(Twitch_Test plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        Runnable runUpdateBorder = new Runnable() {
            public void run() {
                if (sender instanceof Player)
                    sender.sendMessage("[트위치 팔로우 알림] 데이터 가져오는 중...");
                int total_followers = -1;
                List<String> streamers = UpdateBorderCommand.this.plugin.getConfig().getStringList("streamer_names");

                for (String s : streamers) {
                    int s_followers = 0;
                    try {
                        s_followers = Integer.parseInt(UpdateBorderCommand.this.getStreamerFollows(s));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    total_followers += s_followers;
                    if (sender instanceof Player)
                        sender.sendMessage("[트위치 팔로우 알림] " + s + " 는 " + s_followers + "명의 팔로워가 있습니다.");
                }
                /*
                if (total_followers >= 0) {
                    int last_border_size = Integer.parseInt(UpdateBorderCommand.this.plugin.getConfig().getString("current-border-size"));
                    int new_border_size = total_followers /
                            Integer.parseInt(UpdateBorderCommand.this.plugin.getConfig().getString("followers-per-block"));
                    if (sender instanceof Player)
                        sender.sendMessage("[트위치 팔로우 알림] BorderSize: " + new_border_size);
                    if (last_border_size != new_border_size) {
                        UpdateBorderCommand.this.plugin.getConfig().set("current-border-size", Integer.valueOf(new_border_size));
                        UpdateBorderCommand.this.plugin.saveConfig();
                        for (Player p : Bukkit.getServer().getOnlinePlayers())
                            p.sendMessage("[ScorchBorder] New follower count! Added blocks: " + (
                                    new_border_size - last_border_size));
                    }
                } else {
                    for (Player p : Bukkit.getServer().getOnlinePlayers())
                        p.sendMessage("[ScorchBorder] Something went wrong! Please report this!");
                }
                 */
            }
        };
        (new Thread(runUpdateBorder)).start();
        return true;
    }

    public boolean runGetFollowers(CommandSender sender, String[] args) throws IOException {
        String response;
        if (args.length > 0) {
            response = getStreamerFollows(args[0]);
        } else {
            response = "Please provide a name";
            return false;
        }
        sender.sendMessage(response);
        return true;
    }

    public String getStreamerFollows(String name) throws IOException {
        String streamer_id = getStreamerID(name);
        String streamer_follows = null;
        if (streamer_id != "0") {
            String result = getTwitchAPIResult("https://api.twitch.tv/helix/channels/followers?first=1&broadcaster_id=" + streamer_id);
            try {
                streamer_follows = result.split(":")[1].split(",")[0];
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                streamer_id = "0";
            }
        } else {
            streamer_follows = "-1";
        }
        return streamer_follows;
    }

    public String getStreamerID(String name) throws IOException {
        String streamer_id, result = " ";
        result = getTwitchAPIResult("https://api.twitch.tv/helix/users?login=" + name);
        try {
            streamer_id = result.split("id\":")[1].split("\"")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            streamer_id = "0";
        }
        return streamer_id;
    }

    public String getTwitchAPIResult(String url) throws IOException, NoRouteToHostException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.setRequestProperty("Client-Id", this.plugin.getConfig().getString("Client-Id"));
        con.setRequestProperty("Authorization", this.plugin.getConfig().getString("Authorization"));

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        String resultXmlText = "";
        while ((inputLine = in.readLine()) != null) {
            resultXmlText += inputLine;
        }
        in.close();
        con.disconnect();
        return resultXmlText;
    }
}
