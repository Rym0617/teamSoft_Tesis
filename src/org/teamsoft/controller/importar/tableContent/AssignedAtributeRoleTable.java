package org.teamsoft.controller.importar.tableContent;

import org.teamsoft.entity.Role;

/**
 * contiene al atributo del fichero juto con el rol asociado
 */
public class AssignedAtributeRoleTable {

    int id;
    private Role rol;
    private String fileAtribute;

    public AssignedAtributeRoleTable(Role rol, String fileAtribute) {
        this.rol = rol;
        this.fileAtribute = fileAtribute;
    }

    public Role getRol() {
        return rol;
    }

    public void setRol(Role rol) {
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileAtribute() {
        return fileAtribute;
    }

    public void setFileAtribute(String fileAtribute) {
        this.fileAtribute = fileAtribute;
    }

    public String getRoleName() {
        return rol == null ? "" : rol.getRoleName();
    }
}
