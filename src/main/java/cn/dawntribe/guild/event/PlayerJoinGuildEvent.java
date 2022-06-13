package cn.dawntribe.guild.event;

import cn.dawntribe.guild.pojo.Guild;
import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;

/**
 * @author violets007
 * @version 1.0
 * @description: 进入公会事件
 * @date 2022/4/1 4:43 PM
 */
public class PlayerJoinGuildEvent extends GuildEvent {

    private String joinPlayerName;

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public PlayerJoinGuildEvent(String joinPlayerName, Player president, Guild guild) {
        super(president, guild);
        this.joinPlayerName = joinPlayerName;
    }

    public String getJoinPlayerName() {
        return joinPlayerName;
    }
}
