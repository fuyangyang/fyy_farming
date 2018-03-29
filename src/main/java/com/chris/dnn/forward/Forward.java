package com.chris.dnn.forward;

import java.util.Random;

import Jama.Matrix;

import java.util.Random;

/**
 * 7kw
 *
 * gether volume -> odps
 * odps -> softmax
 *
 *
 * 12 * 12 * 12
 * 12 * map(12) * (12 * map(12))  using: embed
 * rt: 0.25ms
 * Created by chris on 17/8/28.
 */
public class Forward {

    private static final int LAYER_0 = 300;
    private static final int LAYER_1 = 200;
    private static final int LAYER_2 = 80;
    private static final int LAYER_3 = 2;

    private static final double EPSILON = 0.2;//shared 除0保护

    //start
    private static final double[] LAMADA = new double[]{0.52, 0.42, 0.32};
    private static final double[] BETA = new double[]{0.13, 0.23, 0.33};
    private static final double[] ALPHA = new double[]{200, 80};


    private static final double[] MU = new double[]{0.32, 0.32, 0.32};
    private static final double[] DELTA = new double[]{0.14, 0.14, 0.14};
    //stop

    private static final long ITERATOR = 1000;

    private static final Random random = new Random();

    public static void main(String[] args) {
        //input
        Matrix input = genPresuMatrix(1, LAYER_0);

        //layer_1
        Matrix w1 = genPresuMatrix(LAYER_0, LAYER_1);
        Matrix b1 = genPresuMatrix(1, LAYER_1);

        //layer_2
        Matrix w2 = genPresuMatrix(LAYER_1, LAYER_2);
        Matrix b2 = genPresuMatrix(1, LAYER_2);

        //layer_3
        Matrix w3 = genPresuMatrix(LAYER_2, LAYER_3);
        Matrix b3 = genPresuMatrix(1, LAYER_3);

        long start = System.currentTimeMillis();

        for (int i = 0; i < ITERATOR; i++) {
            predict(input, w1, b1, w2, b2, w3, b3);
        }

        long stop = System.currentTimeMillis();
        long gap = stop - start;
        System.out.println("cost:" + gap + " millseconds");
    }

    public static void predict(Matrix input, Matrix w1, Matrix b1, Matrix w2, Matrix b2, Matrix w3, Matrix b3) {
        Matrix after_layer1 = forwardOneLayer(input, w1, b1, 0);
        Matrix after_layer2 = forwardOneLayer(after_layer1, w2, b2, 1);
        Matrix after_layer3 = forwardOneLayer(after_layer2, w3, b3, 2);
        softmax(after_layer3);
    }

    public static Matrix forwardOneLayer(Matrix x0, Matrix w, Matrix b, int layer) {

        //output dimension
        int w_col_num = w.getColumnDimension();

        //w * x + b
        Matrix x1 = x0.times(w).plus(b);
        for (int i = 0; i < w_col_num; i++) {
            x1.set(0, i, (x1.get(0, i) - MU[layer]) / Math.sqrt(DELTA[layer] * DELTA[layer] + EPSILON));
        }
        Matrix x2 = x1;

        //r * x2 + b
        double[][] beta = new double[1][w_col_num];
        for (int i = 0; i < w_col_num; i++) {
            beta[0][i] = BETA[layer];
        }
        Matrix x3 = x2.times(LAMADA[layer]).plus(new Matrix(beta));

        //prelu: max(0,x3)  + a * min(0, x3)
        if (layer < 2) {
            for (int i = 0; i < w_col_num; i++) {
                x3.set(0, i, Math.max(0, x3.get(0, i)) + ALPHA[layer] * Math.min(0, x3.get(0, i)));
            }
        }

//        for(int i = 0; i < w_col_num; i++) {
//            System.out.println(" " + x3.get(0, i));
//        }
//        System.out.println();

        return x3;
    }

    public static double[] softmax(Matrix m) {
        double out1 = m.get(0, 0);
        double out2 = m.get(0, 1);

        double e1 = Math.exp(out1);
        double e2 = Math.exp(out2);
        double sum = e1 + e2;

        return new double[]{e1 / sum, e2 / sum};
    }

    public static Matrix genPresuMatrix(int row, int col) {
        double[][] x = new double[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                x[i][j] = getOneDouble();
            }
        }
        return new Matrix(x);
    }

    public static double getOneDouble() {
        int val = random.nextInt(100);
        return (val + 1) / 101.0;
    }

    /**
     * @param m
     * @return 0:平均值；1:标准差
     */
    public static double[] StandardDiviation(Matrix m) {
        double[] rowPackedCopy = m.getRowPackedCopy();
        double[] avgStddiv = StandardDiviation(rowPackedCopy);
        return new double[]{avgStddiv[0], avgStddiv[1]};
    }

    /**
     * @param x
     * @return 0:平均值；1:标准差
     */
    public static double[] StandardDiviation(double[] x) {
        int m = x.length;
        double sum = 0;
        for (int i = 0; i < m; i++) {//求和
            sum += x[i];
        }
        double dAve = sum / m;//求平均值
        double dVar = 0;
        for (int i = 0; i < m; i++) {//求方差
            dVar += (x[i] - dAve) * (x[i] - dAve);
        }
        return new double[]{dAve, Math.sqrt(dVar / m + EPSILON)};
    }

}
