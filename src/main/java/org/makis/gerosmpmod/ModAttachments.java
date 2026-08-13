package org.makis.gerosmpmod;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;

public final class ModAttachments {
    public static final AttachmentType<Integer> BOUNTY_AMOUNT = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, "bounty_amount"),
            integerBuilder -> integerBuilder.initializer(() -> 0).persistent(Codec.INT).copyOnDeath()); // Default as 0
    private ModAttachments() {}
    public static void initialize() {}
}
