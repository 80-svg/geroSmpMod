package org.makis.gerosmpmod;

import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public interface LockableChestHolder {
    enum LockType implements StringRepresentable {
        NONE("none"),
        UUID("uuid"),
        PIN("pin");
        private final String name;

        public static final StringRepresentable.EnumCodec<LockType> CODEC =
                StringRepresentable.fromEnum(LockType::values);

        LockType(String name) {
            this.name = name;
        }

        @Override
        public @NonNull String getSerializedName() {
            return this.name;
        }
        public static LockType fromString(String name) {
            for (LockType l : values()) {
                if (l.name.equals(name)) return l;
            }
            return NONE;
        }
    }
    boolean isLocked();

    LockType getLockType();
    void setLockType(LockType lockType);

    UUID getOwnerUuid();
    void setOwnerUuid(UUID uuid);

    String getPinHash();
    void setPinHash(String pinHash);

    void clearLock();
}
