package top.violets007.guild.inventorys;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;

import cn.nukkit.event.HandlerList;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;

public class FakeSlotChangeEvent extends Event implements Cancellable {
    private final Player player;
    private final FakeInventory inventory;
    private final SlotChangeAction action;
    private boolean cancelled = false;

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    FakeSlotChangeEvent(Player player, FakeInventory inventory, SlotChangeAction action) {
        this.player = player;
        this.inventory = inventory;
        this.action = action;
    }

    @Override
    public void setCancelled() {
        this.cancelled = true;
    }

    public Player getPlayer() {
        return player;
    }

    public FakeInventory getInventory() {
        return inventory;
    }

    public SlotChangeAction getAction() {
        return action;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
