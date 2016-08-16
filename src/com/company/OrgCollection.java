package com.company;

import java.io.*;
import java.util.*;

/**
 * Implements the tree used to represent the org heirarchy
 * read in from the input org hierarchy data file.
 */
class OrgCollection {

    private Org root;

    public Org getRoot() {
        return root;
    }

    private final HashMap<Integer, Org> orgHashMap = new HashMap<>();  // i created this on read; now is a duplicate

    /*
     Given an unordered list of orgs and users, construct an OrgCollection that represents
     that org.
     TODO: Should be refactored as a constructor method on OrgCollection.
     */
    public OrgCollection(HashMap<Integer, OrgAttributes> orgs, HashMap<Integer, UserAttributes> users) {

        // Pure brute force; more performant ways exist.  Call this the MVP edition.
        // Revisit this once all the functional requirements are met and some unit
        // tests are in place, then refactor for performance.
        while (orgs.size() != 0) {
            boolean workDone = false;

            for (Iterator<OrgAttributes> orgIter = orgs.values().iterator(); orgIter.hasNext(); ) {
                // if parentId is valid
                OrgAttributes org = orgIter.next();
                if (org.hasParent) {
                    Org parent = NodeExists(org.parentId);
                    if (parent != null) {
                        // add to tree as child of parent
                        AddChild(parent, org);
                        // remove from HashMap
                        orgIter.remove();
                        workDone = true;
                    }
                } else {
                    workDone = true;
                    // has no parent, is child of dummy root node
                    AddChild(root, org);
                    // remove from HashSet
                    orgIter.remove();
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
        for (UserAttributes user : users.values()) {
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
    private Org NodeExists(int orgId) {
        return orgHashMap.get(orgId);
    }

    // add child node
    private void AddChild(Org parent, OrgAttributes data) {
        Org child = new Org(data);
        if (data.hasParent) {
            parent.addChild(child);
            child.parent = parent;
        } else {
            if (root == null) {
                root = new Org(null);
            }
            root.addChild(child);
            child.parent = root;
        }
        orgHashMap.put(data.Id, child);
    }

    public void FlattenToAscii(Org node, String prefix, PrintWriter out) {
        // root node is a placeholder (non-org) entity.  Only dump its children
        // which are real orgs
        if (node.hasOrgData()) {
            OrgAttributes orgData = node.getOrgData();
            out.println(String.format("%s +- %d, %d, %d, %d", prefix, orgData.Id, node.getTotalNumUsers(), node.getTotalNumFiles(), node.getTotalNumBytes()));
            prefix += "  ";
        }


        for (Org child : node.getChildOrgs()) {
            FlattenToAscii(child, prefix, out);
        }
    }

    public List<Org> getOrgTree(int orgId, boolean inclusive) {
        ArrayList<Org> orgList = null;
        Org org = NodeExists(orgId);
        if (org != null) {
            orgList = new ArrayList<>();
            return FlattenToArray(org, orgList, inclusive);
        }
        return orgList;
    }

    /*
       Do this once for the whole tree
     */
    private void setOrgTreeStats(Org node) {

        // will terminate when we hit leaf nodes
        for (Org child : node.getChildOrgs()) {
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
        for (Org child : node.getChildOrgs()) {
            FlattenToArray(child, array, true);
        }
        return array;
    }


}
