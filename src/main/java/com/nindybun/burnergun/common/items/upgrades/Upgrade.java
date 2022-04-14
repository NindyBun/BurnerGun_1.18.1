package com.nindybun.burnergun.common.items.upgrades;

import net.minecraft.world.item.ItemStack;

public enum Upgrade {
    TIER_1("base_tier_1", 1, 0, 5, 0, false, "type.base"),
    TIER_2("base_tier_2", 2, 0, 5, 0, false, "type.base"),
    TIER_3("base_tier_3", 3, 0, 5, 0, false, "type.base"),
    TIER_4("base_tier_4", 4, 0, 5, 0, false, "type.base"),
    TIER_5("base_tier_5", 5, 0, 5, 0, false, "type.base"),
    //Burner Gun
    FOCAL_POINT_1("focal_point_1", 1, 75, 1, 12, false, "type.burnergun"),
    FOCAL_POINT_2("focal_point_2", 2, 100, 1, 24, false, "type.burnergun"),
    FOCAL_POINT_3("focal_point_3", 3, 125, 1, 32, false, "type.burnergun"),

    //Tools
    FORTUNE_1("fortune_1", 1, 100, 1, 0, true, "type.tool"),
    FORTUNE_2("fortune_2", 2, 125, 1, 0, true, "type.tool"),
    FORTUNE_3("fortune_3", 3, 175, 1, 0, true, "type.tool"),
    HORIZONTAL_EXPANSION_1("horizontal_expansion_1", 1, 0, 1, 0, false, "type.tool"),
    HORIZONTAL_EXPANSION_2("horizontal_expansion_2", 2, 0, 1, 0, false, "type.tool"),
    HORIZONTAL_EXPANSION_3("horizontal_expansion_3", 3, 0, 1, 0, false, "type.tool"),
    HORIZONTAL_EXPANSION_4("horizontal_expansion_4", 4, 0, 1, 0, false, "type.tool"),
    VERTICAL_EXPANSION_1("vertical_expansion_1", 1, 0, 1, 0, false, "type.tool"),
    VERTICAL_EXPANSION_2("vertical_expansion_2", 2, 0, 1, 0, false, "type.tool"),
    VERTICAL_EXPANSION_3("vertical_expansion_3", 3, 0, 1, 0, false, "type.tool"),
    VERTICAL_EXPANSION_4("vertical_expansion_4", 4, 0, 1, 0, false, "type.tool"),
    VEIN_MINER_1("vein_miner_1", 1, 0, 1, 64*1, true, "type.tool"),
    VEIN_MINER_2("vein_miner_2", 2, 0, 1, 64*2, true, "type.tool"),
    VEIN_MINER_3("vein_miner_3", 3, 0, 1, 64*3, true, "type.tool"),
    VEIN_MINER_4("vein_miner_4", 4, 0, 1, 64*4, true, "type.tool"),
    VEIN_MINER_5("vein_miner_5", 5, 0, 1, 64*5, true, "type.tool"),
    MAGNET("magnet", -1, 75, 1, 0, true, "type.tool"),
    SILK_TOUCH("silk_touch", -1, 150, 1, 0, true, "type.tool"),
    LIGHT("light", -1, 50, 1, 0, true, "type.tool"),
    AUTO_SMELT("auto_smelt", -1, 50 + (int)(Math.random() * ((175 - 50) + 1)), 1, 0, true, "type.tool"),
    TRASH("trash", -1, 75, 1, 0, true, "type.tool"),

    //Utilities
    FUEL_EFFICIENCY_1("fuel_efficiency_1", 1, 0, 1, 0.15, false, "type.util"),
    FUEL_EFFICIENCY_2("fuel_efficiency_2", 2, 0, 1, 0.35, false, "type.util"),
    FUEL_EFFICIENCY_3("fuel_efficiency_3", 3, 0, 1, 0.50, false, "type.util"),
    FUEL_EFFICIENCY_4("fuel_efficiency_4", 4, 0, 1, 0.75, false, "type.util"),
    FUEL_EFFICIENCY_5("fuel_efficiency_5", 5, 0, 1, 0.90, false, "type.util"),
    AMBIENCE_1("ambience_1", 1, 0, 1, 1, false, "type.util"),
    AMBIENCE_2("ambience_2", 1, 0, 1, 2, false, "type.util"),
    AMBIENCE_3("ambience_3", 1, 0, 1, 6, false, "type.util"),
    AMBIENCE_4("ambience_4", 1, 0, 1, 24, false, "type.util"),
    AMBIENCE_5("ambience_5", 1, 0, 1, 120, false, "type.util"),

