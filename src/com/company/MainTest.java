package com.company;

import org.junit.Assert;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * A very simple test suite
 */
public class MainTest {

    @org.junit.Test
    public void buildTree() throws Exception {

        OrgCollection orgChart;
        List<Org> orgList;
        HashSet<Organization> orgs;
        HashSet<User> users;

        // use default input file
        users = Main.ReadUserData(Main.userFilename);
        Assert.assertNotNull(users);
        Assert.assertEquals(5, users.size());
        orgs = Main.ReadOrgData(Main.orgFilename);
        Assert.assertNotNull(orgs);
        Assert.assertEquals(5, orgs.size());
        orgChart = Main.BuildTree(orgs, users);
        Assert.assertEquals(5, orgChart.orgHashMap.size());

        // input file with orphaned orgs and same user file as default
        users = Main.ReadUserData("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\users.txt");
        Assert.assertNotNull(users);
        Assert.assertEquals(5, users.size());
        orgs = Main.ReadOrgData("C:\\Users\\Mike\\IdeaProjects\\CodingAssignment\\src\\com\\company\\orgs2.txt");
        Assert.assertNotNull(orgs);
        Assert.assertEquals(4, orgs.size());
        orgChart = Main.BuildTree(orgs, users);
        Assert.assertEquals(2, orgChart.orgHashMap.size());
        
    }

}