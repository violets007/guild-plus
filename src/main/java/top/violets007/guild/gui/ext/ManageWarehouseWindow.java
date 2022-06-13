package top.violets007.guild.gui.ext;

import top.violets007.guild.GuildPlugin;
import top.violets007.guild.gui.CustomWindow;
import top.violets007.guild.gui.WindowLoader;
import top.violets007.guild.gui.WindowManager;
import top.violets007.guild.gui.WindowType;
import top.violets007.guild.pojo.Guild;
import top.violets007.guild.pojo.Member;
import top.violets007.guild.pojo.Warehouse;
import top.violets007.guild.utils.GuildUtils;
import top.violets007.guild.utils.VariableTemplate;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseData;
import cn.nukkit.form.window.FormWindow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author violets007
 * @version 1.0
 * @description:
 * @date 2022/4/6 9:05 PM
 */
public class ManageWarehouseWindow extends CustomWindow implements WindowLoader {


    public ManageWarehouseWindow() {
        super(GuildUtils.getWindowConfigInfo("manage-warehouse-title"));
    }

    @Override
    public FormWindow init(Object... params) {
        Player player = (Player) params[0];
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        List<Warehouse> warehouseList = guild.getWarehouse();
        getElements().clear();
        getElements().add(new ElementDropdown(GuildUtils.getWindowConfigInfo("guild-warehouse-allow-type"), warehouseList.stream().map(warehouse -> GuildUtils.getWarehouseNameByType(warehouse.getType()) + "-" + warehouse.getType()).collect(Collectors.toList())));
        for (Member member : guild.getMembers()) {
            getElements().add(new ElementToggle(GuildPlugin.getInstance().getLang().translateString("guild-warehouse-allow-tips",
                    new String[]{VariableTemplate.PLAYER_NAME}, member.getName()),
                    true));
        }


        return this;
    }

    @Override
    public void onClicked(FormResponseCustom response, Player player) {
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        if (guild == null) return;
        FormResponseData dropdownResponse = response.getDropdownResponse(0);
        Warehouse warehouse = guild.getWarehouse().stream().filter(warehouse1 -> warehouse1.getType().equals(dropdownResponse.getElementContent().split("-")[1])).collect(Collectors.toList()).get(0);
        if (warehouse == null) return;
        List<String> allowOperationList = warehouse.getAllowOperation();
        allowOperationList.clear();
        HashMap<Integer, Object> responses = response.getResponses();

        for (Map.Entry<Integer, Object> entry : responses.entrySet()) {
            Integer index = entry.getKey();
            if (index == 0) continue;
            boolean defaultValue = (boolean) entry.getValue();
            Member member = guild.getMembers().get(index - 1);
            if (defaultValue) allowOperationList.add(member.getName());
        }

        warehouse.setAllowOperation(allowOperationList);
        guild.getWarehouse().set(guild.getWarehouse().indexOf(warehouse), warehouse);
        GuildUtils.saveGuildConfig(guild);

        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-warehouse-allow-msg"),
                WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)));
    }

    @Override
    public void onClosed(Player player) {
        super.onClosed(player);
    }
}
