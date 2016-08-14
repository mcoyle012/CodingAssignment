package com.company;

import java.io.*;
import java.util.*;

/**
 * Implements the tree used to represent the org heirarchy
 * read in from the input org hierarchy data file.
 */
public class OrgCollection {

    public Org root;
    public HashMap<Integer, Org> orgHashMap = new HashMap<Integer, Org>();

    // Construct a tree
    public OrgCollection(Organization data) {
        root = new Org(data);
        // root node has no org data, only its children do
        if (data != null)
            orgHashMap.put(data.Id, root);
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
