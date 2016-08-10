package com.company;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.io.*;
import java.util.*;

public class Main {

    private static String delimiter = "\\s*,\\s*|\r\n|\t";

    private static HashSet<Organization> ReadOrgData(String filename) {
        HashSet<Organization> orgs = null;
        int lineNum = 1;

        try (Scanner s = new Scanner(new BufferedReader(new FileReader(filename))).useDelimiter(delimiter)) {
            orgs = new HashSet<Organization>();
            while (s.hasNextLine()) {
                Organization org = new Organization();
                try {
                    org.Id = s.nextInt();
                    System.out.println(String.format("org.Id = %d", org.Id));
                } catch (InputMismatchException e) {
                    System.out.println(String.format("Ignoring line %d in %s due to parse error in first field", lineNum, filename));
                    s.nextLine();
                    lineNum++;
                    continue;
                }
                try {
                    org.parentId = s.nextInt();
                    org.hasParent = true;
                    System.out.println(String.format("org.parentId = %d", org.parentId));
                } catch (InputMismatchException e) {
                    org.hasParent = false;
                    System.out.println(String.format("org.parentId = null"));
                    s.next();
                }
                try {
                    assert (s.hasNext());
                    org.name = s.next();
                    System.out.println(String.format("org.name = %s", org.name));
                } catch (InputMismatchException e) {
                    System.out.println(String.format("Ignoring line %d in %s due to parse error in third field", lineNum, filename));
                    s.nextLine();
                    lineNum++;
                    continue;
                }
                if (!orgs.add(org)) {
                    System.out.println(String.format("Organization %d already exists, ignoring duplicate definition on line %d in %s", org.Id, lineNum, filename));
                }
                lineNum++;
            }
        } catch (FileNotFoundException fnf) {
        } finally {
            return orgs;
        }
    }


    public static HashSet<User> ReadUserData(String filename) {
        HashSet<User> users = null;

        try (Scanner s = new Scanner(new BufferedReader(new FileReader(filename))).useDelimiter(delimiter)) {
            users = new HashSet<>();
            int lineNum = 1;

            while (s.hasNextLine()) {
                User user = new User();
                try {
                    user.Id = s.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println(String.format("Ignoring line %d in %s due to parse error in first field", lineNum, filename));
                    s.nextLine();
                    lineNum++;
                    continue;
                }
                try {
                    user.Org = s.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println(String.format("Ignoring line %d in %s due to parse error in first field", lineNum, filename));
                    s.nextLine();
                    lineNum++;
                    continue;
                }
                try {
                    user.numFiles = s.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println(String.format("Ignoring line %d in %s due to parse error in first field", lineNum, filename));
                    s.nextLine();
                    lineNum++;
                    continue;
                }

                try {
                    user.numBytes = s.nextInt();

                } catch (InputMismatchException e) {
                    System.out.println(String.format("Ignoring line %d in %s due to parse error in first field", lineNum, filename));
                    s.nextLine();
                    lineNum++;
                    continue;
                }
                if (!users.add(user)) {
                    System.out.println(String.format("User %d already exists, ignoring duplicate definition on line %d in %s", user.Id, lineNum, filename));
                }
                lineNum++;
                s.nextLine();
            }
        } catch (FileNotFoundException fnf) {
        } finally {
            return users;
        }
    }


    public static OrgCollection BuildTree(HashSet<Organization> orgs, HashSet<User> users) {
        // root node of tree has null orgData.  it exists only to hold real org elements
        // as children
        OrgCollection tree = new OrgCollection(null);
        boolean workRemains = true;
        while (workRemains) {
            workRemains = false;
            for (Iterator<Organization> orgIt = orgs.iterator(); orgIt.hasNext(); ) {
                Organization org = orgIt.next();
                // if parentId is valid
                if (org.hasParent) {
                    Org parent = tree.NodeExists(org.parentId);
                    if (parent != null) {
                        // add to tree as child of parent
                        tree.AddChild(parent, org);
                        // remove from HashSet
                        orgIt.remove();
                    } else {
                        workRemains = true;
                    }
                } else {
                    // has no parent, is child of dummy root node
                    tree.AddChild(tree.root, org);
                    // remove from HashSet
                    orgIt.remove();
                }
            }
        }
        return tree;
    }

    public static void main(String[] args) {
        // write your code here
        try {
            OrgCollection orgChart;
            List<Org> orgList;
            HashSet<Organization> orgs;
            HashSet<User> users;
            System.out.println("Hello World!");
            orgs = ReadOrgData("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\orgs.txt");
            users = ReadUserData("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\users.txt");
            orgChart = BuildTree(orgs, users);
            orgChart.FlattenToAscii(orgChart.root, "");
            orgList = orgChart.getOrgTree(1, true);
            System.out.println(String.format("orgList length %d", orgList.size()));
        } catch (Exception exc) {
            // TODO
        }
    }
}
