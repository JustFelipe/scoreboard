package com.github.justfelipe.scoreboard.listeners;

import com.github.justfelipe.scoreboard.manager.ScoreboardManager;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListeners implements Listener {

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        FastBoard fastBoard = new FastBoard(player);

        fastBoard.updateTitle(ScoreboardManager.getInstance().getZone(player));

        ScoreboardManager.getInstance().getFastBoardMap().put(player.getName(), fastBoard);
    }

    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        FastBoard fastBoard = ScoreboardManager.getInstance().getFastBoardMap().remove(player.getName());

        if (fastBoard != null) {
            fastBoard.delete();
        }
    }
}