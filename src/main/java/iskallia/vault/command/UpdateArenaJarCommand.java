package iskallia.vault.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.Vault;
import iskallia.vault.capabilities.PlayerArenaCap;
import iskallia.vault.capabilities.PlayerArenaCapProperties;
import iskallia.vault.config.ConfigBuilder;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.init.ModSounds;
import iskallia.vault.network.message.JarOverlayUpdateWithLevel;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;

import static net.minecraft.command.Commands.argument;

public class UpdateArenaJarCommand extends Command {
    @Override
    public String getName() {
        return "update_arenajar";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("nickname", StringArgumentType.string())
               .then(argument("amount", IntegerArgumentType.integer(1))
               .executes(context -> this.ArenaUpdateLevel(context,
                StringArgumentType.getString(context, "nickname"),
                IntegerArgumentType.getInteger(context, "amount")))));
    }

    private int ArenaUpdateLevel(CommandContext<CommandSource> context, String nickname, Integer amount) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrException();
        int currentLevel;

        PlayerArenaCap cap = player.getCapability(PlayerArenaCapProperties.PLAYER_CAPABILITY, null).orElse(null);
        if (cap != null) {
            cap.arenaSubsAdd(nickname);

            currentLevel = cap.arenaLevelGet();
            if (amount + currentLevel >= Vault.CONFIGURATION.arenaMagicNumber.get()) {
                cap.arenaLevelSet(amount + currentLevel - Vault.CONFIGURATION.arenaMagicNumber.get());

                STitlePacket titlePacket = new STitlePacket(STitlePacket.Type.TITLE,
                    new TranslationTextComponent("tip.the_vault.arena_title")
                        .setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_dd711e))));

                STitlePacket subtitlePacket = new STitlePacket(STitlePacket.Type.SUBTITLE,
                    new TranslationTextComponent(Vault.CONFIGURATION.arenaInteractionType.get() == ConfigBuilder.ArenaTypeEnum.SUBS
                        ? "tip.the_vault.arena_subtitle"
                        : "tip.the_vault.arena_subtitle2",
                        cap.arenaSubsList().size())
                        .setStyle(Style.EMPTY.withColor(Color.fromRgb(0x00_ddd01e))));

                player.connection.send(subtitlePacket);
                player.connection.send(titlePacket);

                Vector3d position = player.position();
                player.level.playSound(null, position.x, position.y, position.z, ModSounds.ARENA_HORNS_SFX, SoundCategory.PLAYERS, 0.55f, 1f);
                ((ServerWorld) player.level).sendParticles(ParticleTypes.EXPLOSION_EMITTER, position.x, position.y, position.z, 3, 1, 1, 1, 0.5);

                //Random rnd = new Random();
                //player.displayClientMessage(new StringTextComponent("Randomly chose: " + cap.arenaSubsList().getString(rnd.nextInt(cap.arenaSubsList().size()))), false);

                // do arena stuff...blah...blah
                cap.arenaSubsClear();
            } else
                cap.arenaLevelAdd(amount);
        }

        ModNetwork.CHANNEL.sendTo(new JarOverlayUpdateWithLevel(cap.arenaLevelGet()), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);

        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() { return false; }
}
