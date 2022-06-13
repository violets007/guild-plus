package top.violets007.guild.gui.ext;

import top.violets007.guild.GuildPlugin;
import top.violets007.guild.event.BuyGuildProductEvent;
import top.violets007.guild.gui.SimpleWindow;
import top.violets007.guild.gui.WindowLoader;
import top.violets007.guild.gui.WindowManager;
import top.violets007.guild.gui.WindowType;
import top.violets007.guild.gui.temp.ConfirmWindow;
import top.violets007.guild.pojo.Guild;
import top.violets007.guild.pojo.Product;
import top.violets007.guild.utils.GuildUtils;
import top.violets007.guild.utils.ItemIDSunName;
import top.violets007.guild.utils.VariableTemplate;
import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;
import me.onebone.economyapi.EconomyAPI;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author violets007
 * @version 1.0
 * @description:
 * @date 2022/4/9 9:56 PM
 */
public class GuildProductListWindow extends SimpleWindow implements WindowLoader {

    public GuildProductListWindow() {
        super(GuildUtils.getWindowConfigInfo("guild-shop-list-title"), "");
    }

    @Override
    public FormWindow init(Object... params) {
        Player player = (Player) params[0];
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        List<String> buttonNames = guild.getProducts().stream().map(product -> {
            return GuildUtils.getWindowConfigInfo("guild-product-format", new String[]{
                    VariableTemplate.ITEM_NAME,
                    VariableTemplate.COUNT,
                    VariableTemplate.PLAYER_NAME,
                    VariableTemplate.PRICE
            }, ItemIDSunName.getIDByName(product.getItem()), product.getItem().getCount(), product.getOwner(), product.getPrice());
        }).collect(Collectors.toList());
        WindowManager.addButtonByNames(getButtons(), buttonNames.toArray(new String[0]));
        return this;
    }

    @Override
    public void onClicked(int id, Player player) {
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());

        if (guild == null) return;
        Product product = guild.getProducts().get(id);

        ConfirmWindow confirmWindow = (ConfirmWindow) WindowManager.getFormWindow(WindowType.CONFIRM_WINDOW,
                GuildUtils.getWindowConfigInfo("confirm-buy-product-tips", new String[]{VariableTemplate.ITEM_NAME}, ItemIDSunName.getIDByName(product.getItem())),
                GuildUtils.getWindowConfigInfo("confirm-buy-product"),
                GuildUtils.getWindowConfigInfo("confirm-buy-product-cancel"));

        confirmWindow.setButtonClickedListener(new BiConsumer<Boolean, Player>() {
            @Override
            public void accept(Boolean confirm, Player player) {
                if (confirm) {
                    if (product == null) {
                        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("buy-product-empty"), WindowManager.getFormWindow(WindowType.GUILD_PRODUCT_LIST_WINDOW, player)));
                        return;
                    }

                    if (!player.getInventory().canAddItem(product.getItem())) {
                        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("buy-product-inventory-full"), WindowManager.getFormWindow(WindowType.GUILD_PRODUCT_LIST_WINDOW, player)));
                        return;
                    }

                    if (product.getOwner().equals(player.getName())) {
                        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("buy-product-owner"), WindowManager.getFormWindow(WindowType.GUILD_PRODUCT_LIST_WINDOW, player)));
                        return;
                    }

                    double myMoney = EconomyAPI.getInstance().myMoney(player);
                    double price = product.getPrice();
                    if (EconomyAPI.getInstance().reduceMoney(player, price) != EconomyAPI.RET_SUCCESS) {
                        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("buy-product-insufficient-economy", new String[]{VariableTemplate.MONEY, VariableTemplate.PRICE}, myMoney, price), WindowManager.getFormWindow(WindowType.GUILD_PRODUCT_LIST_WINDOW, player)));
                        return;
                    }

                    GuildPlugin.getInstance().getServer().getPluginManager().callEvent(new BuyGuildProductEvent(player, guild, product));

                } else {
                    player.showFormWindow(
                            WindowManager.getFormWindow(WindowType.GUILD_PRODUCT_LIST_WINDOW, player)
                    );
                }
            }
        });

        player.showFormWindow(confirmWindow);
    }

    @Override
    public void onClosed(Player player) {
        player.showFormWindow(WindowManager.getFormWindow(WindowType.MANAGE_PRODUCT_WINDOW));
    }
}
