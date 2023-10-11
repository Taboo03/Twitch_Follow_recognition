package org.masu1129.Twitch_Follow_recognition;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddCreatorCommand implements CommandExecutor {
    private final Twitch_Test plugin;

    public AddCreatorCommand(Twitch_Test plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        Runnable runAddCreator = new Runnable() {
            @Override
            public void run() {
                List<String> creators = AddCreatorCommand.this.plugin.getConfig().getStringList("streamer_names");
                creators.add(args[0]);
                AddCreatorCommand.this.plugin.getConfig().set("streamer_names", creators);
                AddCreatorCommand.this.plugin.saveConfig();
                sender.sendMessage("[Twitch_Test] 스트리머 리스트에 추가되었습니다." + args[0]);
            }
        };
        (new Thread(runAddCreator)).start();
        return true;
    }
}
