import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class KNN {
    final String class1 = "Iris-setosa";
    final String class2 = "Iris-versicolor";
    final String class3 = "Iris-virginica";

    private double rangeSepalLength, rangeSepalWidth, rangePetalLength, rangePetalWidth;
    private ArrayList<DataSet> trainingSet, testingSet;
    private int k;

    /**
     * Constructor for the KNN algorithm object
     * 
     * @param trainingSet arraylist of data from input file to be used for training
     * @param testingSet arraylist of data from input file to be used for testing
     * @param k k value for to be used in the algorithm
     */
    public KNN(ArrayList<DataSet> trainingSet, ArrayList<DataSet> testingSet, int k) {
        if(trainingSet.size() == 0 || testingSet.size() == 0) {
            System.out.println("At least one set is empty.");
            return;
        }
        this.trainingSet = trainingSet;
        this.testingSet = testingSet;
        this.k = k;
        calcRange();
    }

    /**
     * Classifies objects in the testing set and saves them to the results file using
     * 
     * @return a string arraylist of the classnames identified
     */
    public ArrayList<String> classify() {
        ArrayList<String> classNames = new ArrayList<String>();
        
        for (int i = 0; i < testingSet.size(); i++) {
            ArrayList<Distance> distances = new ArrayList<Distance>();
            DataSet n = testingSet.get(i);

            for (int j = 0; j < trainingSet.size(); j++) {
                DataSet n2 = trainingSet.get(j);
                double distance = calcDistance(n, n2);
                String className = trainingSet.get(j).getClassName();
                distances.add(new Distance(distance, className));
            }
            Collections.sort(distances, new  distanceComparator());
            String classifiedName = calcCount(distances, k);
            try {
                BufferedWriter fw = new BufferedWriter(new FileWriter("myResult.txt", true));
                fw.write("Test Object: " + (i + 1) + " is classified as: " + classifiedName + ", Actual result is: " + testingSet.get(i).getClassName() + "\n");
                fw.close();
            }
            catch(Exception e){System.out.println(e);} 
            classNames.add(classifiedName);
        }
        return classNames;
    }

    /**
     * Tracks count of the classified object throughout the iteration process
     * 
     * @param distances list of distances between points
     * @param k k value to be used in the algorithm
     * @return the guessed name of the classified object
     */
    public String calcCount(ArrayList<Distance> distances, int k) {
        String classifiedName;
        ArrayList<ClassCount> classCount = new ArrayList<ClassCount>();
        classCount.add(new ClassCount("Iris-setosa"));
        classCount.add(new ClassCount("Iris-versicolor"));
        classCount.add(new ClassCount("Iris-virginica"));

        for (int i = 0; i < k; i++) {

            if (distances.get(i).getClassName().equals(class1)) {
                classCount.get(0).increase();
            }
            else if (distances.get(i).getClassName().equals(class2)) {
                classCount.get(1).increase();
            }
            else {
                classCount.get(2).increase();
            }
        }
        Collections.sort(classCount, new classCountComparator());

        if (classCount.get(0).getCount() == classCount.get(1).getCount()) {
            System.out.println("Found the same number of classes. Updating the k value to: " + (k + 1));
            classifiedName = calcCount(distances, (k + 1));
        }
        else {
            classifiedName = classCount.get(0).getClassName();
        }
        return classifiedName;
    }

    /**
     * Calculate the distance between points based on the calculated range
     * 
     * @param a fist point to be compared
     * @param b second point to be compared
     * @return the distance between points
     */
    public double calcDistance(DataSet a, DataSet b) {
        double distance;
        distance = Math.pow((a.getSepalLength() - b.getSepalLength()), 2) / Math.pow(rangeSepalLength, 2) + 
                   Math.pow((a.getSepalWidth() - b.getSepalWidth()), 2) / Math.pow(rangeSepalWidth, 2) + 
                   Math.pow((a.getPetalLength() - b.getPetalLength()), 2) / Math.pow(rangePetalLength, 2) + 
                   Math.pow((a.getPetalWidth() - b.getPetalWidth()), 2) / Math.pow(rangePetalWidth, 2);
        distance = Math.sqrt(distance);
        return distance;
    }

    /**
     * Calculate the range between the data points min and max
     */
    public void calcRange() {
        double rangeSepalLengthMax = Integer.MIN_VALUE;
        double rangeSepalWidthMax = Integer.MIN_VALUE;
        double rangePetalLengthMax = Integer.MIN_VALUE;
        double rangePetalWidthMax = Integer.MIN_VALUE;
        double rangeSepalLengthMin = Integer.MIN_VALUE;
        double rangeSepalWidthMin = Integer.MIN_VALUE;
        double rangePetalLengthMin = Integer.MIN_VALUE;
        double rangePetalWidthMin = Integer.MAX_VALUE;

        for (int i = 0; i < trainingSet.size(); i++) {
            if (trainingSet.get(i).getSepalLength() > rangeSepalLengthMax) rangeSepalLengthMax = trainingSet.get(i).getSepalLength();
            if (trainingSet.get(i).getSepalLength() < rangeSepalLengthMin) rangeSepalLengthMin = trainingSet.get(i).getSepalLength();
            if (trainingSet.get(i).getSepalWidth() > rangeSepalWidthMax) rangeSepalWidthMax = trainingSet.get(i).getSepalWidth();
            if (trainingSet.get(i).getSepalWidth() < rangeSepalWidthMin) rangeSepalWidthMin = trainingSet.get(i).getSepalWidth();
            if (trainingSet.get(i).getPetalLength() > rangePetalLengthMax) rangePetalLengthMax = trainingSet.get(i).getPetalLength();
            if (trainingSet.get(i).getPetalLength() < rangePetalLengthMin) rangePetalLengthMin = trainingSet.get(i).getPetalLength();
            if (trainingSet.get(i).getPetalWidth() > rangePetalWidthMax) rangePetalWidthMax = trainingSet.get(i).getPetalWidth();
            if (trainingSet.get(i).getPetalWidth() < rangePetalWidthMin) rangePetalWidthMin = trainingSet.get(i).getPetalWidth();
        }
        rangeSepalLength = rangeSepalLengthMax - rangeSepalLengthMin;
        rangeSepalWidth = rangeSepalWidthMax - rangeSepalWidthMin;
        rangePetalLength = rangePetalLengthMax - rangePetalLengthMin;
        rangePetalWidth = rangePetalWidthMax - rangePetalWidthMin;
    }

    /**
     * Comparator for the distances between two points
     */
    class distanceComparator implements Comparator<Distance> {
        public int compare(Distance a, Distance b) {

            if (a.getDistance() < b.getDistance()) {
                return -1;
            }
            else if (a.getDistance() > b.getDistance()) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

    /**
     * Comparator between the class names of two points
     */
    class classCountComparator implements Comparator<ClassCount> {
        public int compare(ClassCount a, ClassCount b) {
            if (a.getCount() < b.getCount()) {
                return 1;
            }
            else if (a.getCount() > b.getCount()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
}