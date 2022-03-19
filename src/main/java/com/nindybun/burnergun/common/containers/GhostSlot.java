package com.nindybun.burnergun.common.containers;

import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Optional;

public class GhostSlot extends SlotItemHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private final RecipeType<? extends AbstractCookingRecipe> RECIPE_TYPE = RecipeType.SMELTING;
    private Level level;
    public GhostSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Level level) {
        super(itemHandler, index, xPosition, yPosition);
        this.level = level;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void set(ItemStack stack) {
        super.set(stack.copy());
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }


    @Override
    public void onTake(Player thePlayer, ItemStack stack) {
    }

    public boolean hasSmeltOption(ItemStack stack){
        SimpleContainer inv = new SimpleContainer(1);
        inv.setItem(0, stack);
        Optional<? extends AbstractCookingRecipe> recipe = level.getRecipeManager().getRecipeFor(RECIPE_TYPE, inv, level);
        return recipe.isPresent();
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.getItem() instanceof BurnerGunMK1 || stack.getItem() instanceof BurnerGunMK2 || stack.getItem() instanceof UpgradeCard)
            return false;
        if (!hasSmeltOption(stack))
            return false;
        return true;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }
}
