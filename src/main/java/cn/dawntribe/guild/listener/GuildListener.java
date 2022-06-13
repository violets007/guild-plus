package cn.dawntribe.guild.listener;

import cn.dawntribe.guild.GuildPlugin;
import cn.dawntribe.guild.event.*;
import cn.dawntribe.guild.gui.WindowManager;
import cn.dawntribe.guild.gui.WindowType;
import cn.dawntribe.guild.inventorys.FakeInventory;
import cn.dawntribe.guild.inventorys.repository.GuildBigChestInventory;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.pojo.Member;
import cn.dawntribe.guild.pojo.Product;
import cn.dawntribe.guild.pojo.Warehouse;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.dawntribe.guild.utils.ItemIDSunName;
import cn.dawntribe.guild.utils.Language;
import cn.dawntribe.guild.utils.VariableTemplate;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.inventory.InventoryMoveItemEvent;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import me.onebone.economyapi.EconomyAPI;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author violets007
 * @version 1.0
 * @description: 公会事件监听器
 * @date 2022/2/25 8:41 PM
 */
public class GuildListener implements Listener {


    @EventHandler(priority = EventPriority.LOW)
    public void onInv(InventoryMoveItemEvent event) {
        InventoryHolder source = event.getSource();
        Player[] viewers = event.getViewers();

    }

    /*@EventHandler(priority = EventPriority.LOW)
    public void onInventoryTransactionEvent(InventoryTransactionEvent event) {
        Player player = event.getTransaction().getSource();
        Set<Inventory> inventories = event.getTransaction().getInventories();
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());

        if (guild == null) {
            inventories.stream().forEach(inventory -> {
                if (inventory instanceof FakeInventory) {
                    inventory.onClose(player);
                    event.setCancelled();
                    WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-warehouse-unauthorized"), WindowManager.getFormWindow(WindowType.GUILD_WINDOW, player));
                }
            });
            return;
        }

        HashMap<String, Object> map = guild.getWarehouse().get(0);
        ArrayList<String> allowOperation = (ArrayList<String>) map.get("allowOperation");
        if (!allowOperation.contains(player.getName()) && !guild.getPresident().equals(player.getName())) {
            inventories.stream().forEach(inventory -> {
                inventory.onClose(player);
                event.setCancelled();
                WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-warehouse-unauthorized"), WindowManager.getFormWindow(WindowType.MEMBER_WINDOW, player));
            });
        }

    }*/

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = event.getInventory();
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        if (!(inventory instanceof FakeInventory)) return;
        if (guild == null) {
            if (inventory instanceof FakeInventory) {
                inventory.close(player);
                Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
                    player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-warehouse-unauthorized"), WindowManager.getFormWindow(WindowType.GUILD_WINDOW, player)));
                }, 20);
            }
            return;
        }

        Warehouse warehouse = guild.getWarehouse().get(0);
        List<String> allowOperation = warehouse.getAllowOperation();
        if (!allowOperation.contains(player.getName()) && !guild.getPresident().equals(player.getName())) {
            inventory.onClose(player);
            Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-warehouse-unauthorized"), WindowManager.getFormWindow(WindowType.MEMBER_WINDOW, player)));
            }, 20);
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCreateGuildEvent(PlayerCreateGuildEvent event) {
        Player player = event.getPlayer();
        Guild guild = event.getGuild();
        Language lang = GuildPlugin.getInstance().getLang();
        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, lang.translateString("create-guild-success", new String[]{VariableTemplate.GUILD_NAME}, guild.getName()), null));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerApplyJoinGuildEvent(PlayerApplyGuildEvent event) {
        Player player = event.getPlayer();
        Guild guild = event.getGuild();

        if (guild.getTempApply().contains(player.getName())) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW,
                    GuildPlugin.getInstance().getLang().translateString("apply-guild-fail", new String[]{VariableTemplate.GUILD_NAME}, guild.getName()),
                    WindowManager.getFormWindow(WindowType.GUILD_LIST_WINDOW,
                            WindowManager.getFormWindow(WindowType.GUILD_WINDOW, player))));
            return;
        }

        guild.getTempApply().add(player.getName());
        GuildUtils.saveGuildConfig(guild);
        player.showFormWindow(WindowManager.getFormWindow(
                WindowType.MESSAGE_WINDOW,
                GuildPlugin.getInstance().getLang().translateString("apply-guild-success", new String[]{VariableTemplate.GUILD_NAME}, guild.getName()),
                WindowManager.getFormWindow(WindowType.GUILD_WINDOW, player)
        ));

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKickMember(KickMemberEvent event) {
        String kickPlayerName = event.getKickPlayerName();
        Guild guild = event.getGuild();
        Player president = event.getPlayer();

        GuildUtils.removeMember(kickPlayerName, guild);

        president.showFormWindow(WindowManager.getFormWindow(
                WindowType.MESSAGE_WINDOW,
                GuildPlugin.getInstance().getLang().translateString("kick-member-success", new String[]{VariableTemplate.PLAYER_NAME}, kickPlayerName),
                WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, president)
        ));

        GuildUtils.saveGuildConfig(guild);

        for (Player player : GuildPlugin.getInstance().getServer().getOnlinePlayers().values()) {
            if (player.getName().equals(kickPlayerName)) {
                player.sendMessage(GuildPlugin.getInstance().getLang().translateString("kick-member-tips", new String[]{VariableTemplate.GUILD_NAME}, guild.getName()));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoinGuildEvent(PlayerJoinGuildEvent event) {
        Player president = event.getPlayer();
        String joinPlayerName = event.getJoinPlayerName();
        Guild guild = event.getGuild();

        List<String> noticeList = guild.getMembers().stream().map(member -> member.getName()).collect(Collectors.toList());
        GuildUtils.addMember(guild, joinPlayerName);
        GuildUtils.removeAllTempApply(joinPlayerName);
        GuildUtils.saveGuildConfig(guild);

        for (Player onlinePlayer : GuildPlugin.getInstance().getServer().getOnlinePlayers().values()) {
            if (noticeList.contains(onlinePlayer.getName())) {
                onlinePlayer.sendTitle(GuildPlugin.getInstance().getLang().translateString("join-guild-tips", new String[]{VariableTemplate.PLAYER_NAME}, joinPlayerName), "");
            }
        }

        president.showFormWindow(WindowManager.getFormWindow(
                WindowType.MESSAGE_WINDOW,
                GuildPlugin.getInstance().getLang().translateString("accept-player-join-guild", new String[]{VariableTemplate.PLAYER_NAME}, joinPlayerName),
                WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, president)
        ));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuitGuild(PlayerQuitGuildEvent event) {
        Player player = event.getPlayer();
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        if (guild == null) return;
        GuildUtils.removeMember(player.getName(), guild);

        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("member-quit-guild-success", new String[]{VariableTemplate.GUILD_NAME}, guild.getName()), WindowManager.getFormWindow(WindowType.GUILD_WINDOW, player)));
    }


    @EventHandler
    public void onBuyGuildProductEvent(BuyGuildProductEvent event) {
        Player player = event.getPlayer();
        Guild guild = event.getGuild();
        Product product = event.getProduct();

        player.getInventory().addItem(product.getItem());
        guild.getProducts().remove(product);
        EconomyAPI.getInstance().addMoney(product.getOwner(), product.getPrice());
        GuildUtils.saveGuildConfig(guild);


        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("buy-product-success", new String[]{VariableTemplate.ITEM_NAME}, ItemIDSunName.getIDByName(product.getItem())), WindowManager.getFormWindow(WindowType.GUILD_PRODUCT_LIST_WINDOW, player)));
    }
}
