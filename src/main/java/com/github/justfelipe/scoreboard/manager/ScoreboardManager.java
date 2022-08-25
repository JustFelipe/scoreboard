package com.github.justfelipe.scoreboard.manager;

import com.github.justfelipe.scoreboard.ScoreboardPlugin;
import com.github.justfelipe.scoreboard.utils.EconomyHook;
import com.github.justfelipe.scoreboard.utils.NumberFormatter;
import com.gmail.nossr50.api.ExperienceAPI;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.nextplugins.cash.api.NextCashAPI;
import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.WeakHashMap;

@Getter
public class ScoreboardManager {

    @Getter private static final ScoreboardManager instance = new ScoreboardManager();

    private final Map<String, FastBoard> fastBoardMap = new WeakHashMap<>();

    public void enable() {

        try {
            Bukkit.getScheduler().runTaskTimerAsynchronously(ScoreboardPlugin.getInstance(), this::run, 30L, 60L);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void run() {

        for (FastBoard fastBoard : fastBoardMap.values()) {
            updateBoard(fastBoard);
        }
    }

    private void updateBoard(FastBoard fastBoard) {

        Player player = fastBoard.getPlayer();

        MPlayer mPlayer = MPlayer.get(player);

        Faction faction = mPlayer.getFaction();

        if (mPlayer.hasFaction()) {

            fastBoard.updateTitle(getZone(player));
            fastBoard.updateLines(
                    "",
                    "§f  Nível: §6" + ExperienceAPI.getPowerLevel(player),
                    "§f  Poder: §6" + mPlayer.getPowerRounded() + "/" + mPlayer.getPowerMaxRounded(),
                    "",
                    "§e [" + faction.getTag() + "] " + faction.getName(),
                    "§f   Online: §e" + faction.getOnlinePlayers().size() + "/" + faction.getMembersCount(),
                    "§f   Poder: §e" + faction.getPowerRounded() + "/" + faction.getPowerMaxRounded(),
                    "§f   Terras: §e" + faction.getLandCount(),
                    "",
                    "§f  Coins: §6" + NumberFormatter.toK(EconomyHook.getEconomy().getBalance(player)),
                    "§f  Cash: §6" + NumberFormatter.format(NextCashAPI.getInstance().findAccountByOwner(player.getName()).get().getBalance()),
                    "",
                    "§7loja.redeweaver.com"
            );
        } else {
            fastBoard.updateTitle(getZone(player));
            fastBoard.updateLines(
                    "",
                    "§f  Nível: §6" + ExperienceAPI.getPowerLevel(player),
                    "§f  Poder: §6" + mPlayer.getPowerRounded() + "/" + mPlayer.getPowerMaxRounded(),
                    "",
                    "§7  Sem facção",
                    "",
                    "§f  Coins: §6" + NumberFormatter.toK(EconomyHook.getEconomy().getBalance(player)),
                    "§f  Cash: §6" + NumberFormatter.format(NextCashAPI.getInstance().findAccountByOwner(player.getName()).get().getBalance()),
                    "",
                    "§7loja.redeweaver.com"
            );
        }
        fastBoard.updateTitle(getZone(player));
    }

    public String getZone(Player player) {

        if (player == null) {
            return "§2 Zona Livre";
        }

        Chunk chunk = player.getLocation().getChunk();

        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(chunk));

        String id = faction.getId();

        if (player.getWorld().getName().equals("mina")) {
            return "§7 Mundo de Mineração";
        }

        switch (id) {

            case "none": {
                return "§2 Zona Livre";
            }

            case "safezone": {
                return "§6 Zona Protegida";
            }

            case "warzone": {
                return "§4 Zona de Guerra";
            }

            default: {
                return faction.getColorTo(MPlayer.get(player)) + "[" + faction.getTag() + "] " + faction.getName();
            }
        }
    }
}