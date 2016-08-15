package com.company;

import java.io.*;
import java.util.*;

/**
 * Implements the tree used to represent the org heirarchy
 * read in from the input org hierarchy data file.
 */
public class OrgCollection {

    private Org root;

    public Org getRoot() {
        return root;
    }

    public HashMap<Integer, Org> orgHashMap = new HashMap<Integer, Org>();  // i created this on read; now is a duplicate

    /*
     Given an unordered list of orgs and users, construct an OrgCollection that represents
     that org.
     TODO: Should be refactored as a constructor method on OrgCollection.
     */
    public OrgCollection(HashMap<Integer, Organization> orgs, HashMap<Integer, User> users) {

        // Pure brute force; more performant ways exist.  Call this the MVP edition.
        // Revisit this once all the functional requirements are met and some unit
        // tests are in place, then refactor for performance.
        while (orgs.size() != 0) {
            boolean workDone = false;

            // Iterating over values only
            for (Organization org : orgs.values()) {
                // if parentId is valid
                if (org.hasParent) {
                    Org parent = NodeExists(org.parentId);
                    if (parent != null) {
                        // add to tree as child of parent
                        AddChild(parent, org);
                        // remove from HashMap
                        orgs.remove(org.Id);
                        workDone = true;
                    }
                } else {
                    workDone = true;
                    // has no parent, is child of dummy root node
                    AddChild(root, org);
                    // remove from HashSet
                    orgs.remove(org.Id);
                }
            }

            // if I added no nodes to the tree, then the input isn't a tree
            // there should be at least one node to add to the tree in every pass
            // unless there are orphaned nodes, which will never get added
            if ((orgs.size() != 0) && !workDone) {
                System.out.println("Input org tree is badly formed.  Ignoring orphaned orgs in input file.");
                break;
            }

        }

        // Accumulate user data to corresponding org
        for (User user : users.values()) {
            Org parent = NodeExists(user.Org);
            if (parent != null) {
                parent.addUser(user);
                parent.orgBytes += user.numBytes;
                parent.orgFiles += user.numFiles;
            }
        }
        setOrgTreeStats(root);
    }

    // find a node in the tree by orgId
    public Org NodeExists(int orgId) {
        return orgHashMap.get(orgId);
    }

    // add child node
    public void AddChild(Org parent, Organization data) {
        Org child = new Org(data);
        parent.addChild(child);
        child.parent = parent;
        orgHashMap.put(data.Id, child);
    }

    public void FlattenToAscii(Org node, String prefix, PrintWriter out) {
        // root node is a placeholder (non-org) entity.  Only dump its children
        // which are real orgs
        if (node.hasOrgData()) {
            Organization orgData = node.getOrgData();
            out.println(String.format("%s +- %d, %d, %d, %d", prefix, orgData.Id, node.getTotalNumUsers(), node.getTotalNumFiles(), node.getTotalNumBytes()));
            prefix += "  ";
        }


        for (Iterator<Org> children = node.getChildOrgs().iterator(); children.hasNext(); ) {
            Org child = children.next();
            FlattenToAscii(child, prefix, out);
        }
    }

    public List<Org> getOrgTree(int orgId, boolean inclusive) {
        ArrayList<Org> orgList = null;
        Org org = NodeExists(orgId);
        if (org != null) {
            orgList = new ArrayList<Org>();
            return FlattenToArray(org, orgList, inclusive);
        }
        return orgList;
    }

    /*
       Do this once for the whole tree
     */
    public void setOrgTreeStats(Org node) {
        Iterator<Org> children = node.getChildOrgs().iterator();

        // will terminate when we hit leaf nodes
        while (children.hasNext()) {
            Org child = children.next();
            setOrgTreeStats(child);
        }
        if (node.parent != null && node != null) {
            node.parent.descendentsNumBytes += node.orgBytes;
            node.parent.descendentsNumFiles += node.orgFiles;
            node.parent.descendentsNumUsers += node.getChildOrgs().size();
        }
    }


    private List<Org> FlattenToArray(Org node, List<Org> array, boolean inclusive) {
        if (node != root && inclusive) {
            array.add(node);
        }
        for (Iterator<Org> children = node.getChildOrgs().iterator(); children.hasNext(); ) {
            Org child = children.next();
            FlattenToArray(child, array, true);
        }
        return array;
    }


}
