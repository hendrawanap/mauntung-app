package com.mauntung.mauntung.domain.model.stamp;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
public class Stamp {
    private final Long id;
    private final Date expiredAt;
    private final Date createdAt;
    @Setter private Date usedAt;

    public Stamp(Date expiredAt, Date createdAt) {
        this(null, expiredAt, createdAt, null);
    }

    public Stamp(Date expiredAt, Date createdAt, Date usedAt) {
        this(null, expiredAt, createdAt, usedAt);
    }

    public Stamp(Long id, Date expiredAt, Date createdAt) {
        this(id, expiredAt, createdAt, null);
    }

    public Stamp(Long id, Date expiredAt, Date createdAt, Date usedAt) {
        validate(expiredAt, createdAt, usedAt);

        this.id = id;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
        this.usedAt = usedAt;
    }

    private void validate(Date expiredAt, Date createdAt, Date usedAt) throws IllegalArgumentException {
        MessageBuilder mb = new MessageBuilder();

        if (expiredAt.before(createdAt))
            mb.append("Expired date must not be before the created date");

        if (usedAt != null && usedAt.after(expiredAt))
            mb.append("Used date must not be after the expired date");

        if (!mb.isEmpty())
            throw new IllegalArgumentException(mb.toString());
    }

    public boolean isUsable() {
        boolean isExpired = new Date().after(expiredAt);
        return !(isExpired || isUsed());
    }

    public boolean isUsed() {
        return usedAt != null;
    }
}
