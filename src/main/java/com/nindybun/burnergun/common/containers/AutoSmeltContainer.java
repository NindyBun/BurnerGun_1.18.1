package com.nindybun.burnergun.common.containers;

import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Auto_Smelt.AutoSmelt;
import com.nindybun.burnergun.common.items.upgrades.Auto_Smelt.AutoSmeltHandler;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutoSmeltContainer extends AbstractContainerMenu {

    AutoSmeltContainer(int windowId, Inventory playerInv,
                       FriendlyByteBuf buf){
        this(windowId, playerInv, new ItemStackHandler(MAX_EXPECTED_HANDLER_SLOT_COUNT));
    }

    public AutoSmeltContainer(int windowId, Inventory playerInventory, IItemHandler handler){
        super(ModContainers.AUTO_SMELT_CONTAINER.get(), windowId);
        this.setup(new InvWrapper(playerInventory), handler, playerInventory.player.level);
    }


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

    private void setup(InvWrapper playerInv, IItemHandler handler, Level level){
        // Add the players hotbar to the gui - the [xpos, ypos] location of each item
        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            int slotNumber = x;
            addSlot(new SlotItemHandler(playerInv, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
        }

        // Add the rest of the player's inventory to the gui
        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
                int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
                addSlot(new SlotItemHandler(playerInv, slotNumber, xpos, ypos));
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
            addSlot(new GhostSlot(handler, slotNumber, xpos, ypos, level));
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
    public ItemStack quickMoveStack(Player playrIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack currentStack = slot.getItem();

            // Stop our items at the very least :P
            if (currentStack.getItem() instanceof BurnerGunMK1 || currentStack.getItem() instanceof UpgradeCard || currentStack.getItem() instanceof BurnerGunMK2)
                return itemstack;

            if (currentStack.isEmpty())
                return itemstack;

            // Find the first empty slot number
            int slotNumber = -1;
            for (int i = 36; i <= 63; i++) {
                if (this.slots.get(i).getItem().isEmpty()) {
                    slotNumber = i;
                    break;
                } else {
                    if (this.slots.get(i).getItem().getItem() == currentStack.getItem()) {
                        break;
                    }
                }
            }

            if (slotNumber == -1)
                return itemstack;

            this.slots.get(slotNumber).set(currentStack.copy().split(1));
        }

        return itemstack;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if ((slotId < this.slots.size()
                && slotId >= 0
                && (this.slots.get(slotId).getItem().getItem() instanceof BurnerGunMK1
                || this.slots.get(slotId).getItem().getItem() instanceof BurnerGunMK2
                || this.slots.get(slotId).getItem().getItem() instanceof UpgradeCard))
                || clickTypeIn == ClickType.SWAP) {
            return ;
        }
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    private static final Logger LOGGER = LogManager.getLogger();
}
