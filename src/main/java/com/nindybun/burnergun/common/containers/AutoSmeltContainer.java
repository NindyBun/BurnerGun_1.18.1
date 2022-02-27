package com.nindybun.burnergun.common.containers;

import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Auto_Smelt.AutoSmelt;
import com.nindybun.burnergun.common.items.upgrades.Auto_Smelt.AutoSmeltHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutoSmeltContainer extends AbstractContainerMenu {

    AutoSmeltContainer(int windowId, Inventory playerInv,
                       FriendlyByteBuf buf){
        this(windowId, playerInv, new AutoSmeltHandler(MAX_EXPECTED_HANDLER_SLOT_COUNT));
    }

    public AutoSmeltContainer(int windowId, Inventory playerInventory, AutoSmeltHandler handler){
        super(ModContainers.AUTO_SMELT_CONTAINER.get(), windowId);
        this.handler = handler;
        this.setup(playerInventory);
    }

    private final AutoSmeltHandler handler;

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int HANDLER_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    public static final int MAX_EXPECTED_HANDLER_SLOT_COUNT = 27;

    private final int HANDLER_SLOTS_PER_ROW = 9;

    private final int HANDLER_INVENTORY_XPOS = 8;
    private static final int HANDLER_INVENTORY_YPOS = 8;

    private final int PLAYER_INVENTORY_XPOS = 8;
    private static final int PLAYER_INVENTORY_YPOS = 84;

    private final int SLOT_X_SPACING = 18;
    private final int SLOT_Y_SPACING = 18;
    private final int HOTBAR_XPOS = 8;
    private final int HOTBAR_YPOS = 142;

    private void setup(Inventory playerInv){
        // Add the players hotbar to the gui - the [xpos, ypos] location of each item
        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            int slotNumber = x;
            addSlot(new Slot(playerInv, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
        }

        // Add the rest of the player's inventory to the gui
        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
                int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
                addSlot(new Slot(playerInv, slotNumber, xpos, ypos));
            }
        }

        int bagSlotCount = handler.getSlots();
        if (bagSlotCount < 1 || bagSlotCount > MAX_EXPECTED_HANDLER_SLOT_COUNT) {
            LOGGER.warn("Unexpected invalid slot count in AutoSmeltHandler(" + bagSlotCount + ")");
            bagSlotCount = Math.max(1, Math.min(MAX_EXPECTED_HANDLER_SLOT_COUNT, bagSlotCount));
        }

        // Add the tile inventory container to the gui
        for (int bagSlot = 0; bagSlot < bagSlotCount; ++bagSlot) {
            int slotNumber = bagSlot;
            int bagRow = bagSlot / HANDLER_SLOTS_PER_ROW;
            int bagCol = bagSlot % HANDLER_SLOTS_PER_ROW;
            int xpos = HANDLER_INVENTORY_XPOS + SLOT_X_SPACING * bagCol;
            int ypos = HANDLER_INVENTORY_YPOS + SLOT_Y_SPACING * bagRow;
            addSlot(new SlotItemHandler(handler, slotNumber, xpos, ypos));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        ItemStack main = playerIn.getMainHandItem();
        ItemStack off = playerIn.getOffhandItem();
        return (!main.isEmpty() && main.getItem() instanceof AutoSmelt) ||
                (!off.isEmpty() && off.getItem() instanceof AutoSmelt) ||
                (!main.isEmpty() && main.getItem() instanceof BurnerGunMK1) ||
                (!off.isEmpty() && off.getItem() instanceof BurnerGunMK1) ||
                (!main.isEmpty() && main.getItem() instanceof BurnerGunMK2) ||
                (!off.isEmpty() && off.getItem() instanceof BurnerGunMK2);
    }


    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        super.quickMoveStack(playerIn, index);
        return ItemStack.EMPTY;
    }

    private static final Logger LOGGER = LogManager.getLogger();
}
