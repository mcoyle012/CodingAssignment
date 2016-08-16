package com.company;

import org.junit.Assert;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * A very simple test suite
 */
public class MainTest {

    @org.junit.Test
    public void buildTree() throws Exception {

        OrgCollection orgChart;
        HashMap<Integer, OrgAttributes> orgs;
        HashMap<Integer, UserAttributes> users;
        PrintWriter pw;

        // use default input file
        users = Main.readUserData(Main.userFilename);
        Assert.assertNotNull(users);
        Assert.assertEquals(5, users.size());
        orgs = Main.readOrgData(Main.orgFilename);
        Assert.assertNotNull(orgs);
        Assert.assertEquals(0, orgs.size());
        orgChart = new OrgCollection(orgs, users);
        // Assert.assertEquals(5, orgChart.orgHashMap.size());
        // dump org tree to file with usage stats
        pw = new PrintWriter(new FileWriter("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\test1.output"));
        orgChart.flattenToAscii(orgChart.getRoot(), "", pw);
        pw.close();


        // input file with orphaned orgs and same user file as default
        users = Main.readUserData("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\test1users.txt");
        Assert.assertNotNull(users);
        Assert.assertEquals(5, users.size());
        orgs = Main.readOrgData("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\test2orgs.txt");
        Assert.assertNotNull(orgs);
        Assert.assertEquals(4, orgs.size());
        orgChart = new OrgCollection(orgs, users);
        // Assert.assertEquals(2, orgChart.orgHashMap.size());
        // dump org tree to file with usage stats
        pw = new PrintWriter(new FileWriter("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\test2.output"));
        orgChart.flattenToAscii(orgChart.getRoot(), "", pw);
        pw.close();


        // larger input file test
        users = Main.readUserData("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\test3users.txt");
        Assert.assertNotNull(users);
        Assert.assertEquals(30, users.size());
        orgs = Main.readOrgData("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\test3orgs.txt");
        Assert.assertNotNull(orgs);
        Assert.assertEquals(20, orgs.size());
        orgChart = new OrgCollection(orgs, users);
        // Assert.assertEquals(20, orgChart.orgHashMap.size());
        // dump org tree to file with usage stats
        pw = new PrintWriter(new FileWriter("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\test3.output"));
        orgChart.flattenToAscii(orgChart.getRoot(), "", pw);
        pw.close();

    }

}