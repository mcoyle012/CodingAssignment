package com.company;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.ArrayList;
import java.util.List;

/*
 * Represents an organization in the org hierarchy tree, plus its relationship
 * to parent and child
 */
public class Org {
    Organization orgData;
    Org parent;
    List<Org> children;

    public Org(Organization data) {
        orgData = data;
        children = new ArrayList<>();
    }

    public int getTotalNumUsers() {
        return 0;
    }

    public int getTotalNumFiles() {
        return 0;
    }

    public int getTotalNumBytes() {
        return 0;
    }

    List<Org> getChildOrgs() {
        return children;
    }


}
