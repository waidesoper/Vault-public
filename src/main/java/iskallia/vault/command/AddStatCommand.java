package iskallia.vault.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.Vault;
import net.minecraft.command.CommandSource;

import static net.minecraft.command.Commands.argument;

public class AddStatCommand extends Command {
    @Override
    public String getName() {
        return "add_stat";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("amount", IntegerArgumentType.integer(1))
               .then(argument("stat_type", IntegerArgumentType.integer(1, 2))
               .executes(context -> receivedSub(context, IntegerArgumentType.getInteger(context, "amount"),  IntegerArgumentType.getInteger(context, "stat_type")))));
    }

    private int receivedSub(CommandContext<CommandSource> context, int amount, int stat_type) throws CommandSyntaxException {
        switch (stat_type) {
            case 1:
                context.getSource().getPlayerOrException().awardStat(Vault.STAT_GIVEN_TRADERS, amount);    // #Crimson_Fluff, AddStat
                break;

            case 2:
                context.getSource().getPlayerOrException().awardStat(Vault.STAT_GIVEN_BOOSTERS, amount);    // #Crimson_Fluff, AddStat
                break;

            case 3:
                context.getSource().getPlayerOrException().awardStat(Vault.STAT_GIVEN_GIFTS, amount);    // #Crimson_Fluff, AddStat
                break;
        }

        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() { return false; }
}
