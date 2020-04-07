import org.knowm.xchart.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Driver {

    public static final int NUM_DATA_POINTS = 4;
    public static final int NUM_ITERATIONS = 5000;

    public static double[] generateAxisForK() {
        double[] arr = new double[10];
        for(int i = 1; i < 10; i++) {
            arr[i] = i;
        }

        return arr;
    }

    public static void runTrial(XYChart numberOfNodesChart,
                                XYChart numberOfEdgesChart,
                                XYChart degreesOfNeighborsChart,
                                XYChart distributionOfDegreesChart,
                                double p) {
        Graph graph = new Graph();

        double[] numberOfNodes = new double[NUM_DATA_POINTS];
        double[] numberOfEdges = new double[NUM_DATA_POINTS];
        double[] degreesOfNeighbors = generateAxisForK();
        double[] chanceOfBeingChosenForDeletion = new double[10];
        Arrays.fill(chanceOfBeingChosenForDeletion, 0);
        double[] time = new double[NUM_DATA_POINTS];

        int dataPoint = 0;

        for(int i = 1; i <= NUM_ITERATIONS; i++) {
            Random random = new Random();
            double r = random.nextDouble();
            // System.out.println(i);

            if(r < p) {
                graph.birth(i);
            } else {
                if(graph.size() == 1)
                    continue;
                graph.death(chanceOfBeingChosenForDeletion);
            }

            if(i % (NUM_ITERATIONS / (NUM_DATA_POINTS )) == 0) {
                numberOfNodes[dataPoint] = graph.size();
                numberOfEdges[dataPoint] = graph.countEdges();
                time[dataPoint] = i;

                dataPoint++;
                System.out.println(i + "th iteration: " + graph.size() + " nodes");
            }
        }

        double total = 0;
        for(double a : chanceOfBeingChosenForDeletion)
            total += a;

        System.out.println(total);
        for(int i = 1; i < 10; i++) {
            chanceOfBeingChosenForDeletion[i] = chanceOfBeingChosenForDeletion[i]/total;
        }

        numberOfNodesChart.addSeries("p = " + p, time, numberOfNodes);
        numberOfEdgesChart.addSeries("p = " + p, time, numberOfEdges);
        degreesOfNeighborsChart.addSeries("p = " + p, degreesOfNeighbors, chanceOfBeingChosenForDeletion);

        double[] distributionOfDegrees = graph.getDistributionOfDegrees();

        total = 0;
        for(double a : distributionOfDegrees)
            total += a;

        double temp = 0;

        for(int i = 0; i < 8; i++) {
            temp += distributionOfDegrees[i];
            distributionOfDegrees[i] = temp;
        }

        for(int i = 0; i < 8; i++) {
            distributionOfDegrees[i] = distributionOfDegrees[i] / total;
        }

        double[] k = new double[8];

        for(int i = 1; i < 8; i++) {
            if(distributionOfDegrees[i] > 0)
                distributionOfDegrees[i] = Math.log10(distributionOfDegrees[i]);
            k[i] = Math.log10(degreesOfNeighbors[i]);
        }

        for (int i = 0; i < 4; i++) {
            temp = distributionOfDegrees[i];
            distributionOfDegrees[i] = distributionOfDegrees[8 - i - 1];
            distributionOfDegrees[8 - i - 1] = temp;
        }

        distributionOfDegrees[7] = distributionOfDegrees[6];

        if(p == 0.75)
            distributionOfDegreesChart.addSeries("p = " + p, k, distributionOfDegrees);
    }

    public static void main(String[] args) throws IOException {
        XYChart numberOfNodesChart = new XYChartBuilder()
                .width(600)
                .height(500)
                .title("Number of Nodes over Time")
                .xAxisTitle("Time")
                .yAxisTitle("Number of Nodes")
                .build();

        XYChart numberOfEdgesChart = new XYChartBuilder()
                .width(600)
                .height(500)
                .title("Number of Edges over Time")
                .xAxisTitle("Time")
                .yAxisTitle("Number of Edges")
                .build();

        XYChart degreesOfNeighborsChart = new XYChartBuilder()
                .width(600)
                .height(500)
                .title("Degrees of Neighbors of Node Chosen For Deletion")
                .xAxisTitle("k")
                .yAxisTitle("Degrees of Neighbors")
                .build();

        XYChart distributionOfDegreesChart = new XYChartBuilder()
                .width(600)
                .height(500)
                .title("Distribution of Degrees")
                .xAxisTitle("k")
                .yAxisTitle("Distribution of Degrees")
                .build();

        runTrial(numberOfNodesChart, numberOfEdgesChart,degreesOfNeighborsChart,distributionOfDegreesChart,0.6);
        runTrial(numberOfNodesChart, numberOfEdgesChart,degreesOfNeighborsChart,distributionOfDegreesChart, 0.75);
        runTrial(numberOfNodesChart, numberOfEdgesChart,degreesOfNeighborsChart, distributionOfDegreesChart,0.9);

        numberOfNodesChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        numberOfEdgesChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        degreesOfNeighborsChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        distributionOfDegreesChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        BitmapEncoder.saveBitmap(numberOfNodesChart, "./numNodesChart", BitmapEncoder.BitmapFormat.PNG);
        BitmapEncoder.saveBitmap(numberOfEdgesChart, "./numEdgesChart", BitmapEncoder.BitmapFormat.PNG);
        BitmapEncoder.saveBitmap(degreesOfNeighborsChart, "./degreesOfNeighborsChart", BitmapEncoder.BitmapFormat.PNG);
        BitmapEncoder.saveBitmap(distributionOfDegreesChart, "./distributionOfDegreesChart", BitmapEncoder.BitmapFormat.PNG);

    }
}
