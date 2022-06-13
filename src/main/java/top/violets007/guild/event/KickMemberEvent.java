package top.violets007.guild.event;

import top.violets007.guild.pojo.Guild;
import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;

/**
 * @author violets007
 * @version 1.0
 * @description: 退出公会
 * @date 2022/4/1 4:43 PM
 */
public class KickMemberEvent extends GuildEvent {

    private String kickPlayerName;


    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public KickMemberEvent(String kickPlayerName, Player president, Guild guild) {
        super(president, guild);
        this.kickPlayerName = kickPlayerName;
    }

    public String getKickPlayerName() {
        return kickPlayerName;
    }

}
