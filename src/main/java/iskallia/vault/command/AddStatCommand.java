package iskallia.vault.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.Vault;
import net.minecraft.command.CommandSource;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

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
        for (Type type : AddStatCommand.Type.values()) {
            builder.then(literal(type.name().toUpperCase())
                .then(argument("amount", IntegerArgumentType.integer(1))
                    .executes(context -> this.receivedSub(context, IntegerArgumentType.getInteger(context, "amount"), type))));
        }
    }

    private int receivedSub(CommandContext<CommandSource> context, int amount, Type stat_type) throws CommandSyntaxException {
        switch (stat_type) {
            case TRADERS:
                context.getSource().getPlayerOrException().awardStat(Vault.STAT_GIVEN_TRADERS, amount);    // #Crimson_Fluff, AddStat
                break;

            case BOOSTERS:
                context.getSource().getPlayerOrException().awardStat(Vault.STAT_GIVEN_BOOSTERS, amount);   // #Crimson_Fluff, AddStat
                break;

            case GIFTS:
                context.getSource().getPlayerOrException().awardStat(Vault.STAT_GIVEN_GIFTS, amount);      // #Crimson_Fluff, AddStat
                break;

            case BITS:
                context.getSource().getPlayerOrException().awardStat(Vault.STAT_GIVEN_BITS, amount);       // #Crimson_Fluff, AddStat
                break;
        }

        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() { return false; }

    public enum Type { TRADERS, BOOSTERS, GIFTS, BITS }
}
