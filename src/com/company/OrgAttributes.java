package com.company;

import java.util.*;

/**
 * Contains the attributes of an organization as defined by the parameters
 * read in from the corresponding input file, and also holds the list of
 * users assigned to this organization that resulted from processing the
 * user input file.
 */
class OrgAttributes {
    int Id;
    int parentId;
    String name;
    boolean hasParent;
    final List<UserAttributes> userList = new ArrayList<>();

    public int getId() {
        return Id;
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

}
