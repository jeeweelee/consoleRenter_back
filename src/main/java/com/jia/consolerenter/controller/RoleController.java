package com.jia.consolerenter.controller;

import com.jia.consolerenter.exception.UserAlreadyExistsException;
import com.jia.consolerenter.model.Role;
import com.jia.consolerenter.model.User;
import com.jia.consolerenter.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("/all-roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<List<Role>>(roleService.getRoles(), HttpStatus.FOUND);
    }

    @PostMapping("/create-new-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createRole(@RequestBody Role theRole){
        try{
            roleService.createRole((theRole));
            return ResponseEntity.ok("New role created successfully");
        }
        catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        }
    }
    @DeleteMapping("/delete/{roleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteRole(roleId);
    }
    @PostMapping("/remove-all-users/{roleId}")
    public Role removeAllUsersFromRole(@PathVariable("roleId") Long roleId){
        return roleService.removeAllUserFromRole(roleId);
    }
    @PostMapping("remove-user-from-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User removeUserFromRole(@RequestParam("userId") Long userId,
                                   @RequestParam("roleId")Long roleId){
        return roleService.removeUserFromRole(userId,roleId);

    }
    @PostMapping("/assign-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User assignRoletoUser (@RequestParam("userId") Long userId,
                                  @RequestParam("roleId")Long roleId){
        return roleService.assignRoletoUser(userId,roleId);
    }
}
