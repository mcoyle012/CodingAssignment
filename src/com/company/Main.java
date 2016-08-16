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

    public static String orgFilename = "C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\orgs.txt";
    public static String userFilename = "C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\users.txt";
    private static String outFilename = "C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\output.txt";
    private static final String delimiter = "\\s*,\\s*";


    public static HashMap<Integer, OrgAttributes> ReadOrgData(String filename) {
        HashMap<Integer, OrgAttributes> orgs = null;
        int lineNum = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            orgs = new HashMap<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(delimiter);
                if (words.length == 3) {
                    OrgAttributes org = new OrgAttributes();
                    org.Id = Integer.parseInt(words[0]);
                    try {
                        org.parentId = Integer.parseInt(words[1]);
                        org.hasParent = true;
                    } catch (Exception e) {
                        org.hasParent = false;
                    }
                    org.name = words[2];
                    if (orgs.put(org.Id, org) != null) {
                        System.out.println(String.format("Duplicate definition for org %d found at line %d in %s, using most recent definition", org.Id, lineNum, filename));
                    }
                } else {
                    System.out.println(String.format("Skipping malformed line line %d in %s due to wrong number of fields", lineNum, filename));
                }
                lineNum++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orgs;
    }

    public static HashMap<Integer, UserAttributes> ReadUserData(String filename) {
        HashMap<Integer, UserAttributes> users = null;
        int lineNum = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            users = new HashMap<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(delimiter);
                if (words.length == 4) {
                    UserAttributes user = new UserAttributes();
                    user.Id = Integer.parseInt(words[0]);
                    user.Org = Integer.parseInt(words[1]);
                    user.numFiles = Integer.parseInt(words[2]);
                    user.numBytes = Integer.parseInt(words[3]);
                    if (users.put(user.Id, user) != null) {
                        System.out.println(String.format("Duplicate definition for org %d found at line %d in %s, using most recent definition", user.Id, lineNum, filename));
                    }
                } else {
                    System.out.println(String.format("Skipping malformed line line %d in %s due to wrong number of fields", lineNum, filename));
                }
                lineNum++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;

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
            if (args.length != 0 && args.length != 3) {
                System.out.println(String.format("Expected three command line arguments (orgFile, userFile, outputFile), but got %d arguments.  Reverting to default filenames.", argc));
            } else if (args.length == 3) {
                orgFilename = args[0];
                userFilename = args[1];
                outFilename = args[2];
            }

            OrgCollection orgChart;
            List<Org> orgList;
            HashMap<Integer, OrgAttributes> orgs;
            HashMap<Integer, UserAttributes> users;

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
