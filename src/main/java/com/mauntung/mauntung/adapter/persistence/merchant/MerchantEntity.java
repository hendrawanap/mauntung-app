package com.mauntung.mauntung.adapter.persistence.merchant;

import com.mauntung.mauntung.adapter.persistence.membership.MembershipEntity;
import com.mauntung.mauntung.adapter.persistence.user.UserEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Entity
@Table(name = "merchant")
public class MerchantEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String name;

    @Column
    private String phone;

    @OneToOne(mappedBy = "merchant")
    private MembershipEntity membership;

    @UpdateTimestamp
    private Date updatedAt;

    @CreationTimestamp
    private Date createdAt;
}
