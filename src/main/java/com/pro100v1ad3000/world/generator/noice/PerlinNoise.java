package main.java.com.pro100v1ad3000.world.generator.noice;

import java.util.Random;

public class PerlinNoise {
    private final int[] permutation;

    public PerlinNoise(long seed) {
        permutation = new int[512];
        Random random = new Random(seed);
        for (int i = 0; i < 256; i++) {
            permutation[i] = i;
        }
        for (int i = 0; i < 256; i++) {
            int j = random.nextInt(256 - i) + i;
            int temp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = temp;
            permutation[i + 256] = permutation[i];
        }
    }

    public double noise(double x, double y) {
        int xi = (int) x & 255;
        int yi = (int) y & 255;
        double xf = x - (int) x;
        double yf = y - (int) y;
        double u = fade(xf);
        double v = fade(yf);

        int a = permutation[permutation[xi] + yi];
        int b = permutation[permutation[xi + 1] + yi];
        int c = permutation[permutation[xi] + yi + 1];
        int d = permutation[permutation[xi + 1] + yi + 1];

        double x1 = lerp(grad(a, xf, yf), grad(b, xf - 1, yf), u);
        double x2 = lerp(grad(c, xf, yf - 1), grad(d, xf - 1, yf - 1), u);
        return lerp(x1, x2, v);
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y) {
        int h = hash & 15;
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : h == 12 || h == 14 ? x : 0;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}
