package cn.dawntribe.guild.event;

import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.pojo.Product;
import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;

/**
 * @author violets007
 * @version 1.0
 * @description:
 * @date 2022/4/17 10:37 PM
 */
public class BuyGuildProductEvent extends GuildEvent {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private Product product;

    public BuyGuildProductEvent(Player player, Guild guild, Product product) {
        super(player, guild);
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
