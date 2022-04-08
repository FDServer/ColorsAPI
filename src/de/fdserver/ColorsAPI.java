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

    public static boolean ACTIVE = true;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoinEarly(PlayerJoinEvent e) {
        if (ACTIVE) {
            Player p = e.getPlayer();
            CachedMetaData meta = LuckPermsProvider.get().getPlayerAdapter(Player.class).getMetaData(p);
            String displayName = meta.getPrefix().replace("&", "§") + p.getName() + meta.getSuffix().replace("&", "§");
            p.setDisplayName(displayName);
        }
//        p.setPlayerListName(displayName);
//        try {
//            Method getHandle = p.getClass().getMethod("getHandle");
//            Object entityPlayer = getHandle.invoke(p);
//            System.out.println(entityPlayer);
//            System.out.println(entityPlayer.getClass().getName());
//            Field cs = Class.forName("net.minecraft.world.entity.player.EntityHuman").getField("cs");
//            cs.setAccessible(true);
//            Object profile = cs.get(entityPlayer);
//            Field ff = profile.getClass().getDeclaredField("name");
//            ff.setAccessible(true);
//            ff.set(profile, displayName);
//            for (Player p2 : Bukkit.getOnlinePlayers()) {
//                p2.hidePlayer(this, p);
//                p2.showPlayer(this, p);
//            }
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | ClassNotFoundException exception) {
//            exception.printStackTrace();
//        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoinLate(PlayerJoinEvent e) {
        setScoreboard(e.getPlayer());
    }

    public static void setScoreboard(Player p) {
        if (ACTIVE) {
            for (Player p2 : Bukkit.getOnlinePlayers()) {
                createTeam(p, p2.getScoreboard());
                createTeam(p2, p.getScoreboard());
            }
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
