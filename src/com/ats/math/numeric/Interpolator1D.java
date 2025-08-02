package com.ats.math.numeric;

/**
 * Interface for objects which can interpolate points of a mapping from a
 * one-dimensional space (domain) to another one-dimensional space (range).
 * <P>
 * This interface is implemented by interpolators like linear, log-linear and
 * spline-based interpolators. Objects which require (1 to n) point
 * interpolation for their methods are assigned one of these interpolators.
 *
 */
public interface Interpolator1D  {
    /**
     * Returns the result of the interpolation for a given coordinate.
     * <p>
     * The array arguments are interpreted to as discrete realizations
     * (ordered-pairs) of a mapping f:X -> Y.
     * <p>
     * The x-axis array argument must be strictly sorted (no duplicates)
     * according to the natural ordering of the array type ([index] < [index +
     * 1]).
     *
     * @param <code>x1</code> the x-axis coordinate for which the interpolated
     *        value is required.
     * @param <code>x</code> a subset of X for which the corresponding values
     *        are to be found in y
     * @param <code>y</code> the function values corresponding to x.
     * @return the result of the interpolation at x1.
     */
    public double interpolate(double x1, double[] x, double[] y);
    boolean isThreadSafe();
    /**
     * Returns the result of the interpolation for a given coordinate.
     * <p>
     * Caters specifically for date based clients, where the dates are typically
     * stored as primitive <code>longs</code>.
     * <p>
     * The array arguments are interpreted to as discrete realizations
     * (ordered-pairs) of a mapping f:X -> Y.
     * <p>
     * The x-axis array argument must be strictly sorted (no duplicates)
     * according to the natural ordering of the array type ([index] < [index +
     * 1]).
     *
     * @param <code>x1</code> the x-axis coordinate for which the interpolated
     *        value is required.
     * @param <code>x</code> a subset of X for which the corresponding values
     *        are to be found in y
     * @param <code>y</code> the function values corresponding to x.
     * @return the result of the interpolation at x1.
     */
    public double interpolate(long x1, long[] x, double[] y);

    /**
     * Returns a human-readable description of this interpolator.
     *
     * @return a human-readable description of this interpolator.
     */
    public String getDescription();

    /**
     * Returns true if the interpolator can extrapolate.
     * @return
     */
    public boolean canExtrapolate();

    /**
     *
     * public interface Interpolator1D {
     *     double interpolate(long x, long[] xArray, double[] yArray);
     *     double interpolate(double x, double[] xArray, double[] yArray);
     *     boolean isThreadSafe();
     *     boolean canExtrapolate();
     *     String getDescription();
     * }
     **/

}



