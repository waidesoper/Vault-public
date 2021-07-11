package iskallia.vault.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.init.ModItems;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.command.CommandSource;

import static net.minecraft.command.Commands.literal;

public class RaidCommand extends Command {
    @Override
    public String getName() {
        return "raid";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        for (Type type : Type.values()) {
            builder.then(literal(type.name().toUpperCase())
                .then(literal("start")
                    .executes(context -> this.startRaid(context, type))));
        }
    }

    private int startRaid(CommandContext<CommandSource> context, Type type) throws CommandSyntaxException {
        VaultRaidData.get(context.getSource().getLevel()).startNew(context.getSource().getPlayerOrException(), ModItems.VAULT_CRYSTAL_OMEGA, type == Type.FINAL_VAULT);

        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() { return false; }

    public enum Type { VAULT, FINAL_VAULT }
}
