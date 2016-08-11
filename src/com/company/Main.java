package com.company;

import java.io.*;
import java.util.*;

/**
 * Entry point for execution of the homework assignment for Code42
 * interview.  Contains static methods for file input and output,
 * in order to generate data structures needed by the Org and OrgCollection
 * classess called out in the assignment.
 */
public class Main {

    private static String delimiter = "\\s*,\\s*|\r\n|\t";

    public static String orgFilename = "C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\orgs.txt";
    public static String userFilename = "C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\users.txt";
    public static String outFilename = "C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\output.txt";

    public static HashSet<Organization> ReadOrgData(String filename) {
        HashSet<Organization> orgs = null;
        int lineNum = 1;

        try (Scanner s = new Scanner(new BufferedReader(new FileReader(filename))).useDelimiter(delimiter)) {
            orgs = new HashSet<Organization>();
            while (s.hasNextLine()) {
                Organization org = new Organization();
                try {
                    org.Id = s.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println(String.format("Ignoring line %d in %s due to parse error in first field", lineNum, filename));
                    s.nextLine();
                    lineNum++;
                    continue;
                }
                try {
                    org.parentId = s.nextInt();
                    org.hasParent = true;
                } catch (InputMismatchException e) {
                    org.hasParent = false;
                    s.next();
                }
                try {
                    assert (s.hasNext());
                    org.name = s.next();
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


    /*
     Given an unordered list of orgs and users, construct an OrgCollection that represents
     that org.
     TODO: Should be refactored as a constructor method on OrgCollection.
     */
    public static OrgCollection BuildTree(HashSet<Organization> orgs, HashSet<User> users) {
        // root node of tree has null orgData.  it exists only to hold real org elements
        // as children, not any org data itself
        OrgCollection tree = new OrgCollection(null);

        // Pure brute force; more performant ways exist.  Call this the MVP edition.
        // Revisit this once all the functional requirements are met and some unit
        // tests are in place, then refactor for performance.
        boolean workRemains = true;
        while (workRemains) {
            boolean workDone = false;
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
                        workDone = true;
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
            // if I added no nodes to the tree, then the input isn't a tree
            // there should be at least one node to add to the tree in every pass
            // unless there are orphaned nodes, which will never get added
            if (workRemains && !workDone) {
                System.out.println("Input org tree is badly formed.  Ignoring orphaned orgs in input file.");
                break;
            }
        }

        // Accumulate user data to corresponding org
        for (Iterator<User> userIt = users.iterator(); userIt.hasNext(); ) {
            User user = userIt.next();
            Org parent = tree.NodeExists(user.Org);
            if (parent != null) {
                parent.orgData.userList.add(user);
                parent.orgBytes += user.numBytes;
                parent.orgFiles += user.numFiles;
            }
        }
        tree.setOrgTreeStats(tree.root);
        return tree;
    }

    /*
      Program entry point.  Optional user supplied arguments indicate the
      intput files to process.  arg1 = org, arg2 = user.
     */
    public static void main(String[] args) {

        try {
            String orgFilename = null;
            String userFilename = null;
            String outFilename = null;
            int argc = 0;

            // use user supplied filenames, if present
            for (String s : args) {
                if (argc++ == 0) {
                    orgFilename = s;
                } else if (argc++ == 1) {
                    userFilename = s;
                } else if (argc++ == 2) {
                    outFilename = s;
                    break;
                }
            }
            if (args.length != 0 && argc != 3) {
                System.out.println(String.format("Expected three command line arguments (orgFile, userFile, outputFile), but got %d.  Reverting to default filenames.", argc));
            }

            OrgCollection orgChart;
            List<Org> orgList;
            HashSet<Organization> orgs;
            HashSet<User> users;

            // read org file data
            orgs = ReadOrgData(orgFilename);
            // read user file data
            users = ReadUserData(userFilename);
            // consturct tree of orgs and add users to each org
            orgChart = BuildTree(orgs, users);

            // dump org tree to file with usage stats
            orgChart.FlattenToAscii(orgChart.root, "", new PrintWriter(new FileWriter(outFilename)));
        } catch (Exception exc) {
            System.out.println(String.format("Program aborting due to exception %s", exc.toString()));
        }
    }
}
