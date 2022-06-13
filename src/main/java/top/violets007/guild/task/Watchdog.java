package top.violets007.guild.task;

import top.violets007.guild.GuildPlugin;
import top.violets007.guild.event.GuildWarOverEvent;
import top.violets007.guild.pojo.Guild;
import top.violets007.guild.pojo.GuildWar;
import top.violets007.guild.pojo.WarStatus;
import top.violets007.guild.utils.Language;
import top.violets007.guild.utils.VariableTemplate;
import top.violets007.guild.utils.WarHandler;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * @author violets007
 * @description:
 */
public class Watchdog extends PluginTask<GuildPlugin> {
    /**
     * 构造一个插件拥有的任务的方法。<br>Constructs a plugin-owned task.
     *
     * @param owner 这个任务的所有者插件。<br>The plugin object that owns this task.
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    public Watchdog(GuildPlugin owner) {
        super(owner);
    }

    @Override
    public void onRun(int currentTick) {
        Iterator<GuildWar> iterator = WarHandler.guildWarCache.iterator();
        while (iterator.hasNext()) {
            GuildWar guildWar = iterator.next();
            Guild initiationGuild = guildWar.getInitiationGuild();
            Guild receivingGuild = guildWar.getReceivingGuild();
            Language lang = GuildPlugin.getInstance().getLang();

            if (guildWar.getJoinGameTime() >= 1 && guildWar.getWarStatus() == WarStatus.WAIT) {

                WarHandler.sendStartGameTips(guildWar, initiationGuild);
                WarHandler.sendStartGameTips(guildWar, receivingGuild);
                guildWar.setJoinGameTime(guildWar.getJoinGameTime() - 1);
            }

            if (guildWar.getJoinGameTime() == 0 && guildWar.getWarStatus() == WarStatus.WAIT) {
                guildWar.setStartTime(new Date());
                guildWar.setWarStatus(WarStatus.GAME);
                WarHandler.tpGamePos(guildWar.getInitiationGuild().getMembers(), "sparring-points", lang.translateString("guild-war-start"));
                WarHandler.tpGamePos(guildWar.getReceivingGuild().getMembers(), "sparring-points1", lang.translateString("guild-war-start"));
            }

            if (guildWar.getWarStatus() == WarStatus.GAME) {
                if (guildWar.getBattleAgainstTime() > 0) {

                    WarHandler.setBossBar(initiationGuild.getMembers(), lang.translateString("bossBar-content", new String[]{VariableTemplate.WAR_TIME, VariableTemplate.KILLER_CREDITS, VariableTemplate.DEATH_CREDITS}, guildWar.getBattleAgainstTime(), guildWar.getDeathCredits(), guildWar.getKillerCredits()), guildWar);
                    WarHandler.setBossBar(receivingGuild.getMembers(), lang.translateString("bossBar-content", new String[]{VariableTemplate.WAR_TIME, VariableTemplate.KILLER_CREDITS, VariableTemplate.DEATH_CREDITS}, guildWar.getBattleAgainstTime(), guildWar.getKillerCredits(), guildWar.getDeathCredits()), guildWar);

                    guildWar.setBattleAgainstTime(guildWar.getBattleAgainstTime() - 1);
                } else {
                    guildWar.setWarStatus(WarStatus.VICTORY);
                    iterator.remove();
                }
            }

            if (guildWar.getWarStatus() == WarStatus.VICTORY) {

                GuildPlugin.getInstance().getServer().getPluginManager().callEvent(new GuildWarOverEvent(guildWar));
                WarHandler.tpGamePos(guildWar.getInitiationGuild().getMembers(), "gameover-points", lang.translateString("guild-war-end"));
                WarHandler.tpGamePos(guildWar.getReceivingGuild().getMembers(), "gameover-points", lang.translateString("guild-war-end"));

                for (Map.Entry<String, Long> entry : guildWar.getBossBarIds().entrySet()) {
                    Player player = Server.getInstance().getPlayer(entry.getKey());
                    if (player != null) player.removeBossBar(entry.getValue());
                }
                guildWar.setWarStatus(null);
            }
        }

    }


}
