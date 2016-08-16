package com.company;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

/**
 * A very simple test suite
 */
public class MainTest {

    @org.junit.Test
    public static void buildTree() throws Exception {

        OrgCollection orgChart;
        List<Org> orgList;
        HashMap<Integer, OrgAttributes> orgs;
        HashMap<Integer, UserAttributes> users;
        PrintWriter pw;

        // use default input file
        users = Main.ReadUserData(Main.userFilename);
        Assert.assertNotNull(users);
        Assert.assertEquals(5, users.size());
        orgs = Main.ReadOrgData(Main.orgFilename);
        Assert.assertNotNull(orgs);
        Assert.assertEquals(5, orgs.size());
        orgChart = new OrgCollection(orgs, users);
        // Assert.assertEquals(5, orgChart.orgHashMap.size());
        // dump org tree to file with usage stats
        pw = new PrintWriter(new FileWriter("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\test1.output"));
        orgChart.FlattenToAscii(orgChart.getRoot(), "", pw);
        pw.close();


        // input file with orphaned orgs and same user file as default
        users = Main.ReadUserData("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\users.txt");
        Assert.assertNotNull(users);
        Assert.assertEquals(5, users.size());
        orgs = Main.ReadOrgData("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\orgs2.txt");
        Assert.assertNotNull(orgs);
        Assert.assertEquals(4, orgs.size());
        orgChart = new OrgCollection(orgs, users);
        // Assert.assertEquals(2, orgChart.orgHashMap.size());
        // dump org tree to file with usage stats
        pw = new PrintWriter(new FileWriter("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\test2.output"));
        orgChart.FlattenToAscii(orgChart.getRoot(), "", pw);
        pw.close();


        // larger input file test
        users = Main.ReadUserData("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\moreusers.txt");
        Assert.assertNotNull(users);
        Assert.assertEquals(30, users.size());
        orgs = Main.ReadOrgData("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\moreorgs.txt");
        Assert.assertNotNull(orgs);
        Assert.assertEquals(20, orgs.size());
        orgChart = new OrgCollection(orgs, users);
        // Assert.assertEquals(20, orgChart.orgHashMap.size());
        // dump org tree to file with usage stats
        pw = new PrintWriter(new FileWriter("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\test3.output"));
        orgChart.FlattenToAscii(orgChart.getRoot(), "", pw);
        pw.close();

    }

}