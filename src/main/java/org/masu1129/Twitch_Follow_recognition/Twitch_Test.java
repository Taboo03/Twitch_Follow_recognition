package org.masu1129.Twitch_Follow_recognition;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class Twitch_Test extends JavaPlugin {
    @Override
    public void onEnable() {
        final Twitch_Test plugin = this;
        saveDefaultConfig();
        getCommand("updateBorder").setExecutor(new UpdateBorderCommand(this));
        getCommand("addCreator").setExecutor(new AddCreatorCommand(this));
        getCommand("removeCreator").setExecutor(new RemoveCreatorCommand(this));
        getCommand("getCreators").setExecutor(new GetCreatorCommand(this));
        BukkitScheduler scheduler = getServer().getScheduler();
        final int update_delay = Integer.parseInt(plugin.getConfig().getString("twitch-update-delay-ticks"));
        scheduler.scheduleSyncRepeatingTask((Plugin) this, new Runnable() {
            @Override
            public void run() {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                Bukkit.dispatchCommand((CommandSender) console, "updateborder");
                for (World w : Bukkit.getServer().getWorlds()) {
                    WorldBorder overworldBorder = w.getWorldBorder();
                    int border_size = Integer.parseInt(plugin.getConfig().getString("current-border-size"));
                    overworldBorder.setSize(border_size, (update_delay / 20));
                }
            }
        },0L, update_delay);
    }

    @Override
    public void onDisable() {
        getLogger().info("Twitch_Follow_recognition plugin is Disable");
    }
}
