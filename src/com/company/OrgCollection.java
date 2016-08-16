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
                if (org.getHasParent()) {
                    Org parent = nodeExists(org.getParentId());
                    if (parent != null) {
                        // add to tree as child of parent
                        addChild(parent, org);
                        // remove from HashMap
                        orgIter.remove();
                        workDone = true;
                    }
                } else {
                    workDone = true;
                    // has no parent, is child of dummy root node
                    addChild(root, org);
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
            Org parent = nodeExists(user.getOrg());
            if (parent != null) {
                parent.addUser(user);
                parent.orgBytes += user.getNumBytes();
                parent.orgFiles += user.getNumFiles();
            }
        }
        setOrgTreeStats(root);
    }

    // find a node in the tree by orgId
    private Org nodeExists(int orgId) {
        return orgHashMap.get(orgId);
    }

    // add child node
    private void addChild(Org parent, OrgAttributes data) {
        Org child = new Org(data);
        if (data.getHasParent()) {
            parent.addChild(child);
            child.parent = parent;
        } else {
            if (root == null) {
                root = new Org(null);
            }
            root.addChild(child);
            child.parent = root;
        }
        orgHashMap.put(data.getId(), child);
    }

    public void flattenToAscii(Org node, String prefix, PrintWriter out) {
        if (node != null) {
            // root node is a placeholder (non-org) entity.  Only dump its children
            // which are real orgs
            if (node.hasOrgData()) {
                OrgAttributes orgData = node.getOrgData();
                out.println(String.format("%s +- %d, %d, %d, %d", prefix, orgData.getId(), node.getTotalNumUsers(), node.getTotalNumFiles(), node.getTotalNumBytes()));
                prefix += "  ";
            }

            for (Org child : node.getChildOrgs()) {
                flattenToAscii(child, prefix, out);
            }
        }
    }

    public List<Org> getOrgTree(int orgId, boolean inclusive) {
        ArrayList<Org> orgList = null;
        Org org = nodeExists(orgId);
        if (org != null) {
            orgList = new ArrayList<>();
            return flattenToArray(org, orgList, inclusive);
        }
        return orgList;
    }

    /*
       Do this once for the whole tree
     */
    private void setOrgTreeStats(Org node) {

        if (node != null) {
            // will terminate when we hit leaf nodes
            for (Org child : node.getChildOrgs()) {
                setOrgTreeStats(child);
            }
            if (node.parent != null) {
                node.parent.descendentsNumBytes += node.orgBytes;
                node.parent.descendentsNumFiles += node.orgFiles;
                node.parent.descendentsNumUsers += node.getChildOrgs().size();
            }
        }
    }


    private List<Org> flattenToArray(Org node, List<Org> array, boolean inclusive) {
        if (node != root && inclusive) {
            array.add(node);
        }
        for (Org child : node.getChildOrgs()) {
            flattenToArray(child, array, true);
        }
        return array;
    }


}
