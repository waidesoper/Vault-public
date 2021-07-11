package iskallia.vault.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.Vault;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.ItemBit;
import iskallia.vault.util.EntityHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static net.minecraft.command.Commands.argument;

public class GiveBitsCommand extends Command {
    public static List<ItemBit> BIT_ITEMS;

    private static void initializeBits() {
        BIT_ITEMS = new LinkedList<>();
        BIT_ITEMS.add(ModItems.BIT_10000);
        BIT_ITEMS.add(ModItems.BIT_5000);
        BIT_ITEMS.add(ModItems.BIT_1000);
        BIT_ITEMS.add(ModItems.BIT_100);
        BIT_ITEMS.sort(Comparator.comparingInt(b -> - b.getValue())); // Sort by decreasing bitItem::value
        // ^^^ Here to make this thing fail-safe
    }

    @Override
    public String getName() {
        return "give_bits";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("amount", IntegerArgumentType.integer())
            .executes(context -> dropBits(context, IntegerArgumentType.getInteger(context, "amount"))));
    }

    public int dropBits(CommandContext<CommandSource> context, int bitsInput) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrException();

        if (BIT_ITEMS == null) initializeBits(); // <-- Lazy init to prevent any registry order errors

        for (ItemBit bitItem : BIT_ITEMS) {
            if (bitsInput < bitItem.getValue()) continue;

            int amount = bitsInput / bitItem.getValue();

            EntityHelper.giveItem(player, new ItemStack(bitItem, amount));
            player.awardStat(Vault.STAT_GIVEN_BITS, amount * bitItem.getValue());    // #Crimson_Fluff, AddStat, only count bits we have bit_items for (so no single bits)

            bitsInput %= bitItem.getValue();
        }

        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }
}
