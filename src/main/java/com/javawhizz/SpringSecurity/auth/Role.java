package com.javawhizz.SpringSecurity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.javawhizz.SpringSecurity.security.UserAccount;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_name")
    private String roleName;

    //TODO: we will create this class later

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "role",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            }
    )
    private Set<Authority> authorities = new HashSet<>();

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "userAccountId")
    @JsonIgnore
    private UserAccount userAccount;

    public Role(String roleName){
        this.roleName = roleName;
    }

    public Role addAuthorities(Set<Authority> authorities){
        for (Authority authority : authorities) {
            if (authority != null){
                this.authorities.add(authority);
                authority.setRole(this);
            }
        }
        return this;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> grantedAuthorities = this.authorities
                .stream()
                .map(authority ->
                        new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toSet());
        grantedAuthorities.add(
                new SimpleGrantedAuthority("ROLE_"+this.roleName));
        return grantedAuthorities;
    }
}
