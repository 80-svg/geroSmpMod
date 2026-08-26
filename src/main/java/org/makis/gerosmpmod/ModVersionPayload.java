package org.makis.gerosmpmod;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class ModVersionPayload {
    public record VersionPayload(String version, List<String> message) implements CustomPacketPayload {

        public static final CustomPacketPayload.Type<VersionPayload> ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, "mod_version_payload"));

        public static final StreamCodec<ByteBuf, VersionPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, VersionPayload::version,
                ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list(256)), VersionPayload::message,
                VersionPayload::new
        );

        @Override
        public @NonNull Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
}
