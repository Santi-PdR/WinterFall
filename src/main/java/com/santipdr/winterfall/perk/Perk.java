package com.santipdr.winterfall.perk;

import java.util.Locale;

public enum Perk {
    VANGUARD("Vanguard", "20% more stamina, built for the frontline."),
    SCAVENGER("Scavenger", "Earns 25% more salvage."),
    MARKSMAN("Marksman", "Steadier weapon handling and reduced firearm spread."),
    FIELD_MEDIC("Field Medic", "Wound and bleeding recovery is accelerated."),
    SENTINEL("Sentinel", "Resists part of storm and scavenger damage."),
    RUNNER("Runner", "Reduced sprint stamina consumption.");

    private final String displayName;
    private final String description;

    Perk(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String displayName() { return displayName; }
    public String description() { return description; }

    public static Perk byId(String id) {
        try {
            return valueOf(id.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return VANGUARD;
        }
    }
}
