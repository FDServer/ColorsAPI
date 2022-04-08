package de.fdserver;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ColorsAPI extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoinEarly(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        CachedMetaData meta = LuckPermsProvider.get().getPlayerAdapter(Player.class).getMetaData(p);
        String displayName = meta.getPrefix().replace("&", "§") + p.getName() + meta.getSuffix().replace("&", "§");
        p.setDisplayName(displayName);
    }

    public static void setScoreboard(Player p) {
        for (Player p2 : Bukkit.getOnlinePlayers()) {
            createTeam(p, p2.getScoreboard());
            createTeam(p2, p.getScoreboard());
        }
    }

    private static void createTeam(Player p, Scoreboard sb) {
        Team t;
        if ((t = sb.getTeam(p.getName())) == null)
            t = sb.registerNewTeam(p.getName());
        t.setColor(ChatColor.getByChar(LuckPermsProvider.get().getPlayerAdapter(Player.class).getMetaData(p).getPrefix().replace("&", "")));
        t.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        t.addEntry(p.getName());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage("§7[§2+§7] " + e.getPlayer().getDisplayName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage("§7[§4-§7] " + e.getPlayer().getDisplayName());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setFormat("%s §7|§r %s");
    }

}
