package iskallia.vault.network.message;

import iskallia.vault.client.gui.overlay.VaultRaidOverlay;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StartBossLoopMessage {
    public StartBossLoopMessage() { }

    public static void write(StartBossLoopMessage message, PacketBuffer buffer) {}

    public static StartBossLoopMessage read(PacketBuffer buffer) {
        return new StartBossLoopMessage();
    }

    public static void onMessage(StartBossLoopMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> { VaultRaidOverlay.bossSummoned = true; });

        context.setPacketHandled(true);
    }
}
