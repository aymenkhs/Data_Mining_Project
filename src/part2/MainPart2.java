package part2;



import common.Dataset;
import part2.classification.KNN;
import part2.classification.NaiveBayes;
import part2.pretreatment.Discretization;
import part2.pretreatment.Normalization;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainPart2 {

    public static void main(String[] args) throws FileNotFoundException {

        Dataset ds = API.normalization("dataset/seeds_dataset.txt", "minmax");
        ArrayList<String[]> r =  API.discretisation("dataset/seeds_dataset.txt", "minmax", "amplitude", 4);
        KNN knn = API.knn("dataset/seeds_dataset.txt", 20,"minmax", 5, "euclidean");

        NaiveBayes naive = API.naiveBayes("dataset/seeds_dataset.txt", 20,"minmax", "amplitude", 4);
        System.out.println(  );
    }
}
