package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;

/*
 * Represents an organization in the org hierarchy tree, plus its relationship
 * to parent and child
 */
public class Node {
    Organization orgData;
    Node parent;
    ArrayList<Node> children;

    public Node(Organization data) {
        orgData = data;
        children = new ArrayList<Node>();
    }


}
