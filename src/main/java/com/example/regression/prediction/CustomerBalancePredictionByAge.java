package com.example.regression.prediction;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class CustomerBalancePredictionByAge {

    public static void main(String[] args) {
        try {
            // Load data
            List<String[]> data = loadCSV("src/main/resources/Bank_Data.csv");
            
            // Prepare regression model
            SimpleRegression regression = new SimpleRegression();
            
            // add data points
            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);
                double age = Integer.parseInt(row[1]);  // AGE [Independent variable (X)]
                double balance = Double.parseDouble(row[2]);  // BANK BALANCE [Dependent variable (Y)]
                regression.addData(age, balance);
            }
            
            // model statistics
            System.out.println("=== Model Summary ===");
            System.out.printf("R-squared: %.4f\n", regression.getRSquare());
            System.out.printf("Intercept: %.2f\n", regression.getIntercept());
            System.out.printf("Slope: %.4f\n", regression.getSlope());
            System.out.printf("Standard Error: %.4f\n\n", regression.getRegressionSumSquares());
            
            // predictions for eager customers
            System.out.println("=== Predictions ===");
            predictBalance(regression, 27);
            predictBalance(regression, 32);
            predictBalance(regression, 44);
            predictBalance(regression, 56);
            predictBalance(regression, 62);

            predictBalance(regression, 73);
            
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
    
    private static List<String[]> loadCSV(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            return reader.readAll();
        }
    }
    
    private static void predictBalance(SimpleRegression regression, int age) {
        double balance = regression.predict(age);
        System.out.printf("Predicted Bank Balance for %d year old: $%,.2f\n",
                         age, balance);
    }
}