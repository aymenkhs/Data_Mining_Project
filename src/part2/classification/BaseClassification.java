package part2.classification;

import common.Dataset;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class BaseClassification {

    protected Dataset dataset, train_data, test_data;
    protected HashMap<ArrayList<Double>, Integer> predictedData;

    protected ArrayList<ArrayList<Integer>> confusion_matrix;
    private HashMap<Integer, int[][]> confusionMatrixClasses;

    protected double executionTime;

    public BaseClassification(Dataset dataset, int testSize) {
        this.dataset = dataset;
        this.split_data(testSize);
    }

    public HashMap<ArrayList<Double>, Integer> getPredictedData() {
        return predictedData;
    }

    public ArrayList<ArrayList<Integer>> getConfusion_matrix() {
        return confusion_matrix;
    }

    public String getPrintedConfusion_matrix() {
        String value = "";
        for (ArrayList<Integer> line : this.confusion_matrix){
            for (int column : line){
                value += column + " ";
            }
            value += "\n";
        }
        return value;
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public int[][] getConfusionMatrixClass(int idClass) {
        return confusionMatrixClasses.get(idClass);
    }

    public String getPrintedConfusionMatrixClass(int idClass) {
        int[][] matrix = confusionMatrixClasses.get(idClass);
        String value = "class " + idClass + "\n" + matrix[0][0] + " " + matrix[0][1] + "\n" + matrix[1][0] + " " + matrix[1][1] + "\n";
        return value;
    }

    public double getRecall(int idClass){
        int[][] matrix = getConfusionMatrixClass(idClass);
        return (double) matrix[0][0] / (double) (matrix[0][0] + matrix[0][1]);

    }

    public double getMeanRecall(){
        return (getRecall(1) + getRecall(2) + getRecall(3)) / 3;
    }

    public double getPrecision(int idClass){
        int[][] matrix = getConfusionMatrixClass(idClass);
        return (double) matrix[0][0] / (double) (matrix[0][0] + matrix[1][0]);
    }

    public double getMeanPrecision(){
        return (getPrecision(1) + getPrecision(2) + getPrecision(3)) / 3;
    }

    public double getFScore(int idClass){
        double precision = getPrecision(idClass), recall = getRecall(idClass);
        return (2*precision*recall)/(recall + precision);
    }

    public double getMeanFScore(){
        return (getFScore(1) + getFScore(2) + getFScore(3)) / 3;
    }

    public double getAccuracy(int idClass){
        int[][] matrix = getConfusionMatrixClass(idClass);
        return (double) (matrix[0][0] + matrix[1][1]) / (double) (matrix[0][0] + matrix[0][1] + matrix[1][0] + matrix[1][1]);
    }

    public double getMeanAccuracy(){
        return (getAccuracy(1) + getAccuracy(2) + getAccuracy(3)) / 3;
    }

    public double getSensitivity(int idClass){
        int[][] matrix = getConfusionMatrixClass(idClass);
        return (double) matrix[0][0] / (double) (matrix[0][0] + matrix[0][1]);
    }

    public double getMeanSensitivity(){
        return (getSensitivity(1) + getSensitivity(2) + getSensitivity(3)) / 3;
    }

    public double getSpecificity(int idClass){
        int[][] matrix = getConfusionMatrixClass(idClass);
        return (double) matrix[1][1] / (double) (matrix[1][0] + matrix[1][1]);
    }

    public double getMeanSpecificity(){
        return (getSpecificity(1) + getSpecificity(2) + getSpecificity(3)) / 3;
    }

    public ArrayList<ArrayList<Integer>> confusionMatrix(){
        this.confusion_matrix = new ArrayList<>();
        int predictedClass, realClass;

        //Initialize confusion_matrix
        for (int i=0; i<3; i++) {
            this.confusion_matrix.add(new ArrayList<>());
            for (int j=0; j<3; j++) {
                this.confusion_matrix.get(i).add(0);
            }
        }

        for (ArrayList<Double> list : this.predictedData.keySet()) {

            predictedClass = this.predictedData.get(list);
            realClass = list.get(list.size() - 1).intValue();
            int value = this.confusion_matrix.get(realClass-1).get(predictedClass-1);
            value++;
            this.confusion_matrix.get(realClass-1).set(predictedClass-1, value);
        }

        return this.confusion_matrix;
    }

    public void confusionMatrixClasses(){
        confusionMatrixClasses = new HashMap<>(  );
        confusionMatrixClasses.put(1, confusionMatrixForClass(1));
        confusionMatrixClasses.put(2, confusionMatrixForClass(2));
        confusionMatrixClasses.put(3, confusionMatrixForClass(3));
    }

    private int[][] confusionMatrixForClass(int idClass){
        int[][] confusionMatrixClass = new int[2][2];

        int predictedClass, realClass;

        for (ArrayList<Double> list : this.predictedData.keySet()) {

            predictedClass = this.predictedData.get(list);
            realClass = list.get(list.size() - 1).intValue();

            if (predictedClass == idClass && realClass == idClass){
                confusionMatrixClass[0][0]++;
            } else if (predictedClass != idClass && realClass == idClass){
                confusionMatrixClass[0][1]++;
            } else if (predictedClass == idClass && realClass != idClass){
                confusionMatrixClass[1][0]++;
            } else {
                confusionMatrixClass[1][1]++;
            }
        }

        return confusionMatrixClass;
    }

    public abstract void test();

    public void execute(){
        double y = System.nanoTime();
        test();
        double x = System.nanoTime();
        this.executionTime = x - y;

        this.confusionMatrix();
        this.confusionMatrixClasses();
        System.out.println(  );
    }

    // this methos splits the data between train and test data
    protected void split_data(int testSize){
        ArrayList<ArrayList<Double>> train = new ArrayList<>();
        ArrayList<ArrayList<Double>> test = new ArrayList<>();
        double instanceClass;

        // nbTestx represent the number of
        int nbTest1 = testSize, nbTest2 = testSize, nbTest3 = testSize;
        for (int i=0; i<this.dataset.nbInstances(); i++){
            instanceClass = this.dataset.getClass( i );
            if (instanceClass == 1){
                if (nbTest1 > 0){
                    test.add(this.dataset.getInstanceArrayList(i));
                    nbTest1--;
                } else {
                    train.add(this.dataset.getInstanceArrayList(i));
                }
            } else if (instanceClass == 2){
                if (nbTest2 > 0){
                    test.add(this.dataset.getInstanceArrayList(i));
                    nbTest2--;
                } else {
                    train.add(this.dataset.getInstanceArrayList(i));
                }
            } else if (instanceClass == 3){
                if (nbTest3 > 0){
                    test.add(this.dataset.getInstanceArrayList(i));
                    nbTest3--;
                } else {
                    train.add(this.dataset.getInstanceArrayList(i));
                }
            }
        }
        this.train_data = new Dataset(train);
        this.test_data = new Dataset(test);
    }
}
