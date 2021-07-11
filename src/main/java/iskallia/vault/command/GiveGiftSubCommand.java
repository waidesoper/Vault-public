package iskallia.vault.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.item.ItemGiftBomb;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

public class GiveGiftSubCommand extends Command {
    @Override
    public String getName() {
        return "give_giftsub";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        for (ItemGiftBomb.Variant type : ItemGiftBomb.Variant.values()) {
            builder.then(literal(type.name().toUpperCase())
                .then(argument("gifter", StringArgumentType.string())
                .then(argument("sub_count", IntegerArgumentType.integer(1))
                .executes(context -> this.giveGiftBomb(context, type,
                    StringArgumentType.getString(context, "gifter"),
                    IntegerArgumentType.getInteger(context, "sub_count"))))));
        }
    }

    private int giveGiftBomb(CommandContext<CommandSource> context, ItemGiftBomb.Variant type, String nickname, Integer amount) throws CommandSyntaxException {
        ItemStack stack = ItemGiftBomb.forGift(type, nickname, amount);
        ServerPlayerEntity player = context.getSource().getPlayerOrException();

        if (! player.addItem(stack)) player.drop(stack, false);

        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() { return false; }
}
