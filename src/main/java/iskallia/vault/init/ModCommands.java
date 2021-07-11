package iskallia.vault.init;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import iskallia.vault.Vault;
import iskallia.vault.command.*;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import java.util.function.Supplier;

import static net.minecraft.command.Commands.literal;

public class ModCommands {
    public static ReloadConfigsCommand RELOAD_CONFIGS;
    public static RaidCommand RAID;
    public static VaultLevelCommand VAULT_LEVEL;
//    public static InternalCommand INTERNAL;
    public static GiveBitsCommand GIVE_BITS;
    public static GearDebugCommand GEAR_DEBUG_COMMAND;

    public static AddStatCommand ADD_STAT_COMMAND;                          // #Crimson_Fluff, so we can call AddStat
    public static GiveStatueCommand ADD_STATUE_COMMAND;                     // #Crimson_Fluff, so we can give Player Statue (Gift sub, Mega sub)
    public static GiveGiftSubCommand ADD_GIFTSUB_COMMAND;                   // #Crimson_Fluff, so we can give Player Gift Bomb)
    public static GiveTraderCoreCommand ADD_TRADERCORE_COMMAND;             // #Crimson_Fluff, so we can give Player Trader Core)

    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher, Commands.EnvironmentType env) {
        RELOAD_CONFIGS = registerCommand(ReloadConfigsCommand::new, dispatcher, env);
        RAID = registerCommand(RaidCommand::new, dispatcher, env);
        VAULT_LEVEL = registerCommand(VaultLevelCommand::new, dispatcher, env);
//        INTERNAL = registerCommand(InternalCommand::new, dispatcher, env);
        GIVE_BITS = registerCommand(GiveBitsCommand::new, dispatcher, env);
        GEAR_DEBUG_COMMAND = registerCommand(GearDebugCommand::new, dispatcher, env);

        ADD_STAT_COMMAND = registerCommand(AddStatCommand::new, dispatcher, env);                   // #Crimson_Fluff, so we can call AddStat
        ADD_STATUE_COMMAND = registerCommand(GiveStatueCommand::new, dispatcher, env);              // #Crimson_Fluff, so we can give Player Statue (Gift sub, Mega sub)
        ADD_GIFTSUB_COMMAND = registerCommand(GiveGiftSubCommand::new, dispatcher, env);            // #Crimson_Fluff, so we can give Player Gift Bomb
        ADD_TRADERCORE_COMMAND = registerCommand(GiveTraderCoreCommand::new, dispatcher, env);      // #Crimson_Fluff, so we can give Player Trader Core
    }

    public static <T extends Command> T registerCommand(Supplier<T> supplier, CommandDispatcher<CommandSource> dispatcher, Commands.EnvironmentType env) {
        T command = supplier.get();

        if (! command.isDedicatedServerOnly() || env == Commands.EnvironmentType.DEDICATED || env == Commands.EnvironmentType.ALL) {
            LiteralArgumentBuilder<CommandSource> builder = literal(command.getName());
            builder.requires((sender) -> sender.hasPermission(command.getRequiredPermissionLevel()));
            command.build(builder);
            dispatcher.register(literal(Vault.MOD_ID).then(builder));
        }

        return command;
    }
}
