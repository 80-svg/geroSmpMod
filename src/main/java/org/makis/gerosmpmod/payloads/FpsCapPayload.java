package org.makis.gerosmpmod.payloads;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.makis.gerosmpmod.GeroSmpMod;

public record FpsCapPayload(boolean active, int fps) implements CustomPacketPayload {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, "fps_cap");
    public static final CustomPacketPayload.Type<FpsCapPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, FpsCapPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            FpsCapPayload::active,
            ByteBufCodecs.INT,
            FpsCapPayload::fps,
            FpsCapPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
