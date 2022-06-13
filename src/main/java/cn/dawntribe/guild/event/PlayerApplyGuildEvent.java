package cn.dawntribe.guild.event;

import cn.dawntribe.guild.pojo.Guild;
import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;

/**
 * @author violets007
 * @version 1.0
 * @description: 申请进入公会事件
 * @date 2022/4/1 4:43 PM
 */
public class PlayerApplyGuildEvent extends GuildEvent {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public PlayerApplyGuildEvent(Player player, Guild guild) {
        super(player, guild);
    }
}
