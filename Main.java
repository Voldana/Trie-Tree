package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TTree tTree = new TTree();
        Scanner scanner = new Scanner(System.in);
        int numberOfCommands = scanner.nextInt();
        int command,data,numberOfAnswers = 0;
        int[] answers = new int[numberOfCommands];

        for(int i = 0; i < numberOfCommands; i ++){
            command = scanner.nextInt();
            data = scanner.nextInt();

            if(command == 1){
                tTree.insertValue(Integer.toBinaryString(data));
            }else if (command == 2){
                int xor = tTree.maxExclusiveOr(data);
                answers[numberOfAnswers] = xor;
                numberOfAnswers++;
            }
        }
        for(int i = 0; i < numberOfAnswers; i++){
            System.out.println(answers[i]);
        }
    }
}
class TTreeNode{

    private boolean finished = false;
    private TTreeNode[] nodeChildren = new TTreeNode[2];                       //For this node's children
    private String str = "";

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public TTreeNode[] getNodeChildren() {
        return nodeChildren;
    }

    public void setNodeChildren(TTreeNode[] nodeChildren) {
        this.nodeChildren = nodeChildren;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}

class TTree {

    int maxKeysLength = 0;            //Represents the maximum length of keys
    static TTreeNode rNode = new TTreeNode();


    /*int maxExclusiveOr(int number) {

        TTreeNode tempNode = null;
        String answer = "";
        String binary = Integer.toBinaryString(number);
        int difference = maxKeysLength - binary.length();

        if (difference == 0) {       // both have same size
            answer = binary;
        } else if (difference > 0) {       //Setting 0's for the rest of digits cause we have free space
            for (int i = 0; i < difference; i++) {
                answer += "0";
            }
            answer += binary;          // copying the rest of the string
        } else {                          // cutting the string
            answer = binary.substring(Math.abs(difference));        // cutting LSB's from the string
        }
        int firstOccurrence = answer.indexOf("0");  // looks for the first zero in the string
        if (firstOccurrence == -1) {
            //The string doesn't have a 0 so we can only use 0 for xor
            return number;
        } else {
            String searchFor = answer.substring(firstOccurrence--);
            tempNode = find(searchFor, rNode);
            if (tempNode != null)
                return Integer.parseInt(tempNode.getStr(), 2) ^ number;
            if(tempNode == null)
                return number;
        }
        return number;
    }

    }*/
    void insertValue(String binary) {         //Inserts a new key in the tree

        int binaryLength = binary.length();

        if (binaryLength > maxKeysLength) {
            maxKeysLength = binaryLength;
        }

        int index = 0;
        TTreeNode tempNode = rNode;

        for (int i = 0; i < binaryLength; i++) {
            index =Character.getNumericValue(binary.charAt(i));
            if (tempNode.getNodeChildren()[index] == null) {
                tempNode.getNodeChildren()[index] = new TTreeNode();
                tempNode.getNodeChildren()[index].setStr(tempNode.getStr() + index);
            }
            tempNode = tempNode.getNodeChildren()[index];
        }

        tempNode.setFinished(true);
    }


    TTreeNode find(String string, TTreeNode tempNode) {             // Thanks to another indian youtuber

        if (string.length() == 0 && tempNode.isFinished() == true) {        ////the current node is the end of a key and our string is empty
            return tempNode;
        } else if (string.length() == 0) {                                      //the current node ain't end of a key
            return null;
        }

        int index = Character.getNumericValue(string.charAt(0));
        TTreeNode a = null;
        TTreeNode b = null;

        int direction = index ^ 1;              //shows the direction

        if (tempNode.getNodeChildren()[direction] != null) {
            a = find(string.substring(1), tempNode.getNodeChildren()[direction]);
        }
        if (a != null)
            return a;
        if (tempNode.getNodeChildren()[(direction + 1) % 2] != null) {

            b = find(string.substring(1), tempNode.getNodeChildren()[(direction + 1) % 2]);

        }
        return b;

    }

    int maxExclusiveOr(int n) {

        TTreeNode tempNode = null;
        String binary = Integer.toBinaryString(n);
        String answer = "";
        int difference = maxKeysLength - binary.length();

        if (difference > 0) {
            for (int i = 0; i < maxKeysLength - binary.length(); i++) {
                answer += "0";
            }
            answer = answer.concat(binary);
        } else if (binary.length() > maxKeysLength) {
            answer = binary.substring(binary.length() - maxKeysLength);
        } else {
            answer = binary;
        }

        int i;
        i = answer.indexOf("0");

        if (i == -1) {
            return n;
        }

        if (i != -1) {

            String searchFor = "";
            searchFor = searchFor.concat(answer.substring(i--));
            tempNode = this.find(searchFor, rNode);
        }
        if (tempNode == null)
            return n;
        else if (tempNode != null) {
            return Integer.parseInt(tempNode.getStr(), 2) ^ n;
        }
        return n;
    }
}