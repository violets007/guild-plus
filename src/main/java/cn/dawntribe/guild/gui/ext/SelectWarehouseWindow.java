package cn.dawntribe.guild.gui.ext;

import cn.dawntribe.guild.GuildPlugin;
import cn.dawntribe.guild.gui.SimpleWindow;
import cn.dawntribe.guild.gui.WindowLoader;
import cn.dawntribe.guild.gui.WindowManager;
import cn.dawntribe.guild.gui.WindowType;
import cn.dawntribe.guild.inventorys.FakeInventory;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.pojo.Warehouse;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.dawntribe.guild.utils.VariableTemplate;
import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author violets007
 * @description:
 */
public class SelectWarehouseWindow extends SimpleWindow implements WindowLoader {

    public SelectWarehouseWindow() {
        super(GuildUtils.getWindowConfigInfo("select-warehouse-title"), "");
    }

    @Override
    public FormWindow init(Object... params) {
        Player player = (Player) params[0];
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        List<Warehouse> warehouseList = guild.getWarehouse();
        ArrayList<String> buttonNames = new ArrayList<>();
        for (Warehouse warehouse : warehouseList) {
            buttonNames.add(GuildUtils.getWindowConfigInfo("select-warehouse-button", new String[]{VariableTemplate.WAREHOUSE}, GuildUtils.getWarehouseNameByType(warehouse.getType())));
        }
        WindowManager.addButtonByNames(getButtons(), buttonNames.toArray(new String[0]));
        return this;
    }

    @Override
    public void onClicked(int id, Player player) {
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        if (guild == null) return;

        Warehouse warehouse = guild.getWarehouse().get(id);
        if (warehouse == null) return;

        String type = warehouse.getType();
        Map<Integer, Item> content = warehouse.getContent();
        FakeInventory inventory = GuildUtils.getWarehouse(type, player, content);

        List<String> allowOperation = warehouse.getAllowOperation();
        if (!allowOperation.contains(player.getName()) && !guild.getPresident().equals(player.getName())) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-warehouse-unauthorized"), WindowManager.getFormWindow(WindowType.MEMBER_WINDOW, player)));
            return;
        }

        player.addWindow(inventory);
    }

    @Override
    public void onClosed(Player player) {
        player.showFormWindow(WindowManager.getFormWindow(WindowType.MEMBER_WINDOW, player));
    }
}
