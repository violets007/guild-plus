package top.violets007.guild.event;

import top.violets007.guild.pojo.Guild;
import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;

/**
 * @author violets007
 * @description: 创建公会事件
 * @date: 2022/2/28 1:22 AM
 */
public class PlayerCreateGuildEvent extends GuildEvent {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public PlayerCreateGuildEvent(Player player, Guild guild) {
        super(player, guild);
    }

}
