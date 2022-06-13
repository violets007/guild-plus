package cn.dawntribe.guild.inventorys.repository;

import cn.dawntribe.guild.inventorys.HopperFakeInventory;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.nukkit.Player;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;

import java.util.Map;

/**
 * @author violets007
 * @version 1.0
 * @description:
 * @date 2022/2/25 10:09 PM
 */
public class GuildHopperInventory extends HopperFakeInventory {


    public GuildHopperInventory(InventoryHolder holder, String name, Map<Integer, Item> content) {
        super(holder);
        this.setContents(content);
        setName(name);
    }

    @Override
    public void onClose(Player who) {
        super.onClose(who);
        GuildUtils.saveWarehouses(who, this.getType().getDefaultTitle(), this.getContents());
    }
}
