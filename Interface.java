/**
 * Author: Andrew Kirrane
 * Project 1 COMP 494 Datamining
 * 6/13/2022
 * 
 * This is an implementation of the KNN algorithm to deal with classifying the data from the iris.txt dataset
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class Interface {
    /**
     * Constructor for the user interface class
     * 
     * @param train filename for the data
     * @param s scanner object to take in user inputs
     */
    public Interface(String train, Scanner s) {
        try {
            PrintWriter pw = new PrintWriter("myResult.txt");
            pw.close();
        }
        catch(FileNotFoundException e) {
            System.out.println(e);
        }
        int k = 0;
        System.out.println("Enter k value: ");
        k = Integer.parseInt(s.nextLine());
        System.out.println("Enter the desired percentage for training data (0.0 - 1.0): ");
        double percent = 1 - Double.parseDouble(s.nextLine()); 
        ArrayList<DataSet> trainingSet = loadDataSets(train);
        double numLinesTest = Math.round(trainingSet.size() * percent);
        ArrayList<DataSet> testingSet = new ArrayList<DataSet>();
        Random rand = new Random();

        while (numLinesTest > 0) {
            int num = rand.nextInt(trainingSet.size());
            testingSet.add(trainingSet.get(num));
            trainingSet.remove(num);
            numLinesTest--;
        }

        KNN knn = new KNN(trainingSet, testingSet, k);
        ArrayList<String> classifiedNames = knn.classify();
        int numCorrect = 0;

        for (int i = 0; i < testingSet.size(); i++) {

            if (testingSet.get(i).getClassName().equals(classifiedNames.get(i))) {
                numCorrect = numCorrect + 1;
            }
        }
        try{    
            BufferedWriter fw = new BufferedWriter(new FileWriter("myResult.txt", true));
            double accuracy = (double) numCorrect / testingSet.size() * 100.0;
            fw.write("Number correct: " + numCorrect + "/" + testingSet.size() + ", Accuracy: " + accuracy + "%");   
            fw.close();    
        }
        catch(Exception e){System.out.println(e);}    
        System.out.println("Success...");
    }

    /**
     * Loads file data into data set objects to be iterated through by
     * 
     * @param fileName name of file that user specifies
     * @return the arraylist object to be iterated through
     */
    private ArrayList<DataSet> loadDataSets(String fileName) {
        ArrayList<DataSet> dataSets = new ArrayList<DataSet>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                String[] data = line.split(",");
                String sepalLength = data[0].trim();
                String sepalWidth = data[1].trim();
                String petalLength = data[2].trim();
                String petalWidth = data[3].trim();
                String className = data[4].trim();

                if (sepalLength != null && sepalWidth != null && petalLength != null && petalWidth != null && className != null) {
                    dataSets.add(new DataSet(Float.parseFloat(sepalLength), Float.parseFloat(sepalWidth), Float.parseFloat(petalLength), Float.parseFloat(petalWidth), className));
                }

                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return dataSets;
    }

    /**
     * Main function to run program with
     * 
     * @param args user passes in filename when initializing the program
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("You must include a filename.");
            return;
        }
        String training = args[0];
        System.out.println("Loading data from: " + training);
        Scanner s = new Scanner(System.in);
        while (true) {
            new Interface(training, s);
            System.out.println("Enter y if you want to run the program again: ");
            String answer = s.nextLine();
            if (!answer.equals("y")) {
                break;
            }
        }
        s.close();
    }
}