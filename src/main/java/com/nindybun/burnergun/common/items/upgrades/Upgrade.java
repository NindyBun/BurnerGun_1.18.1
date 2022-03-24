package com.nindybun.burnergun.common.items.upgrades;

import net.minecraft.world.item.ItemStack;

public enum Upgrade {
    TIER_1("base_tier_1", 1, 0, 5, 0, false, "type.base"),
    TIER_2("base_tier_2", 2, 0, 5, 0, false, "type.base"),
    TIER_3("base_tier_3", 3, 0, 5, 0, false, "type.base"),
    TIER_4("base_tier_4", 4, 0, 5, 0, false, "type.base"),
    TIER_5("base_tier_5", 5, 0, 5, 0, false, "type.base"),
    FORTUNE_1("fortune_1", 1, 100, 1, 0, true, "type.util"),
    FORTUNE_2("fortune_2", 2, 125, 1, 0, true, "type.util"),
    FORTUNE_3("fortune_3", 3, 175, 1, 0, true, "type.util"),
    HORIZONTAL_EXPANSION_1("horizontal_expansion_1", 1, 0, 1, 0, false, "type.tool"),
    HORIZONTAL_EXPANSION_2("horizontal_expansion_2", 2, 0, 1, 0, false, "type.tool"),
    HORIZONTAL_EXPANSION_3("horizontal_expansion_3", 3, 0, 1, 0, false, "type.tool"),
    HORIZONTAL_EXPANSION_4("horizontal_expansion_4", 4, 0, 1, 0, false, "type.tool"),
    VERTICAL_EXPANSION_1("vertical_expansion_1", 1, 0, 1, 0, false, "type.tool"),
    VERTICAL_EXPANSION_2("vertical_expansion_2", 2, 0, 1, 0, false, "type.tool"),
    VERTICAL_EXPANSION_3("vertical_expansion_3", 3, 0, 1, 0, false, "type.tool"),
    VERTICAL_EXPANSION_4("vertical_expansion_4", 4, 0, 1, 0, false, "type.tool"),
    FOCAL_POINT_1("focal_point_1", 1, 75, 1, 12, false, "type.burnergun"),
    FOCAL_POINT_2("focal_point_2", 2, 100, 1, 24, false, "type.burnergun"),
    FOCAL_POINT_3("focal_point_3", 3, 125, 1, 32, false, "type.burnergun"),
    FUEL_EFFICIENCY_1("fuel_efficiency_1", 1, 0, 1, 0.15, true, "type.util"),
    FUEL_EFFICIENCY_2("fuel_efficiency_2", 2, 0, 1, 0.35, true, "type.util"),
    FUEL_EFFICIENCY_3("fuel_efficiency_3", 3, 0, 1, 0.50, true, "type.util"),
    FUEL_EFFICIENCY_4("fuel_efficiency_4", 4, 0, 1, 0.75, true, "type.util"),
    FUEL_EFFICIENCY_5("fuel_efficiency_5", 5, 0, 1, 0.90, true, "type.util"),
    AMBIENCE_1("ambience_1", 1, 0, 1, 1, false, "type.util"),
    AMBIENCE_2("ambience_2", 1, 0, 1, 2, false, "type.util"),
    AMBIENCE_3("ambience_3", 1, 0, 1, 6, false, "type.util"),
    AMBIENCE_4("ambience_4", 1, 0, 1, 24, false, "type.util"),
    AMBIENCE_5("ambience_5", 1, 0, 1, 120, false, "type.util"),

    VEIN_MINER_1("vein_miner_1", 1, 0, 1, 64*1, true, "type.tool"),
    VEIN_MINER_2("vein_miner_2", 2, 0, 1, 64*2, true, "type.tool"),
    VEIN_MINER_3("vein_miner_3", 3, 0, 1, 64*3, true, "type.tool"),
    VEIN_MINER_4("vein_miner_4", 4, 0, 1, 64*4, true, "type.tool"),
    VEIN_MINER_5("vein_miner_5", 5, 0, 1, 64*5, true, "type.tool"),
    MAGNET("magnet", -1, 75, 1, 0, true, "type.util"),
    SILK_TOUCH("silk_touch", -1, 150, 1, 0, true, "type.tool"),
    LIGHT("light", -1, 50, 1, 0, true, "type.burnergun"),
    AUTO_SMELT("auto_smelt", -1, 50 + (int)(Math.random() * ((175 - 50) + 1)), 1, 0, true, "type.util"),
    TRASH("trash", -1, 75, 1, 0, true, "type.util")
    ;

    private final String name;
    private final int tier;
    private final int cost;
    private final int stackSize;
    private final double extraValue;
    private final String baseName;
    private final String toolTip, type;
    private final UpgradeCard card;
    private final ItemStack upgradeStack;
    private final boolean isToggleable;
    private boolean active = true;

    Upgrade(String name, int tier, int cost, int stackSize, double extraValue, boolean isToggleable, String type){
        this.name = name;
        this.tier = tier;
        this.isToggleable = isToggleable;
        this.cost = cost;
        this.extraValue = extraValue;
        this.stackSize = stackSize;
        this.card = new UpgradeCard(this);
        this.upgradeStack = new ItemStack(this.card);
        this.baseName = tier == -1 ? name : name.substring(0, name.lastIndexOf('_'));
        this.toolTip = "tooltip.burnergun." + this.baseName;
        this.type = "tooltip.burnergun." + type;
    }


    public int getCost() {
        return this.cost;
    }
    public int getTier() {
        return this.tier;
    }
    public int getStackSize(){ return this.stackSize; }
    public boolean hasTier(){
        return tier != -1;
    }
    public String getBaseName() {
        return this.baseName;
    }
    public String getName() {
        return this.name;
    }
    public String getToolTip() {
        return this.toolTip;
    }
    public String getType() { return this.type; }
    public UpgradeCard getCard() {
        return this.card;
    }
    public double getExtraValue() { return this.extraValue; }
    public boolean isToggleable(){ return this.isToggleable; }
    public void setActive(boolean value) { this.active = value; }
    public boolean isActive() { return this.active; }
    public ItemStack getUpgradeStack() {
        return this.upgradeStack;
    }
    public boolean lazyIs(Upgrade upgrade) {
        return this.getBaseName().equals(upgrade.getBaseName());
    }
}
