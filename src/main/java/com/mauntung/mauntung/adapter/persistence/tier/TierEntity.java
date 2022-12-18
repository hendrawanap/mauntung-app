package com.mauntung.mauntung.adapter.persistence.tier;

import com.mauntung.mauntung.adapter.persistence.membership.MembershipEntity;
import com.mauntung.mauntung.adapter.persistence.reward.RewardEntity;
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
@Table(name = "tier")
public class TierEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "membership_id")
    private MembershipEntity membership;

    @Column(columnDefinition = "integer default 0")
    private Integer requiredPoints;

    @Column(columnDefinition = "float default 1.0")
    private Float multiplierFactor;

    @ManyToMany
    @JoinTable(
        name = "reward_tier",
        joinColumns = @JoinColumn(name = "tier_id"),
        inverseJoinColumns = @JoinColumn(name = "reward_id")
    )
    private Set<RewardEntity> rewards;

    @UpdateTimestamp
    private Date updatedAt;

    @CreationTimestamp
    private Date createdAt;
}
