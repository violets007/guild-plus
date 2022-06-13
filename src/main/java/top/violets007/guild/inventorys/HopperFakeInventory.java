package top.violets007.guild.inventorys;

import cn.nukkit.Player;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;

/**
 * @author violets007
 * @version 1.0
 * @description:
 * @date 2022/2/25 9:59 PM
 */
public class HopperFakeInventory extends FakeInventory {

    private String name;

    public HopperFakeInventory(InventoryHolder holder) {
        super(InventoryType.HOPPER, holder, InventoryType.HOPPER.getDefaultTitle());
    }

    @Override
    protected List<BlockVector3> onOpenBlock(Player who) {
        BlockVector3 blockPosition = new BlockVector3((int) who.x, ((int) who.y) - 3, (int) who.z);
        placeHopper(who, blockPosition);
        return Collections.singletonList(blockPosition);
    }

    protected UpdateBlockPacket getDefaultPack(int id, BlockVector3 pos) {
        UpdateBlockPacket updateBlock = new UpdateBlockPacket();
        updateBlock.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(id, 0);
        updateBlock.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
        updateBlock.x = pos.x;
        updateBlock.y = pos.y;
        updateBlock.z = pos.z;
        return updateBlock;
    }

    void placeHopper(Player who, BlockVector3 pos) {
        who.dataPacket(getDefaultPack(BlockID.HOPPER_BLOCK, pos));
        BlockEntityDataPacket blockEntityData = new BlockEntityDataPacket();
        blockEntityData.x = pos.x;
        blockEntityData.y = pos.y;
        blockEntityData.z = pos.z;
        blockEntityData.namedTag = getNbt(pos, getName());

        who.dataPacket(blockEntityData);
    }

    private static byte[] getNbt(BlockVector3 pos, String name) {
        CompoundTag tag = new CompoundTag()
                .putString("id", BlockEntity.HOPPER)
                .putInt("x", pos.x)
                .putInt("y", pos.y)
                .putInt("z", pos.z)
                .putString("CustomName", name == null ? "Hopper" : name);

        try {
            return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create NBT for chest");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
