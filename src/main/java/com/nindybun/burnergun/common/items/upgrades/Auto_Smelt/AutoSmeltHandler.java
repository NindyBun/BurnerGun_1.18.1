package com.nindybun.burnergun.common.items.upgrades.Auto_Smelt;

import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Optional;

public class AutoSmeltHandler extends ItemStackHandler {
    public static final Logger LOGGER = LogManager.getLogger();
    private static final RecipeType<? extends AbstractCookingRecipe> RECIPE_TYPE = RecipeType.SMELTING;

    public AutoSmeltHandler(int numberOfSlots){
        super(numberOfSlots);
    }

    @Override
    public CompoundTag serializeNBT() {
        super.serializeNBT();
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        this.setStackInSlot(slot, Items.AIR.getDefaultInstance());
        return ItemStack.EMPTY;
    }

    public boolean hasSmeltOption(ItemStack stack){
        SimpleContainer inv = new SimpleContainer(1);
        inv.setItem(0, stack);
        Level world = Minecraft.getInstance().level;
        Optional<? extends AbstractCookingRecipe> recipe = world.getRecipeManager().getRecipeFor(RECIPE_TYPE, inv, world);
        if (!recipe.isPresent())
            return false;
        return true;
    }


    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (stack.getItem() instanceof BurnerGunMK1 || stack.getItem() instanceof BurnerGunMK2 || stack.getItem() instanceof UpgradeCard)
            return false;
        if (hasSmeltOption(stack))
            return false;
        if (this.getStackInSlot(slot).getItem() == Items.AIR){
            this.setStackInSlot(slot, stack.getItem().getDefaultInstance());
        }else if (stack.getItem() == Items.AIR){
            this.setStackInSlot(slot, Items.AIR.getDefaultInstance());
        }else if (stack.getItem() != Items.AIR){
            this.setStackInSlot(slot, Items.AIR.getDefaultInstance());
            this.setStackInSlot(slot, stack.getItem().getDefaultInstance());
        }
        return false;
    }

    protected void onContentsChanged(int slot) {
        this.validateSlotIndex(slot);
    }


}
