package top.violets007.guild.inventorys.repository;

import top.violets007.guild.inventorys.DoubleChestFakeInventory;
import top.violets007.guild.utils.GuildUtils;
import cn.nukkit.Player;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;

import java.util.Map;

/**
 * @author violets007
 * @version 1.0
 * @description: 公会大箱子
 * @date 2022/2/25 9:42 PM
 */
public class GuildBigChestInventory extends DoubleChestFakeInventory {


    public GuildBigChestInventory(InventoryHolder holder, String name, Map<Integer, Item> content) {
        super(holder, InventoryType.DOUBLE_CHEST.getDefaultTitle());
        this.setContents(content);
        this.setName(name);
    }

    @Override
    public void onClose(Player who) {
        super.onClose(who);
        GuildUtils.saveWarehouses(who, this.getType().getDefaultTitle(), this.getContents());
    }


}
