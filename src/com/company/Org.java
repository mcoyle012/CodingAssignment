package com.company;

import java.util.*;

/**
 * Represents an organization in the org hierarchy tree, plus its relationship
 * to parent and child
 */
public class Org {
    private final OrgAttributes orgData;
    Org parent;
    private final List<Org> children;
    int orgBytes = 0;
    int orgFiles = 0;
    int descendentsNumBytes = 0;
    int descendentsNumFiles = 0;
    int descendentsNumUsers = 0;

    // Construct an Org based on parameters read from input file
    public Org(OrgAttributes data) {
        orgData = data;
        children = new ArrayList<>();
    }

    /*
     * Recursively calculate the number of users in this organization
     * and its children.
     */
    public int getTotalNumUsers() {
        return children.size() + descendentsNumUsers;
    }

    /*
      Calculate the total number of files in this organization
     */
    public int getTotalNumFiles() {
        return orgFiles + descendentsNumFiles;
    }

    /*
      Calculate the total number of bytes in this organization
     */
    public int getTotalNumBytes() {

        return orgBytes + descendentsNumBytes;
    }

    public void addChild(Org node) {
        children.add(node);
    }

    public void addUser(UserAttributes user) {
        orgData.getUserList().add(user);
    }

    public boolean hasOrgData() {
        return orgData != null;
    }

    public OrgAttributes getOrgData() {
        return orgData;
    }
    List<Org> getChildOrgs() {
        return children;
    }


}
