package com.santipdr.winterfall.registry;

import com.santipdr.winterfall.WinterFall;
import com.santipdr.winterfall.item.FirearmItem;
import com.santipdr.winterfall.item.MeleeWeaponItem;
import com.santipdr.winterfall.weapon.FirearmProfile;
import com.santipdr.winterfall.weapon.MeleeProfile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WinterFall.MOD_ID);
    public static final RegistryObject<Item> SHORT_AMMO = ITEMS.register("short_ammo", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LONG_AMMO = ITEMS.register("long_ammo", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SHOTGUN_SHELL = ITEMS.register("shotgun_shell", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BILHOOK_BLUEPRINT = ITEMS.register("billhook_blueprint", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> FIELD_RATION = ITEMS.register("field_ration", () -> new Item(new Item.Properties().food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(5).saturationMod(0.65F).build())));
    public static final RegistryObject<Item> CANTEEN = ITEMS.register("canteen", () -> new com.santipdr.winterfall.item.CanteenItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<MeleeWeaponItem> COMBAT_KNIFE = melee("combat_knife", 5.0F, 13.0F, 3.1F, 7, 22, .24F, .25F, 18);
    public static final RegistryObject<MeleeWeaponItem> BILLHOOK = melee("billhook", 7.0F, 19.0F, 3.7F, 9, 28, .34F, .45F, 24);
    public static final RegistryObject<MeleeWeaponItem> PIPE_WRENCH = melee("pipe_wrench", 6.0F, 17.0F, 3.0F, 9, 25, .08F, .65F, 23);
    public static final RegistryObject<MeleeWeaponItem> CLEAVER = melee("cleaver", 6.5F, 18.0F, 3.0F, 8, 25, .30F, .35F, 22);
    public static final RegistryObject<MeleeWeaponItem> MACHETE = melee("machete", 6.0F, 16.0F, 3.45F, 8, 24, .25F, .30F, 20);
    public static final RegistryObject<MeleeWeaponItem> HATCHET = melee("hatchet", 7.0F, 20.0F, 2.95F, 10, 29, .22F, .55F, 26);
    public static final RegistryObject<MeleeWeaponItem> SLEDGEHAMMER = melee("sledgehammer", 9.0F, 27.0F, 3.25F, 14, 36, .10F, 1.1F, 34);
    public static final RegistryObject<MeleeWeaponItem> CROWBAR = melee("crowbar", 6.0F, 16.0F, 3.15F, 8, 24, .10F, .50F, 22);
    public static final RegistryObject<MeleeWeaponItem> SPEAR = melee("scrap_spear", 5.5F, 15.0F, 4.4F, 8, 24, .18F, .35F, 24);
    public static final RegistryObject<MeleeWeaponItem> RIOT_BATON = melee("riot_baton", 4.5F, 12.0F, 2.8F, 6, 20, .04F, .70F, 18);
    public static final RegistryObject<MeleeWeaponItem> TRENCH_SHOVEL = melee("trench_shovel", 7.0F, 21.0F, 3.2F, 10, 30, .18F, .70F, 28);

    public static final RegistryObject<FirearmItem> BROOMHANDLE_22 = firearm("broomhandle_22", SHORT_AMMO, 10, 18, 38, 240, 36, 2.4F, 26);
    public static final RegistryObject<FirearmItem> MARK_VII = firearm("mark_vii", SHORT_AMMO, 7, 45, 95, 120, 48, 1.1F, 32);
    public static final RegistryObject<FirearmItem> SERVICE_RIFLE = firearm("service_rifle", LONG_AMMO, 20, 31, 72, 360, 64, .55F, 38);
    public static final RegistryObject<FirearmItem> PUMP_SHOTGUN = firearm("pump_shotgun", SHOTGUN_SHELL, 6, 42, 78, 70, 22, 5.5F, 42);
    public static final RegistryObject<FirearmItem> IMPROVISED_SMG = firearm("improvised_smg", SHORT_AMMO, 24, 15, 32, 600, 32, 3.2F, 46);
    public static final RegistryObject<FirearmItem> HUNTING_RIFLE = firearm("hunting_rifle", LONG_AMMO, 5, 58, 118, 55, 80, .25F, 48);
    public static final RegistryObject<FirearmItem> FLARE_GUN = firearm("flare_gun", SHORT_AMMO, 1, 12, 20, 40, 24, 3.8F, 24);

    public static final RegistryObject<Item> SCRAPPER = ITEMS.register("scrapper", () -> new ItemNameBlockItem(ModBlocks.SCRAPPER.get(), new Item.Properties()));
    public static final RegistryObject<Item> WORKBENCH = ITEMS.register("workbench", () -> new ItemNameBlockItem(ModBlocks.WORKBENCH.get(), new Item.Properties()));
    public static final RegistryObject<Item> COMBAT_KNIFE_SCAVENGER_SPAWN_EGG = ITEMS.register("combat_knife_scavenger_spawn_egg",
            () -> new net.minecraftforge.common.ForgeSpawnEggItem(ModEntityTypes.COMBAT_KNIFE_SCAVENGER, 0x29333D, 0xB9C2C9, new Item.Properties()));

    private ModItems() {}

    private static RegistryObject<MeleeWeaponItem> melee(String name, float light, float heavy, float reach, float cost, float heavyCost, float bleed, float knockback, int cooldown) {
        return ITEMS.register(name, () -> new MeleeWeaponItem(new MeleeProfile(light, heavy, reach, cost, heavyCost, bleed, knockback, cooldown), new Item.Properties().stacksTo(1)));
    }

    private static RegistryObject<FirearmItem> firearm(String name, java.util.function.Supplier<Item> ammo, int magazine, float body, float head, int rpm, float range, float spread, int reloadTicks) {
        return ITEMS.register(name, () -> new FirearmItem(new FirearmProfile(ammo, magazine, body, head, rpm, range, spread, reloadTicks), new Item.Properties().stacksTo(1)));
    }
}
