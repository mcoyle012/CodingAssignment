package com.company;

import java.util.HashMap;
import java.util.Iterator;
import java.io.*;

/**
 * Implements the tree used to represent the org heirarchy
 * read in from the input org hierarchy data file.
 */
public class Tree {

    public Node root;
    public HashMap<Integer, Node> orgHashMap = new HashMap<Integer, Node>();

    // Construct a tree
    public Tree(Organization data) {
        root = new Node(data);
        // root node has no org data, only its children do
        if (data != null)
            orgHashMap.put(data.Id, root);
    }

    // find a node in the tree by orgId
    public Node NodeExists(int orgId) {
        return orgHashMap.get(orgId);
    }

    // add child node
    public void AddChild(Node parent, Organization data) {
        Node child = new Node(data);
        parent.children.add(child);
        orgHashMap.put(data.Id, child);
    }

    public void FlattenToAscii(Node node, String prefix) {
        // root node is a placeholder (non-org) entity.  Only dump its children
        // which are real orgs
        if (node.orgData != null) {
            System.out.println(String.format("%s +- %s", prefix, node.orgData.name));
            prefix += "  ";
        }

        for (Iterator<Node> children = node.children.iterator(); children.hasNext(); ) {
            Node child = children.next();
            FlattenToAscii(child, prefix);
        }
    }


}
