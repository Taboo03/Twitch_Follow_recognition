package org.masu1129.Twitch_Follow_recognition;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GetCreatorCommand implements CommandExecutor {
    private final Twitch_Test plugin;

    public GetCreatorCommand(Twitch_Test plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        Runnable runGetCreators = new Runnable() {
            public void run() {
                List<String> creators = GetCreatorCommand.this.plugin.getConfig().getStringList("streamer_names");
                sender.sendMessage("등록된 스트리머:");
                for (String creator : creators)
                    sender.sendMessage(creator);
            }
        };
        (new Thread(runGetCreators)).start();
        return true;
    }
}