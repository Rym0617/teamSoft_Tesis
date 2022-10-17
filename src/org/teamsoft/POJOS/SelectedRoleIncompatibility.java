/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.POJOS;

import org.teamsoft.entity.Role;

/**
 *
 * @author Alejandro Durán
 */
public class SelectedRoleIncompatibility {

    private Role roleAFk;
    private Role roleBFk;

    public SelectedRoleIncompatibility(Role roleAFk, Role roleBFk) {

        this.roleAFk = roleAFk;
        this.roleBFk = roleBFk;
    }

    public Role getRoleAFk() {
        return roleAFk;
    }

    public void setRoleAFk(Role roleAFk) {
        this.roleAFk = roleAFk;
    }

    public Role getRoleBFk() {
        return roleBFk;
    }

    public void setRoleBFk(Role roleBFk) {
        this.roleBFk = roleBFk;
    }
}
