package com.zona.wordReplacer.entity.auth

import jakarta.persistence.*
import java.util.UUID

enum class UserRole(val roleName: String) {
    GUEST("GUEST"),
    USER("USER"),
    ADMIN("ADMIN");

    companion object {
        fun fromString(roleName: String): UserRole {
            return UserRole.values().find { it.roleName.equals(roleName) }
                ?: throw IllegalArgumentException("No enum value for casting: $roleName")
        }
    }

    override fun toString(): String {
        return roleName
    }
}
@Entity
@Table(name = "role", uniqueConstraints = [
    UniqueConstraint(columnNames = ["member_id", "role"])
])
class Role(
    @Column(name = "role")
    val role: String,

    @ManyToOne(targetEntity = Member::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    val member: Member,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "role_id")
    val id: UUID? = null,
) {

    // Override equals and hashCode methods
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Role) return false

        if (role != other.role) return false
        if (member != other.member) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = role.hashCode()
        result = 31 * result + member.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }
}