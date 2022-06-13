package cn.dawntribe.guild.inventorys.repository;

import cn.dawntribe.guild.inventorys.ChestFakeInventory;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.nukkit.Player;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;

import java.util.Map;

/**
 * @author violets007
 * @version 1.0
 * @description: 公会仓库
 * @date 2022/2/25 9:37 PM
 */
public class GuildChestInventory extends ChestFakeInventory {


    public GuildChestInventory(InventoryHolder holder, String name, Map<Integer, Item> content) {
        super(holder, InventoryType.CHEST.getDefaultTitle());
        this.setContents(content);
        this.setName(name);
    }

    @Override
    public void onClose(Player who) {
        super.onClose(who);
        GuildUtils.saveWarehouses(who, this.getType().getDefaultTitle(), this.getContents());
    }

}
