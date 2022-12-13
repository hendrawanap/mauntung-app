package com.mauntung.mauntung.domain.model.stamp;

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
        this.id = null;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
        this.usedAt = null;
    }

    public Stamp(Date expiredAt, Date createdAt, Date usedAt) {
        this.id = null;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
        this.usedAt = usedAt;
    }

    public Stamp(Long id, Date expiredAt, Date createdAt) {
        this.id = id;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
        this.usedAt = null;
    }

    public Stamp(Long id, Date expiredAt, Date createdAt, Date usedAt) {
        this.id = id;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
        this.usedAt = usedAt;
    }

    public boolean isUsable() {
        boolean isExpired = new Date().after(expiredAt);
        return !(isExpired || isUsed());
    }

    public boolean isUsed() {
        return usedAt != null;
    }
}
