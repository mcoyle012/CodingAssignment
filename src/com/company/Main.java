package com.company;

import java.io.*;
import java.util.*;

/**
 * Entry point for execution of the homework assignment for Code42
 * interview.  Contains static methods for file input and output,
 * in order to generate data structures needed by the Org and OrgCollection
 * classess called out in the assignment.
 */
class Main {

    private static final String delimiter = "\\s*,\\s*";

    public static String orgFilename = "C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\orgs.txt";
    public static String userFilename = "C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\users.txt";
    public static String outFilename = "C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\output.txt";

    public static HashMap<Integer, Organization> ReadOrgData(String filename) {
        HashMap<Integer, Organization> orgs = null;
        int lineNum = 1;

        try (Scanner s = new Scanner(new BufferedReader(new FileReader(filename))).useDelimiter(delimiter)) {
            orgs = new HashMap<Integer, Organization>();
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
                if (orgs.put(org.Id, org) != null) {
                    System.out.println(String.format("Organization %d already exists, ignoring duplicate definition on line %d in %s", org.Id, lineNum, filename));
                }
                lineNum++;
            }
        } catch (FileNotFoundException fnf) {
        } finally {
            return orgs;
        }
    }


    public static HashMap<Integer, User> ReadUserData(String filename) {
        HashMap<Integer, User> users = null;

        try (Scanner s = new Scanner(new BufferedReader(new FileReader(filename))).useDelimiter(delimiter)) {
            users = new HashMap<Integer, User>();
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
                if (users.put(user.Id, user) != null) {
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
      Program entry point.  Optional user supplied arguments indicate the
      intput files to process.  arg1 = org, arg2 = user.
     */
    public static void main(String[] args) {

        try {
            int argc = 0;

            // use user supplied filenames, if present  t
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
            HashMap<Integer, Organization> orgs;
            HashMap<Integer, User> users;

            // read org file data
            orgs = ReadOrgData(orgFilename);
            // read user file data
            users = ReadUserData(userFilename);
            // consturct tree of orgs and add users to each org
            orgChart = new OrgCollection(orgs, users);

            // dump org tree to file with usage stats
            PrintWriter pw = new PrintWriter(new FileWriter(outFilename));
            orgChart.FlattenToAscii(orgChart.getRoot(), "", pw);
            pw.close();

        } catch (Exception exc) {
            System.out.println(String.format("Program aborting due to exception %s", exc.toString()));
        }
    }
}
