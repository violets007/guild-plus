package cn.dawntribe.guild.inventorys.repository;

import cn.dawntribe.guild.inventorys.DoubleChestFakeInventory;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.pojo.Warehouse;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.nukkit.Player;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
