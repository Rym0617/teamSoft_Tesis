/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.POJOS;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.teamsoft.entity.PersonGroup;
import org.teamsoft.model.PersonGroupFacade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Alejandro Durán
 */
@Named("groupTree")
@SessionScoped
@Deprecated
public class GroupTree implements Serializable{
    
    @EJB
    private PersonGroupFacade ejbFacade;
    private List<PersonGroup> groupList = null;

    private TreeNode root;
    
    public PersonGroup getGroup(String id) {
        return getEjbFacade().find(Long.parseLong(id));
    }
    
    public PersonGroupFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(PersonGroupFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    
    public List<PersonGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<PersonGroup> groupList) {
        this.groupList = groupList;
    }
        
    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
    
    public PersonGroup searchRoot (){
        PersonGroup group = null;
        boolean found = false;
   
        for (int i = 0; i < groupList.size() && !found; i++) {
            if (groupList.get(i).getParentGroup()==null) {
                group=groupList.get(i);
                found = true;
            }
        }
        return group;
    }
    
    public PersonGroup searchSon (int parentId){
        PersonGroup group = null;
        boolean found = false;
   
        for (int i = 0; i < groupList.size() && !found; i++) {
            if (groupList.get(i).getParentGroup().getId()==parentId) {
                group=groupList.get(i);
                found = true;
            }
        }
        return group;
    }
    
    @PostConstruct
    public void init() {
        
        groupList = ejbFacade.findAll();
        
        root = new DefaultTreeNode("Root", null);
        
        TreeNode nodeM = new DefaultTreeNode(searchRoot().getName(), root);
        
        for (int i = 0; i < groupList.size(); i++) {
            TreeNode node = new DefaultTreeNode(groupList.get(i).getName(),nodeM);
        }
                
   /*     TreeNode node1 = new DefaultTreeNode(searchSon(searchRoot().getId().intValue()).getName(), node0);*/
       
        TreeNode node10 = new DefaultTreeNode("Node 1.0", nodeM);
        node10.getChildren().add(new DefaultTreeNode("Node 1.0.0"));
        root.getChildren().add(new DefaultTreeNode("Node 2"));
    }
}
