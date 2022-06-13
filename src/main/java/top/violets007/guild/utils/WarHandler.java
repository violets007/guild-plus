package top.violets007.guild.utils;

import top.violets007.guild.GuildPlugin;
import top.violets007.guild.pojo.Guild;
import top.violets007.guild.pojo.GuildWar;
import top.violets007.guild.pojo.Member;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.utils.DummyBossBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author violets007
 * @description: 一些公会战处理
 */
public class WarHandler {
    public static ArrayList<GuildWar> guildWarCache = new ArrayList<>();
    public static HashMap<String, Long> sendWarWaitTime = new HashMap<>();

    public static void tpGamePos(String playerName, String msg) {
        Guild guild = GuildUtils.getGuildByPlayerName(playerName);
        List<Member> members = guild.getMembers().stream().filter(member -> member.getName().equals(playerName)).collect(Collectors.toList());
        ArrayList<GuildWar> guildWarCache = WarHandler.guildWarCache;
        GuildWar guildWar = guildWarCache.get(0);
        if (guildWar.getInitiationGuild().getName().equals(guild.getName())) {
            tpGamePos(members, "sparring-points", msg);
        } else {
            tpGamePos(members, "sparring-points1", msg);
        }
    }

    public static void tpGamePos(List<Member> members, String posStr, String msg) {
        for (Member member : members) {
            Player player = Server.getInstance().getPlayer(member.getName());
            if (player != null) {
                Position pos = GuildUtils.getPosByStr(GuildPlugin.getInstance().getWarConfig().getString(posStr));
                player.teleport(pos);
                player.sendTitle(msg);
            }
        }
    }

    public static void setBossBar(List<Member> members, String text, GuildWar guildWar) {
        for (Member member : members) {
            Player player = Server.getInstance().getPlayer(member.getName());
            if (player == null) return;

            if (!guildWar.getBossBarIds().containsKey(player.getName())) {
                DummyBossBar dummyBossBar = new DummyBossBar.Builder(player).text(text).build();
                player.createBossBar(dummyBossBar);

                guildWar.getBossBarIds().put(player.getName(), dummyBossBar.getBossBarId());
            }

            Long bossBarId = guildWar.getBossBarIds().get(player.getName());
            DummyBossBar dummyBossBar = player.getDummyBossBar(bossBarId);
            dummyBossBar.setText(text);
        }
    }

    public static void sendStartGameTips(GuildWar guildWar, Guild initiationGuild) {
        Language lang = GuildPlugin.getInstance().getLang();
        for (Member member : initiationGuild.getMembers()) {
            Player player = Server.getInstance().getPlayer(member.getName());
            if (player != null) {
                player.sendTip(lang.translateString("guild-war-start-time", new String[]{VariableTemplate.START_TIME}, guildWar.getJoinGameTime()));
            }
        }
    }

    public static void tpPosByGuild(Player player, Guild guild, String msg) {
        List<Member> members = guild.getMembers().stream().filter(member -> member.getName().equals(player.getName())).collect(Collectors.toList());
        if (members.size() > 0) {
            WarHandler.tpGamePos(members, "sparring-points", msg);
        }
    }

    public static void addItems(Guild initiationGuild, ArrayList<Item> items) {
        for (Member member : initiationGuild.getMembers()) {
            Player player = Server.getInstance().getPlayer(member.getName());
            if (player != null) {
                player.getInventory().addItem(items.toArray(new Item[0]));
            }
        }
    }
}
