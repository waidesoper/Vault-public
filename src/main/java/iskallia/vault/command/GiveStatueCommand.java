package iskallia.vault.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.block.item.LootStatueBlockItem;
import iskallia.vault.util.StatueType;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

public class GiveStatueCommand extends Command {
    @Override
    public String getName() {
        return "give_statue";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        for (StatueType type : StatueType.values()) {
            builder.then(literal(type.name().toUpperCase())
                .then(argument("nickname", StringArgumentType.string())
                .executes(context -> this.giveGiftStatue(context, type, StringArgumentType.getString(context, "nickname")))));
        }
    }

    private int giveGiftStatue(CommandContext<CommandSource> context, StatueType type, String nickname) throws CommandSyntaxException {
        ItemStack stack = LootStatueBlockItem.getStatueBlockItem(nickname, type, false, false);
        ServerPlayerEntity player = context.getSource().getPlayerOrException();

        if (! player.addItem(stack)) player.drop(stack, false);

        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() { return false; }
}
