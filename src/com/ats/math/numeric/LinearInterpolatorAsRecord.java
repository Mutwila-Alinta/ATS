package com.ats.math.numeric;

import java.util.Arrays;
import java.util.Date;

    /**
     * A record-based implementation of a linear 1D interpolator.
     * Immutable, stateless, and thread-safe.
     */
    public record LinearInterpolatorAsRecord() implements Interpolator1D {

        private static final String DESCRIPTION = "linear_interpolator";

        @Override
        public boolean isThreadSafe() {
            return true;
        }

        @Override
        public boolean canExtrapolate() {
            return false;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }

        @Override
        public String toString() {
            return DESCRIPTION;
        }

        /**
         * Interpolates between two 2D points (x1, y1) and (x2, y2) at x.
         * If x1 == x2, returns y1.
         */
        public static double interpolate(double x1, double y1, double x2, double y2, double x) {
            if (x1 == x2) return y1;
            return y1 + ((y2 - y1) * (x - x1)) / (x2 - x1);
        }

        @Override
        public double interpolate(long xVal, long[] x, double[] y) {
            if (xVal <= x[0]) return y[0];

            int index = Arrays.binarySearch(x, xVal);
            if (index >= 0) return y[index];

            int insertionPoint = -index - 1;
            if (insertionPoint >= x.length) {
                throw new IllegalArgumentException(
                        "Cannot extrapolate beyond end-point: x = " + x[x.length - 1] +
                                ", date = " + new Date(x[x.length - 1])
                );
            }

            int i = insertionPoint - 1;
            return interpolate(x[i], y[i], x[i + 1], y[i + 1], xVal);
        }

        @Override
        public double interpolate(double xVal, double[] x, double[] y) {
            if (xVal <= x[0]) return y[0];

            int index = Arrays.binarySearch(x, xVal);
            if (index >= 0) return y[index];

            int insertionPoint = -index - 1;
            if (insertionPoint >= x.length) {
                throw new IllegalArgumentException(
                        "Cannot extrapolate beyond end-point: x = " + x[x.length - 1]
                );
            }

            int i = insertionPoint - 1;
            return interpolate(x[i], y[i], x[i + 1], y[i + 1], xVal);
        }

        //todo Usage
        /**
         * Interpolator1D interpolator = new LinearInterpolator();
         * double result = interpolator.interpolate(3.5, new double[]{1, 4, 7}, new double[]{10, 40, 70});
         * */
    }

