package top.violets007.guild.event;


import top.violets007.guild.pojo.GuildWar;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

/**
 * @author violets007
 * @description:
 */
public class GuildWarOverEvent extends Event {
    public static HandlerList handlerList = new HandlerList();

    private GuildWar guildWar;

    public static HandlerList getHandlers() {
        return handlerList;
    }

    public GuildWarOverEvent(GuildWar guildWar) {
        this.guildWar = guildWar;
    }

    public GuildWar getGuildWar() {
        return guildWar;
    }

    public void setGuildWar(GuildWar guildWar) {
        this.guildWar = guildWar;
    }
}
