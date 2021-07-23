package iskallia.vault.network.message;

import iskallia.vault.client.gui.overlay.JarPersonalOverlay;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class JarOverlayUpdateWithLevel {
    private int a = 0;
    public JarOverlayUpdateWithLevel() { }

    public JarOverlayUpdateWithLevel(int level) { this.a = level; }

    public static void write(JarOverlayUpdateWithLevel message, PacketBuffer buffer) {
        buffer.writeInt(message.a);
    }

    public static JarOverlayUpdateWithLevel read(PacketBuffer buffer) {
        JarOverlayUpdateWithLevel message = new JarOverlayUpdateWithLevel();
        message.a = buffer.readInt();
        return message;
    }

    public static void onMessage(JarOverlayUpdateWithLevel message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> { JarPersonalOverlay.currentLevel = message.a; });

        context.setPacketHandled(true);
    }
}
