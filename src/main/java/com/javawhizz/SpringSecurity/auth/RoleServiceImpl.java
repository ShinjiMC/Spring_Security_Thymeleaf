package com.javawhizz.SpringSecurity.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleUSER() {
        Role role = new Role("USER")
                .addAuthorities(Set.of(
                        new Authority("customer:read"),
                        new Authority("customer:write")
                ));
        Optional<Role> theRole = roleRepository.findRoleByRoleName(role.getRoleName());
        if (theRole.isEmpty()){
            return roleRepository.save(role);
        }
        return theRole.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("role not found"));
    }
}
