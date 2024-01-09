package com.zona.wordReplacer.entity.auth

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

data class MemberView(
    val name: String?,
    val email: String?,
    val memberId: Long?,
    val roles: List<String>
)

@Entity
@Table(name = "member")
class Member(
    @Column(name = "name", unique = true, nullable = false)
    var name: String?,
    @Column(name = "email", unique =  true, nullable = false)
    var email: String?,
    @Column(name = "password", columnDefinition = "VARCHAR(68)", nullable = false)
    var password: String?,

    @Column(name = "is_activated")
    var isActivated: Boolean = false,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    val id: Long? = null,

    @JsonIgnore
    @OneToMany(targetEntity = Role::class, cascade = [CascadeType.ALL], mappedBy = "member", fetch = FetchType.EAGER)
    var roles: MutableList<Role> = mutableListOf(),
) {

    fun toView(): MemberView {
        return MemberView(
            name,
            email,
            id,
            roles.map { it.role }
        )
    }

    fun isRole(role: UserRole): Boolean {
        return roles.map { it.role }.contains(role.roleName)
    }

}