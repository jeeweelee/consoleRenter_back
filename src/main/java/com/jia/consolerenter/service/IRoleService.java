package com.jia.consolerenter.service;

import com.jia.consolerenter.model.Role;
import com.jia.consolerenter.model.User;

import java.util.List;

public interface IRoleService {
    List<Role> getRoles();
    Role createRole(Role role);
    void deleteRole(Long id);
    Role findByName(String name);
    User removeUserFromRole(Long userId,Long roleId);
    User assignRoletoUser(Long userId,Long roleId);
    Role removeAllUserFromRole(Long roleId);
}
