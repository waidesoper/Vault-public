package iskallia.vault.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class PlayerArenaCapProperties {
    @CapabilityInject(PlayerArenaCap.class)
    public static Capability<PlayerArenaCap> PLAYER_CAPABILITY;
}
