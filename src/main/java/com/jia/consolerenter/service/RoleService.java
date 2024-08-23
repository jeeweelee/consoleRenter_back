package com.jia.consolerenter.service;

import com.jia.consolerenter.exception.ResourceNotFoundException;
import com.jia.consolerenter.exception.UserAlreadyExistsException;
import com.jia.consolerenter.model.Role;
import com.jia.consolerenter.model.User;
import com.jia.consolerenter.repository.RoleRepository;
import com.jia.consolerenter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role theRole) {
        String roleName = "ROLE_"+theRole.getName().toUpperCase();
        Role role = new Role(roleName);
        if (roleRepository.existsByName(roleName)){
            throw new UserAlreadyExistsException(theRole.getName()+" role already exists");
        }
        return roleRepository.save(role);
    }



    @Override
    public void deleteRole(Long id) {
        this.removeAllUserFromRole(id);
        roleRepository.deleteById(id);

    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).get();
    }

    @Override
    public User removeUserFromRole(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if(role.isPresent() && role.get().getUsers().contains(user.get())){
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            return user.get();
        }
        throw new ResourceNotFoundException("User not found");
    }

    @Override
    public User assignRoletoUser(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if(user.isPresent() && user.get().getRoles().contains(role.get())){
            throw new ResourceNotFoundException(user.get().getFirstName()+ "is already assigned to that" +role.get().getName() + "role");
        }
        if(role.isPresent()){
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());

        }
        return user.get();
    }

    @Override
    public Role removeAllUserFromRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        role.get().removeAllUsersFromRole();
        return roleRepository.save(role.get());
    }

}
