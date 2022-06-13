package top.violets007.guild.event;

import top.violets007.guild.pojo.Guild;
import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;

/**
 * @author violets007
 * @description: 玩家退出公会
 * @date: 2022/5/2 9:44 PM
 */
public class PlayerQuitGuildEvent extends GuildEvent {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public PlayerQuitGuildEvent(Player player, Guild guild) {
        super(player, guild);
    }
}
