package org.masu1129.Twitch_Follow_recognition;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RemoveCreatorCommand implements CommandExecutor {
    private final Twitch_Test plugin;

    public RemoveCreatorCommand(Twitch_Test plugin) {       // 그러면 이게 생성자..?

        this.plugin = plugin;
    }

    public boolean onCommand(final CommandSender sender, Command cmd, String label,final String[] args) {
        Runnable runRemoveCreator = new Runnable() {
            @Override
            public void run() {
                List<String> creators = RemoveCreatorCommand.this.plugin.getConfig().getStringList("streamer_names");
                creators.remove(args[0]);
                RemoveCreatorCommand.this.plugin.getConfig().set("streamer_names", creators);
                sender.sendMessage("[Test_Twitch] 스트리머 리스트에서 삭제되었습니다 : " + args[0]);
            }
        };
        (new Thread(runRemoveCreator)).start();
        return true;
    }
}
