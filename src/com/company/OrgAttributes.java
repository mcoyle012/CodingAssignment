package com.company;

import java.util.*;

/**
 * Contains the attributes of an organization as defined by the parameters
 * read in from the corresponding input file, and also holds the list of
 * users assigned to this organization that resulted from processing the
 * user input file.
 */
class OrgAttributes {
    private int id;
    private int parentId;
    private String name;
    private boolean hasParent;
    private final List<UserAttributes> userList = new ArrayList<>();

    public OrgAttributes(int oId, int pId, String assignedName, boolean parent) {
        id = oId;
        parentId = pId;
        name = assignedName;
        hasParent = parent;
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public boolean getHasParent() {
        return hasParent;
    }

    public List<UserAttributes> getUserList() {
        return userList;
    }

}
