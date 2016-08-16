package com.company;

/**
 * Contains the attributes of a user as defined by the parameters read
 * in from the corresponding input file.
 */
class UserAttributes {

    private final int id;
    private final int org;
    private final int numFiles;
    private final int numBytes;

    public UserAttributes(int uid, int pOrg, int files, int bytes) {
        id = uid;
        org = pOrg;
        numFiles = files;
        numBytes = bytes;
    }

    public int getId() {
        return id;
    }

    public int getOrg() {
        return org;
    }

    public int getNumFiles() {
        return numFiles;
    }

    public int getNumBytes() {
        return numBytes;
    }
}
