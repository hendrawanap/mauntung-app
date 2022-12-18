package com.mauntung.mauntung.adapter.persistence.customer;

import com.mauntung.mauntung.adapter.persistence.membership.MembershipEntity;
import com.mauntung.mauntung.adapter.persistence.user.UserEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class CustomerEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String name;

    private Boolean gender;

    private Date birthDate;

    @Column(nullable = false)
    private UUID customerCode;

    private String phone;

    @ManyToMany
    @JoinTable(
        name = "customer_membership",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "membership_id")
    )
    private Set<MembershipEntity> memberships;

    @UpdateTimestamp
    private Date updatedAt;

    @CreationTimestamp
    private Date createdAt;
}
