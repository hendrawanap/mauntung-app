package com.mauntung.mauntung.adapter.persistence.membership;

import com.mauntung.mauntung.adapter.persistence.merchant.MerchantEntity;
import com.mauntung.mauntung.adapter.persistence.reward.RewardEntity;
import com.mauntung.mauntung.adapter.persistence.tier.TierEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Entity
@Table(name = "membership")
public class MembershipEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "merchant_id")
    private MerchantEntity merchant;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String rules;

    @OneToMany(mappedBy = "membership")
    private Set<RewardEntity> rewards;

    @OneToMany(mappedBy = "membership")
    private Set<TierEntity> tiers;

    @Column(nullable = false)
    private Boolean isFinalized;

    @UpdateTimestamp
    private Date updatedAt;

    @CreationTimestamp
    private Date createdAt;
}