    //Sword
    REACH_1("reach_1", 1, 10, 1, 10, false, "type.sword"),
    REACH_2("reach_2", 2, 20, 1, 15, false, "type.sword"),
    REACH_3("reach_3", 3, 30, 1, 25, false, "type.sword"),
    LOOTING_1("looting_1", 1, 100, 1, 0, true, "type.sword"),
    LOOTING_2("looting_2", 2, 125, 1, 0, true, "type.sword"),
    LOOTING_3("looting_3", 3, 175, 1, 0, true, "type.sword"),
    FIRE_ASPECT_1("fire_aspect_1", 1, 100, 1, 0, true, "type.sword"),
    FIRE_ASPECT_2("fire_aspect_2", 1, 175, 1, 0, true, "type.sword"),
    SHARPNESS_1("sharpness_1", 1, 75, 1, 0, true, "type.sword"),
    SHARPNESS_2("sharpness_2", 2, 100, 1, 0, true, "type.sword"),
    SHARPNESS_3("sharpness_3", 3, 125, 1, 0, true, "type.sword"),
    SHARPNESS_4("sharpness_4", 4, 150, 1, 0, true, "type.sword"),
    SHARPNESS_5("sharpness_5", 5, 175, 1, 0, true, "type.sword"),
    SMITE_1("smite_1", 1, 75, 1, 0, true, "type.sword"),
    SMITE_2("smite_2", 2, 100, 1, 0, true, "type.sword"),
    SMITE_3("smite_3", 3, 125, 1, 0, true, "type.sword"),
    SMITE_4("smite_4", 4, 150, 1, 0, true, "type.sword"),
    SMITE_5("smite_5", 5, 175, 1, 0, true, "type.sword"),
    BANE_OF_ARTHROPODS_1("bane_of_arthropods_1", 1, 75, 1, 0, true, "type.sword"),
    BANE_OF_ARTHROPODS_2("bane_of_arthropods_2", 2, 100, 1, 0, true, "type.sword"),
    BANE_OF_ARTHROPODS_3("bane_of_arthropods_3", 3, 125, 1, 0, true, "type.sword"),
    BANE_OF_ARTHROPODS_4("bane_of_arthropods_4", 4, 150, 1, 0, true, "type.sword"),
    BANE_OF_ARTHROPODS_5("bane_of_arthropods_5", 5, 175, 1, 0, true, "type.sword"),
    ATTACK_SPEED_1("attack_speed_1", 1, 75, 1, 1.6*2.5, false, "type.sword"),
    ATTACK_SPEED_2("attack_speed_2", 2, 100, 1, 1.6*2.5*2, false, "type.sword"),
    ATTACK_SPEED_3("attack_speed_3", 3, 125, 1, 1.6*2.5*3, false, "type.sword"),
    ATTACK_SPEED_4("attack_speed_4", 4, 150, 1, 1.6*2.5*4, false, "type.sword"),
    ATTACK_SPEED_5("attack_speed_5", 5, 175, 1, 1.6*2.5*5, false, "type.sword"),
    ATTACK_DAMAGE_1("attack_damage_1", 1, 75, 1, 1.8, false, "type.sword"),
    ATTACK_DAMAGE_2("attack_damage_2", 2, 100, 1, 2.6, false, "type.sword"),
    ATTACK_DAMAGE_3("attack_damage_3", 3, 125, 1, 3.4, false, "type.sword"),
    ATTACK_DAMAGE_4("attack_damage_4", 4, 150, 1, 4.2, false, "type.sword"),
    ATTACK_DAMAGE_5("attack_damage_5", 5, 175, 1, 5, false, "type.sword"),
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
