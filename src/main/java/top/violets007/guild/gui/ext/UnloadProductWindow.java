package top.violets007.guild.gui.ext;

import top.violets007.guild.GuildPlugin;
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

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


/**
 * @author violets007
 * @version 1.0
 * @description:
 * @date 2022/4/9 11:29 PM
 */
public class UnloadProductWindow extends SimpleWindow implements WindowLoader {
    private static final String SEPARATOR = "-";

    public UnloadProductWindow() {
        super(GuildUtils.getWindowConfigInfo("unload-product-title"), "");
    }

    @Override
    public FormWindow init(Object... params) {
        Player player = (Player) params[0];
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        List<Product> products = guild.getProducts().stream().filter(product -> product.getOwner().equals(player.getName())).collect(Collectors.toList());
        List<String> buttonNames = products.stream().map(product -> ItemIDSunName.getIDByName(product.getItem()) + SEPARATOR + product.getPrice()).collect(Collectors.toList());
        WindowManager.addButtonByNames(getButtons(), buttonNames.toArray(new String[0]));
        return this;
    }

    @Override
    public void onClicked(int id, Player player) {
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        String itemName = getResponse().getClickedButton().getText().split(SEPARATOR)[0];
        if (guild == null) return;
        Product product = GuildUtils.getProductByItemName(guild, itemName);

        if (product == null) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("unload-product-empty"), WindowManager.getFormWindow(WindowType.UNLOAD_PRODUCT_WINDOW, player)));
            return;
        }

        ConfirmWindow confirmWindow = (ConfirmWindow) WindowManager.getFormWindow(WindowType.CONFIRM_WINDOW,
                GuildUtils.getWindowConfigInfo("confirm-unload-product-tips", new String[]{VariableTemplate.ITEM_NAME}, ItemIDSunName.getIDByName(product.getItem())),
                GuildUtils.getWindowConfigInfo("confirm-unload-product"),
                GuildUtils.getWindowConfigInfo("confirm-unload-product-cancel"));

        confirmWindow.setButtonClickedListener(new BiConsumer<Boolean, Player>() {
            @Override
            public void accept(Boolean confirm, Player player) {
                if (confirm) {
                    if (product == null) {
                        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("unload-product-empty"), WindowManager.getFormWindow(WindowType.UNLOAD_PRODUCT_WINDOW, player)));
                        return;
                    }

                    if (!player.getInventory().canAddItem(product.getItem())) {
                        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("unload-product-inventory-full"), WindowManager.getFormWindow(WindowType.UNLOAD_PRODUCT_WINDOW, player)));
                        return;
                    }

                    player.getInventory().addItem(product.getItem());
                    guild.getProducts().remove(id);
                    GuildUtils.saveGuildConfig(guild);

                    player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("unload-product-success", new String[]{VariableTemplate.ITEM_NAME}, ItemIDSunName.getIDByName(product.getItem())), WindowManager.getFormWindow(WindowType.UNLOAD_PRODUCT_WINDOW, player)));
                } else {
                    player.showFormWindow(
                            WindowManager.getFormWindow(WindowType.UNLOAD_PRODUCT_WINDOW, player)
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
