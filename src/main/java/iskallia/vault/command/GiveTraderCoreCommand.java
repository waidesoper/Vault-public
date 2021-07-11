package iskallia.vault.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.item.ItemTraderCore;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

public class GiveTraderCoreCommand extends Command {
    @Override
    public String getName() {
        return "give_tradercore";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        for (ItemTraderCore.CoreType type : ItemTraderCore.CoreType.values()) {
            builder.then(literal(type.name().toUpperCase())
                .then(argument("nickname", StringArgumentType.string())
                .then(argument("megahead", BoolArgumentType.bool())
                .executes(context -> this.giveTraderCore(context, type,
                    StringArgumentType.getString(context, "nickname"),
                    BoolArgumentType.getBool(context, "megahead"))))));
        }
    }

    private int giveTraderCore(CommandContext<CommandSource> context, ItemTraderCore.CoreType type, String nickname, Boolean megahead) throws CommandSyntaxException {
        // #Crimson_Fluff, Value does not actually seem to be used
        ItemStack stack = ItemTraderCore.generate(nickname, 1, megahead, type);
        ServerPlayerEntity player = context.getSource().getPlayerOrException();

        if (! player.addItem(stack)) player.drop(stack, false);

        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() { return false; }
}
