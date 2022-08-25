package com.github.justfelipe.scoreboard.utils;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyHook {

    @Getter private static Economy economy;

    public void hook(Plugin plugin) {

        RegisteredServiceProvider<Economy> registration = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (registration == null) return;

        economy = registration.getProvider();
    }
}