package com.javawhizz.SpringSecurity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "authority")
public class Authority {
    @Id
    @GeneratedValue
    @Column(name = "authority_id")
    private Long authorityId;

    @Column(name = "authority_name")
    private String authorityName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    @JsonIgnore
    private Role role;

    public Authority(String authorityName){
        this.authorityName = authorityName;
    }
}
