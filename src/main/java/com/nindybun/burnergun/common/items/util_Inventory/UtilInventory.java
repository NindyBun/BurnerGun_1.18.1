package com.nindybun.burnergun.common.items.util_Inventory;

import com.nindybun.burnergun.BurnerGun;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

public class UtilInventory extends Item {

    private UtilInvTabs[] tabs = new UtilInvTabs[3];
    private int currTab = 0;
    private final int CHEST_SIZE = 54;

    public UtilInventory() {
        super(new Properties().stacksTo(1).tab(BurnerGun.itemGroup));
        new UtilInvTabs(0, "Chest", tabs) {
            @Override
            public ItemStackHandler setHandler() {
                return new ItemStackHandler(CHEST_SIZE);
            }

            @Override
            public ItemStack setTabIcon() {
                return new ItemStack(Items.CHEST);
            }
        };
        new UtilInvTabs(1, "Chest", tabs) {
            @Override
            public ItemStackHandler setHandler() {
                return new ItemStackHandler(CHEST_SIZE);
            }

            @Override
            public ItemStack setTabIcon() {
                return new ItemStack(Items.CHEST);
            }
        };
        new UtilInvTabs(2, "Chest", tabs) {
            @Override
            public ItemStackHandler setHandler() {
                return new ItemStackHandler(CHEST_SIZE);
            }

            @Override
            public ItemStack setTabIcon() {
                return new ItemStack(Items.CHEST);
            }
        };
    }

    public int getCurrentTab(){
        return this.currTab;
    }

    public void setCurrentTab(int newTab){
        if (newTab < 0 && newTab >= this.tabs.length)
            throw new IndexOutOfBoundsException("Tab " + newTab + " is Out of Bounds!");
        this.currTab = newTab;
    }

    public UtilInvTabs getTab(int id){
        if (id >= 0 && id < tabs.length)
            return tabs[id];
        return null;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {

        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }



}

abstract class UtilInvTabs{
    private final int tabId;
    private ItemStack tabIcon;
    private ItemStackHandler tabHandler;
    private final Component displayName;
    private ResourceLocation guiTexture;

    public UtilInvTabs(String name, UtilInvTabs[] tabs){
        this(-1, name, tabs);
    }

    public UtilInvTabs(int id, String name, UtilInvTabs[] tabs){
        this.displayName = new TextComponent(name);
        this.tabIcon = ItemStack.EMPTY;
        this.tabHandler = null;
        this.tabId = addTab(id, this, tabs);
    }

    public abstract ItemStackHandler setHandler();

    public UtilInvTabs setGuiTexture(ResourceLocation texture){
        this.guiTexture = texture;
        return this;
    }

    public ItemStackHandler getTabHandler(){
        if (this.tabHandler == null)
            this.tabHandler = this.setHandler();
        return this.tabHandler;
    }

    public ResourceLocation getGuiTexture(){
        return this.guiTexture;
    }

    public abstract ItemStack setTabIcon();

    public ItemStack getTabIcon(){
        if (this.tabIcon.isEmpty())
            this.tabIcon = this.setTabIcon();
        return this.tabIcon;
    }

    public Component getDisplayName(){
        return this.displayName;
    }

    public int getTabId(){
        return this.tabId;
    }

    public int addTab(int id, UtilInvTabs newTab, UtilInvTabs[] tabs){
        if (id == -1)
            id = tabs.length;
        if (id >= tabs.length){
            UtilInvTabs[] tmp = new UtilInvTabs[id + 1];
            System.arraycopy(tabs, 0, tmp, 0, tabs.length);
            tabs = tmp;
        }
        tabs[id] = newTab;
        return id;
    }

}