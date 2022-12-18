package com.mauntung.mauntung.adapter.persistence.reward;

import com.mauntung.mauntung.adapter.persistence.membership.MembershipEntity;
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
@Table(name = "reward")
public class RewardEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "membership_id")
    private MembershipEntity membership;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false, columnDefinition = "text")
    private String termsCondition;

    @Column(nullable = false)
    private Integer cost;

    private Date startPeriod;

    private Date endPeriod;

    private Integer stock;

    @ManyToMany(mappedBy = "rewards")
    private Set<TierEntity> tiers;

    @UpdateTimestamp
    private Date updatedAt;

    @CreationTimestamp
    private Date createdAt;
}
