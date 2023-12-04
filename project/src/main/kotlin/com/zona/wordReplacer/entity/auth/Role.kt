package com.zona.wordReplacer.entity.auth

import jakarta.persistence.*
import java.util.UUID


@Entity
@Table(name = "role", uniqueConstraints = [
    UniqueConstraint(columnNames = ["member_id", "role"])
])
class Role(
    @Column(name = "role")
    val role: String,

    @ManyToOne(targetEntity = Member::class)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    val member: Member,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "role_id")
    val roleId: UUID? = null,
)