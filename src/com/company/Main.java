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

    public static final String orgFilename = "C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\orgs.txt";
    public static final String userFilename = "C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\users.txt";
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
                    int orgId, parentId;
                    boolean hasParent;
                    orgId = Integer.parseInt(words[0]);
                    try {
                        parentId = Integer.parseInt(words[1]);
                        hasParent = true;
                    } catch (Exception e) {
                        hasParent = false;
                        parentId = -1;
                    }
                    OrgAttributes org = new OrgAttributes(orgId, parentId, words[2], hasParent);
                    if (orgs.put(org.getId(), org) != null) {
                        System.out.println(String.format("Duplicate definition for org %d found at line %d in %s, using most recent definition", org.getId(), lineNum, filename));
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
                    int[] attrs = new int[4];
                    int idx = 0;
                    for (String s : words) {
                        attrs[idx] = Integer.parseInt(words[idx]);
                        idx++;
                    }
                    UserAttributes user = new UserAttributes(attrs[0], attrs[1], attrs[2], attrs[3]);
                    if (users.put(user.getId(), user) != null) {
                        System.out.println(String.format("Duplicate definition for org %d found at line %d in %s, using most recent definition", user.getId(), lineNum, filename));
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

            OrgCollection orgChart;
            HashMap<Integer, OrgAttributes> orgs;
            HashMap<Integer, UserAttributes> users;

            // read org file data
            orgs = ReadOrgData(orgFilename);
            // read user file data
            users = ReadUserData(userFilename);
            // consturct tree of orgs and add users to each org
            orgChart = new OrgCollection(orgs, users);

            // dump org tree to file with usage stats
            String outFilename = "C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\output.txt";
            PrintWriter pw = new PrintWriter(new FileWriter(outFilename));
            orgChart.FlattenToAscii(orgChart.getRoot(), "", pw);
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
