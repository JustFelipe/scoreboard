package com.github.justfelipe.scoreboard;

import com.github.justfelipe.scoreboard.listeners.PlayerConnectionListeners;
import com.github.justfelipe.scoreboard.manager.ScoreboardManager;
import com.github.justfelipe.scoreboard.utils.EconomyHook;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ScoreboardPlugin extends JavaPlugin {

    @Getter private static ScoreboardPlugin instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {

        try {

            new EconomyHook().hook(this);

            ScoreboardManager.getInstance().enable();

            Bukkit.getPluginManager().registerEvents(new PlayerConnectionListeners(), this);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            getLogger().severe("Ocorreu um erro na inicialização do plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
