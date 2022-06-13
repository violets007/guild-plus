package top.violets007.guild.gui.ext;

import top.violets007.guild.GuildPlugin;

import top.violets007.guild.gui.CustomWindow;
import top.violets007.guild.gui.WindowLoader;
import top.violets007.guild.gui.WindowManager;
import top.violets007.guild.gui.WindowType;
import top.violets007.guild.pojo.Guild;
import top.violets007.guild.pojo.Product;
import top.violets007.guild.utils.GuildUtils;
import top.violets007.guild.utils.ItemIDSunName;
import top.violets007.guild.utils.Utils;
import top.violets007.guild.utils.VariableTemplate;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseData;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.item.Item;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author violets007
 * @version 1.0
 * @description:
 * @date 2022/4/9 11:19 PM
 */
public class UploadProductWindow extends CustomWindow implements WindowLoader {

    private static final String SEPARATOR = "-";

    public UploadProductWindow() {
        super(GuildUtils.getWindowConfigInfo("upload-product-title"));
    }

    @Override
    public FormWindow init(Object... params) {
        Player player = (Player) params[0];
        Map<Integer, Item> contents = player.getInventory().getContents();
        getElements().clear();
        getElements().add(new ElementDropdown(GuildPlugin.getInstance().getLang().translateString("select-upload-product-item"), contents.entrySet().stream().map(entry -> {
            return entry.getKey() + SEPARATOR + ItemIDSunName.getIDByName(entry.getValue());
        }).collect(Collectors.toList())));
        getElements().add(new ElementInput("", GuildPlugin.getInstance().getLang().translateString("input-product-price")));
        getElements().add(new ElementSlider(GuildUtils.getWindowConfigInfo("guild-upload-number"), 1, 64, 1, 1));
        return this;
    }

    @Override
    public void onClicked(FormResponseCustom response, Player player) {
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        if (guild == null) return;

        FormResponseData dropdownResponse = response.getDropdownResponse(0);
        float count = response.getSliderResponse(2);
        String priceStr = response.getInputResponse(1);
        String elementContent = dropdownResponse.getElementContent();
        String itemName = elementContent.split("-")[1];
        Item item = player.getInventory().getContents().get(Integer.parseInt(elementContent.split("-")[0])).clone();

        List<Product> existProduct = guild.getProducts().stream().filter(product -> product.getItem().getName().equals(itemName)).collect(Collectors.toList());

        if (dropdownResponse.getElementContent() == null || dropdownResponse.getElementContent().isEmpty()) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("upload-product-empty"), WindowManager.getFormWindow(WindowType.MANAGE_PRODUCT_WINDOW)));
            return;
        }

        if (existProduct.size() > 0) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("upload-product-exist"), WindowManager.getFormWindow(WindowType.UPLOAD_PRODUCT_WINDOW, player)));
            return;
        }

        if (item.getCount() < count) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("upload-product-item-count"), WindowManager.getFormWindow(WindowType.UPLOAD_PRODUCT_WINDOW, player)));
            return;
        }

        item.setCount(Float.valueOf(count).intValue());

        if (!Utils.isNumber(priceStr)) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("input-product-price-error"), WindowManager.getFormWindow(WindowType.UPLOAD_PRODUCT_WINDOW, player)));
            return;
        }

        GuildUtils.uploadProduct(guild, item, player, Integer.parseInt(priceStr), new Date());
        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("input-product-price-success", new String[]{VariableTemplate.ITEM_NAME}, itemName), WindowManager.getFormWindow(WindowType.MANAGE_PRODUCT_WINDOW)));
    }

    @Override
    public void onClosed(Player player) {
        player.showFormWindow(WindowManager.getFormWindow(WindowType.MANAGE_PRODUCT_WINDOW));
    }
}
