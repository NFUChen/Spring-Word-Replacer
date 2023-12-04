package com.zona.wordReplacer.entity.auth

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "member")
class Member(
    @Column(name = "name", unique = true, nullable = false)
    val name: String,
    @Column(name = "email", unique =  true, nullable = false)
    val email: String,
    @Column(name = "password", columnDefinition = "CHAR(68)", nullable = false)
    val password: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    val memberId: Long? = null,

    @JsonIgnore
    @OneToMany(targetEntity = Role::class, cascade = [CascadeType.ALL], mappedBy = "member")
    val roles: MutableList<Role> = mutableListOf(),



) {

}