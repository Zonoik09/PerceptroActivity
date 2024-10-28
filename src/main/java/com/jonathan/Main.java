package com.jonathan;

import java.util.ArrayList;
import java.util.List;

public class Main {
    // Lista de diferentes EPOCHS
    private static List<Integer> epochsList = List.of(1, 2, 5, 10, 20,50,75,100,200);


    private double[] weights;
    private double bias;
    private double learningRate = 0.1;

    // Constructor para inicializar el perceptrón
    public Main(int inputSize) {
        weights = new double[inputSize];
        bias = 0.0;
        // Inicialización de los pesos aleatorios
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random() - 0.5;
        }
    }

    // Función de activación
    private int activate(double sum) {
        return sum >= 0 ? 1 : 0; // Salida 1 para positivo, 0 para negativo
    }

    // Función para predecir la salida
    private int predict(int[] inputs) {
        double sum = bias;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }
        return activate(sum);
    }

    // Entrenar el perceptrón
    public void train(int[][] inputData, int[] labels, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < inputData.length; i++) {
                int prediction = predict(inputData[i]);
                int error = labels[i] - prediction;

                // Ajustar pesos y bias
                for (int j = 0; j < weights.length; j++) {
                    weights[j] += learningRate * error * inputData[i][j];
                }
                bias += learningRate * error;
            }
        }
    }

    // Comprobar la precisión del perceptrón
    public double testAccuracy(int[][] inputData, int[] labels) {
        int correct = 0;
        for (int i = 0; i < inputData.length; i++) {
            int prediction = predict(inputData[i]);
            if (prediction == labels[i]) {
                correct++;
            }

            // Imprimir entrada y resultado
            // Descomenta la siguiente sección si quieres ver los detalles de las predicciones
            /*
            System.out.print("Entrada: ");
            for (int bit : inputData[i]) {
                System.out.print(bit);
            }
            System.out.println(" -> Resultado: " + (prediction == 0 ? "No Línea" : "Línea") +
                    " (Esperado: " + (labels[i] == 0 ? "No Línea" : "Línea") + ")");
            */
        }
        return (correct / (double) inputData.length) * 100.0;
    }

    // Generar todas las combinaciones posibles de matrices de 3x3
    public static List<int[]> generateAllMatrices() {
        List<int[]> matrices = new ArrayList<>();

        // Hay 2^9 combinaciones posibles
        for (int i = 0; i < 512; i++) {
            int[] matrix = new int[9];

            // Convertir el índice `i` a una combinación binaria de 9 bits
            String binary = String.format("%9s", Integer.toBinaryString(i)).replace(' ', '0');

            // Rellenar el array con los bits correspondientes
            for (int j = 0; j < 9; j++) {
                matrix[j] = binary.charAt(j) - '0';
            }

            matrices.add(matrix);
        }

        return matrices;
    }

    // Comprobar si una matriz tiene una línea diagonal
    private static boolean isDiagonal(int[][] matrix) {
        return (matrix[0][0] == 1 && matrix[1][1] == 1 && matrix[2][2] == 1) ||
                (matrix[0][2] == 1 && matrix[1][1] == 1 && matrix[2][0] == 1);
    }

    // Comprobar si una matriz tiene una línea vertical
    private static boolean isVertical(int[][] matrix) {
        return (matrix[0][0] == 1 && matrix[1][0] == 1 && matrix[2][0] == 1) ||
                (matrix[0][1] == 1 && matrix[1][1] == 1 && matrix[2][1] == 1) ||
                (matrix[0][2] == 1 && matrix[1][2] == 1 && matrix[2][2] == 1);
    }

    // Comprobar si una matriz tiene una línea horizontal
    private static boolean isHorizontal(int[][] matrix) {
        return (matrix[0][0] == 1 && matrix[0][1] == 1 && matrix[0][2] == 1) ||
                (matrix[1][0] == 1 && matrix[1][1] == 1 && matrix[1][2] == 1) ||
                (matrix[2][0] == 1 && matrix[2][1] == 1 && matrix[2][2] == 1);
    }

    // Función principal
    public static void main(String[] args) {
        // Generar todas las combinaciones posibles de matrices
        List<int[]> allMatrices = generateAllMatrices();

        // Etiquetas para cada perceptrón
        int[] diagonalLabels = new int[allMatrices.size()];
        int[] verticalLabels = new int[allMatrices.size()];
        int[] horizontalLabels = new int[allMatrices.size()];

        // Definir las etiquetas según las líneas
        for (int i = 0; i < allMatrices.size(); i++) {
            int[] matrix = allMatrices.get(i);

            // Convertir la matriz a 3x3 para facilitar la lectura
            int[][] matrix3x3 = {
                    {matrix[0], matrix[1], matrix[2]},
                    {matrix[3], matrix[4], matrix[5]},
                    {matrix[6], matrix[7], matrix[8]}
            };

            // Comprobar si tiene una línea
            diagonalLabels[i] = (isDiagonal(matrix3x3) ? 1 : 0);
            verticalLabels[i] = (isVertical(matrix3x3) ? 1 : 0);
            horizontalLabels[i] = (isHorizontal(matrix3x3) ? 1 : 0);
        }

        // Crear tres perceptrones
        Main perceptronDiagonal = new Main(9);
        Main perceptronVertical = new Main(9);
        Main perceptronHorizontal = new Main(9);

        // Entrenar y probar perceptrones para cada valor de EPOCHS
        for (int epochs : epochsList) {
            // Entrenar perceptrones
            perceptronDiagonal.train(allMatrices.toArray(new int[0][]), diagonalLabels, epochs);
            perceptronVertical.train(allMatrices.toArray(new int[0][]), verticalLabels, epochs);
            perceptronHorizontal.train(allMatrices.toArray(new int[0][]), horizontalLabels, epochs);

            // Probar precisión
            double accuracyDiagonal = perceptronDiagonal.testAccuracy(allMatrices.toArray(new int[0][]), diagonalLabels);
            double accuracyVertical = perceptronVertical.testAccuracy(allMatrices.toArray(new int[0][]), verticalLabels);
            double accuracyHorizontal = perceptronHorizontal.testAccuracy(allMatrices.toArray(new int[0][]), horizontalLabels);

            double totalAccuracy = (accuracyDiagonal + accuracyVertical + accuracyHorizontal) / 3;

            System.out.println("EPOCHS: " + epochs);
            System.out.println("Perceptrón Diagonal - Precisión: " + accuracyDiagonal + "%");
            System.out.println("Perceptrón Vertical - Precisión: " + accuracyVertical + "%");
            System.out.println("Perceptrón Horizontal - Precisión: " + accuracyHorizontal + "%");
            System.out.println("Precisión Total - Promedio: " + totalAccuracy + "%");
            System.out.println("------------------------------");
        }
    }
}
