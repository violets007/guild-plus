package top.violets007.guild.event;

import top.violets007.guild.pojo.Guild;
import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

/**
 * @author violets007
 * @description: 公会事件
 * @date: 2022/2/28 1:21 AM
 */
public abstract class GuildEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private Guild guild;

    public static HandlerList getHandlers() {
        return handlers;
    }

    public GuildEvent(Player player, Guild guild) {
        this.player = player;
        this.guild = guild;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }
}
