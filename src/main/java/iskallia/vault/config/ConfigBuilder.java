package iskallia.vault.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigBuilder {
    public final ForgeConfigSpec CLIENT;
    public ForgeConfigSpec.IntValue arenaMagicNumber;
    public ForgeConfigSpec.BooleanValue arenaEnabled;
    public ForgeConfigSpec.EnumValue<ArenaTypeEnum> arenaInteractionType;


    public ConfigBuilder() {
    // CLIENT
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Arena");

        arenaMagicNumber = builder
            .comment("Magic number needed to activate the Arena.  Default: 10")
            .defineInRange("arenaMagicNumber", 10, 1, 250000);

        arenaInteractionType = builder
            .comment("Type of viewer interaction that counts toward the Magic number needed to activate the Arena.  Default: \"SUBS\"")
            .defineEnum("arenaInteractionType", ArenaTypeEnum.SUBS);

        arenaEnabled = builder
            .comment("Enable the Arena ?  Default: true")       // allow non-affiliates to turn the jar off
            .define("arenaEnabled", true);

        builder.pop();
        CLIENT = builder.build();
    }

    public enum ArenaTypeEnum { CHANNEL_POINTS, SUBS, BITS }
}
