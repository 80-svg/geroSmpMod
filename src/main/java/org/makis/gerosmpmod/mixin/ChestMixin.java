package org.makis.gerosmpmod.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.makis.gerosmpmod.LockableChestHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
@Mixin(ChestBlockEntity.class)
public class ChestMixin extends BlockEntity implements LockableChestHolder {
    @Unique private String pinHash = "";
    @Unique private UUID ownerUuid = null;
    @Unique private LockType lockType = LockType.NONE;

    public ChestMixin(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    @Override
    public boolean isLocked() {
        return this.lockType != LockType.NONE;
    }

    @Override
    public LockType getLockType() {
        return this.lockType;
    }

    @Override
    public void setLockType(LockType lockType) {
        this.lockType = lockType;
        this.setChanged();
    }

    @Override
    public UUID getOwnerUuid() {
        return this.ownerUuid;
    }

    @Override
    public void setOwnerUuid(UUID uuid) {
        this.ownerUuid = uuid;
        this.setChanged();
    }

    @Override
    public String getPinHash() {
        return this.pinHash;
    }

    @Override
    public void setPinHash(String pinHash) {
        this.pinHash = pinHash != null ? pinHash : "";
        this.setChanged();
    }

    /// Clears all data about locking
    @Override
    public void clearLock() {
        this.lockType = LockType.NONE;
        this.ownerUuid = null;
        this.pinHash = "";
        this.setChanged();
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void loadCustomData(ValueInput input, CallbackInfo ci) {
        this.lockType = LockType.fromString(input.getStringOr("LockType", ""));
        String ownerUuidStr = input.getStringOr("OwnerUuid", "");
        UUID parsedUuid = null;
        if (!ownerUuidStr.isEmpty()) {
            try {
                parsedUuid = UUID.fromString(ownerUuidStr);
            } catch (IllegalArgumentException ignored) {
            }
        }
        this.ownerUuid = parsedUuid;
        this.pinHash = input.getStringOr("PinHash", "");
    }
    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void saveCustomData(ValueOutput output, CallbackInfo ci) {
        output.putString("LockType", this.lockType.toString());
        if (this.ownerUuid != null) {
            output.putString("OwnerUuid", this.ownerUuid.toString());
        }
        if (!this.pinHash.isEmpty()) {
            output.putString("PinHash", this.pinHash);
        }
    }
}
