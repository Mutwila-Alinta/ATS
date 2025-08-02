package com.ats.pricing.marketData;


import com.ats.math.numeric.Interpolator1D;
import com.ats.pricing.foundation.BusinessCenter;
import com.ats.pricing.operator.ScenarioOperation;
import com.ats.pricing.tools.BusinessDayConvention;
import com.ats.pricing.tools.ContingentDateInterval;
import com.ats.pricing.tools.DateInterval;
import com.ats.pricing.tools.DayCountConvention;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Base class for all interpolated one-dimensional date-based surfaces.
 * <p>
 * TODO - add remove (date) and remove(dateInterval) methods
 *
*/
public abstract class DateBasedSurface1D
        implements Cloneable, ScenarioOperation<DateBasedSurface1D> {

    /**
     * This boundary type indicates that the supplied boundary date (constructor
     * arg) be defined as the anchor-date (curve's start date). The end date is
     * then implicitly defined by the last date defined in the supplied date
     * array. A left-bounded curve will throw an exception if queried for values
     * before the boundary date (anchor-date), but delegate all other exception
     * handling responsibility to the interpolator.
     */
    public static final int LEFT_BOUNDARY = 0;

    /**
     * This boundary type indicates that the supplied boundary date (constructor
     * arg) be defined as the curve's end date, after which no values are
     * defined. The anchor date is then implicitly defined by the first date
     * defined in the supplied date array. A right-bounded curve will throw an
     * exception if queried for values before the anchor-date OR after the
     * boundary date (end-date), but delegate all other exception handling
     * responsibility to the interpolator.
     */
    public static final int RIGHT_BOUNDARY = 1;

    /**
     * The boundary type of this curve.
     */
    private int boundaryType;

    /**
     * The date range spanned by this curve. It's closure is guaranteed by this
     * class. The start date of this interval defines the anchor date.
     */
    private DateInterval dateInterval;

    /**
     * The internal date storage array.
     */
    private long[] xArray;

    /**
     * The human readable name of the (date based) x-dimension .
     */
    private String xName;

    /**
     * The internal value storage array.
     */
    private double[] valueArray;

    /**
     * The human readable name of the values-dimension .
     */
    private String valuesName;

    /**
     * The interpolator
     */
    private Interpolator1D interpolator;

    /**
     * The daycount convention characterizing this curve.
     */
    private DayCountConvention dayCountConvention;

    private Currency currency;

    /**
     * Creates a <code>DateBasedSurface1D</code>.
     *
     * @param boundaryDate the boundary date which (depending on the supplied
     *        boundary type), is either the anchor-date or the end-date.
     * @param boundaryType type indicating if the supplied boundary-date is the
     *        anchor-date (LEFT) or end-date (RIGHT).
     * @param xName the human-readable name of the x-dimension (date-based
     *        dimension)
     * @param valuesName the human-readable name of the values-dimension
     * @param values a map of dates to values, where the latter are supplied as
     *        <code>Double</code> instances.
     * @param interpolator the interpolator used to interpolate between
     *        explicitly defined node dates.
     * @param dayCountConvention the daycount convention characterizing this
     *        curve.
     * @throws IllegalArgumentException if any of the supplied dates fall before
     *         the defined anchor date, or if any nulls are contained in the
     *         supplied value map.
     */
    protected DateBasedSurface1D(LocalDate boundaryDate, int boundaryType, String xName, String valuesName,
                                 Map<LocalDate, Double> values, Interpolator1D interpolator, DayCountConvention dayCountConvention,
                                 Currency currency) {
        super();
        initialize(boundaryDate, boundaryType, values);
        this.xName = xName;
        this.valuesName = valuesName;
        this.interpolator = interpolator;
        this.dayCountConvention = dayCountConvention;
        this.currency = currency;
    }

    /**
     * Creates a <code>DateBasedSurface1D</code>.
     *
     * @param boundaryDate the boundary date which (depending on the supplied
     *        boundary type), is either the anchor-date or the end-date.
     * @param boundaryType type indicating if the supplied boundary-date is the
     *        anchor-date (LEFT) or end-date (RIGHT).
     * @param xValues the dates for this curve that must be supplied in strictly
     *        ascending order.
     * @param values the values as <code>doubles</code> that correspond to the
     *        supplied dates.
     * @param interpolator the interpolator used to interpolate between
     *        explicitly defined node dates.
     * @param dayCountConvention the daycount convention characterizing this
     *        curve.
     * @param xName the human-readable name of the x-dimension (date-based
     *        dimension)
     * @param valuesName the human-readable name of the values-dimension
     * @throws IllegalArgumentException if any of the supplied dates fall before
     *         the defined anchor date; if any nulls are contained in the
     *         supplied date array; or if the date array is not sorted in
     *         ascending order.
     */
    protected DateBasedSurface1D(LocalDate boundaryDate, int boundaryType, String xName, LocalDate[] xValues, String valuesName,
                                 double[] values, Interpolator1D interpolator, DayCountConvention dayCountConvention, Currency currency) {
        initialize(boundaryDate, boundaryType, xValues, values);
        this.xName = xName;
        this.valuesName = valuesName;
        this.interpolator = interpolator;
        this.dayCountConvention = dayCountConvention;
        this.currency = currency;
    }

    /**
     * Creates a <code>DateBasedSurface1D</code>.
     *
     * @param boundaryDate the boundary date which (depending on the supplied
     *        boundary type), is either the anchor-date or the end-date.
     * @param boundaryType type indicating if the supplied boundary-date is the
     *        anchor-date (LEFT) or end-date (RIGHT).
     * @param xValues the dates for this curve that must be supplied in strictly
     *        ascending order.
     * @param values the values as <code>doubles</code> that correspond to the
     *        supplied dates.
     * @param interpolator the interpolator used to interpolate between
     *        explicitly defined node dates.
     * @param dayCountConvention the daycount convention characterizing this
     *        curve.
     * @throws IllegalArgumentException if any of the supplied dates fall before
     *         the defined anchor date or if the date array is not sorted in
     *         ascending order.
     */
    protected DateBasedSurface1D(long boundaryDate, int boundaryType, String xName, long[] xValues, String valuesName,
                                 double[] values, Interpolator1D interpolator, DayCountConvention dayCountConvention, Currency currency) {
        initialize(boundaryDate, boundaryType, xValues, values);
        this.xName = xName;
        this.valuesName = valuesName;
        this.interpolator = interpolator;
        this.dayCountConvention = dayCountConvention;
        this.currency = currency;
    }

    /**
     * Creates a <code>DateBasedSurface1D</code>.
     *
     * @param dateEntries the dates and corresponding values for this curve.
     * @param dateRangeLabel the human-readable name of the x-dimension
     *        (date-based dimension)
     * @param valueRangeLabel the human-readable name of the values-dimension
     * @param interpolator the interpolator used to interpolate between
     *        explicitly defined node dates.
     * @param dayCountConvention the day count convention characterising this
     *        curve.
     */
    protected DateBasedSurface1D(SortedMap<Date, Double> dateEntries, String dateRangeLabel, String valueRangeLabel,
                                 Interpolator1D interpolator, DayCountConvention dayCountConvention, Currency currency) {

        xArray = new long[dateEntries.keySet().size()];
        valueArray = new double[xArray.length];
        int counter = 0;
        for (Date date : dateEntries.keySet()) {
            xArray[counter] = date.getTime();
            valueArray[counter++] = dateEntries.get(date);
        }

        dateInterval = new DateInterval(Instant.ofEpochMilli(xArray[0])
                .atZone(ZoneId.systemDefault())
                .toLocalDate(), true, Instant.ofEpochMilli(xArray[xArray.length - 1])
                .atZone(ZoneId.systemDefault())
                .toLocalDate(), true);
        this.boundaryType = LEFT_BOUNDARY;
        this.xName = dateRangeLabel;
        this.valuesName = valueRangeLabel;
        this.interpolator = interpolator;
        this.dayCountConvention = dayCountConvention;
        this.currency = currency;
    }

    /**
     * Private default constructor.
     */
    @SuppressWarnings("unused")
    private DateBasedSurface1D() {
    }

    /**
     * Returns the valuesName.
     *
     * @return the valuesName.
     */
    public final String getValuesName() {
        return valuesName;
    }

    /**
     * Returns the xName.
     *
     * @return the xName.
     */
    public final String getXName() {
        return xName;
    }

    /**
     * Returns the interpolated value for the supplied date.
     *
     * @throws IllegalArgumentException if the supplied date falls outside the
     *         range covered by this curve.
     * @throws NullPointerException if the supplied date argument is null
     */
    public final Object getValue(Date date) {
        return getValue(date.getTime());
    }

    /**
     * Returns the interpolated value for the supplied date.
     *
     * @throws IllegalArgumentException if the supplied date falls outside the
     *         range covered by this curve.
     */
    public final Object getValue(long date) {
        double value = getDoubleValue(date);
        return createValue(date, value);
    }

    /**
     * Returns the interpolated value for the supplied date as a
     * <code>double</code>.
     *
     * @throws IllegalArgumentException if the supplied date falls outside the
     *         range covered by this curve.
     * @throws NullPointerException if the supplied date argument is null
     */
    public final double getDoubleValue(Date date) {
        return getDoubleValue(date.getTime());
    }

    /**
     * Returns the interpolated value for the supplied date as a
     * <code>double</code>.
     *
     * @throws IllegalArgumentException if the supplied date falls outside the
     *         range covered by this curve.
     */
    public final double getDoubleValue(long time) {
        long anchorTime = getAnchorTime();
        if (time < anchorTime) {
            throw new IllegalArgumentException(
                    "In the curve " + getSpecificCurveType() + " with reference " + getCurveReference()
                            + ": Argument date [" + new Date(time) + "] must be on or after " + new Date(anchorTime));
        }
        if (boundaryType == RIGHT_BOUNDARY) {
            long endTime = dateInterval.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            if (time > endTime) {
                throw new IllegalArgumentException(
                        "In the curve " + getSpecificCurveType() + " with reference " + getCurveReference()
                                + ": Argument date " + new Date(time) + " must be on or before " + new Date(endTime));
            }
        }

        double value = interpolator.interpolate(time, xArray, valueArray);
        return value;
    }

    /**
     * Returns the value for the supplied dates.
     *
     * @throws UnsupportedOperationException if the implementing class does not
     *         cater for this operation
     * @throws IllegalArgumentException if the supplied dates fall outside the
     *         range covered by this curve.
     * @throws NullPointerException if the supplied date arguments are null
     */
    protected abstract Object getValue(Date date1, Date date2);

    /**
     * Returns the value for the supplied dates as a <code>double</code>.
     *
     * @throws UnsupportedOperationException if the implementing class does not
     *         cater for this operation
     * @throws IllegalArgumentException if the supplied dates fall outside the
     *         range covered by this curve.
     * @throws NullPointerException if the supplied date arguments are null
     */
    protected abstract double getDoubleValue(Date date1, Date date2);

    /**
     * Sets the intrinsic value for the given date, and returns the resultant
     * curve.
     * <p>
     * In other words, this method takes a potentially interpolated value and
     * makes it an explicit point on the curve.
     *
     * @throws UnsupportedOperationException if the implementing class does not
     *         cater for this operation
     * @throws IllegalArgumentException if the supplied date falls before the
     *         anchor date of the curve.
     * @throws NullPointerException if the supplied date argument is null
     */
    public DateBasedSurface1D setValue(Date date) {
        return setValue(date.getTime());
    }

    /**
     * Sets the intrinsic value for the given long date, and returns the
     * resultant curve.
     * <p>
     * In other words, this method takes a potentially interpolated value and
     * makes it an explicit point on the curve.
     *
     * @throws IllegalArgumentException if the supplied date falls before the
     *         anchor date of the curve.
     */
    public DateBasedSurface1D setValue(long time) throws IllegalArgumentException {
        double value = getDoubleValue(time);
        return setValue(time, value);
    }

    /**
     * Sets the supplied value for the given date, and returns the resultant
     * curve.
     *
     * @throws IllegalArgumentException if the supplied date falls before the
     *         anchor date of the curve.
     * @throws NullPointerException if the supplied date argument is null
     */
    public DateBasedSurface1D setValue(Date date, double value) {
        long time = date.getTime();
        return setValue(time, value);
    }

    /**
     * Sets the supplied value for the given (long) date, and returns the
     * resultant curve.
     *
     * @throws UnsupportedOperationException if the implementing class does not
     *         cater for this operation
     * @throws IllegalArgumentException if the supplied date falls before the
     *         anchor date of the curve.
     */
    public DateBasedSurface1D setValue(long time, double value) {
        DateBasedSurface1D copy = (DateBasedSurface1D) clone();
        long firstTime = xArray[0];
        long anchorTime = getAnchorTime();
        if (time < anchorTime) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument date must be on or after " + new Date(anchorTime));
        }
        if (boundaryType == RIGHT_BOUNDARY) {
            long endTime = dateInterval.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            if (time > endTime) {
                throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                        + getCurveReference() + ": Argument date must be on or before " + new Date(endTime));
            }
        }
        if (time < firstTime) // before first date
        {
            copy.xArray = new long[xArray.length + 1];
            copy.xArray[0] = time;
            System.arraycopy(xArray, 0, copy.xArray, 1, xArray.length);
            copy.valueArray = new double[copy.xArray.length];
            copy.valueArray[0] = value;
            System.arraycopy(valueArray, 0, copy.valueArray, 1, valueArray.length);
            return copy;
        } else {
            int index = Arrays.binarySearch(xArray, time);
            if (index >= 0) // date already exists
            {
                copy.valueArray = new double[valueArray.length];
                System.arraycopy(valueArray, 0, copy.valueArray, 0, valueArray.length);
                copy.valueArray[index] = value;
            } else {
                int absIndex = -index;
                if (absIndex > xArray.length) // date after last date
                {
                    copy.xArray = new long[xArray.length + 1];
                    copy.xArray[xArray.length] = time;
                    System.arraycopy(xArray, 0, copy.xArray, 0, xArray.length);
                    copy.valueArray = new double[copy.xArray.length];
                    copy.valueArray[valueArray.length] = value;
                    System.arraycopy(valueArray, 0, copy.valueArray, 0, valueArray.length);
                } else { // date falls in curve range
                    absIndex = absIndex - 2;
                    copy.xArray = new long[xArray.length + 1];
                    copy.xArray[absIndex + 1] = time;
                    System.arraycopy(xArray, 0, copy.xArray, 0, absIndex + 1);
                    System.arraycopy(xArray, absIndex + 1, copy.xArray, absIndex + 2, xArray.length - (absIndex + 1));
                    copy.valueArray = new double[copy.xArray.length];
                    copy.valueArray[absIndex + 1] = value;
                    System.arraycopy(valueArray, 0, copy.valueArray, 0, absIndex + 1);
                    System.arraycopy(valueArray, absIndex + 1, copy.valueArray, absIndex + 2,
                            valueArray.length - (absIndex + 1));
                }
            }
        }
        return copy;
    }

    public final DateBasedSurface1D bucketShift(ContingentDateInterval dateInterval, double shift, boolean isRelative,
                                                boolean isNoWeightings) {
        if (isNoWeightings) {
            if (isRelative) {
                return relativeNoWeightingBucketShift(dateInterval.getDateInterval(), shift);
            } else {
                return absoluteNoWeightingBucketShift(dateInterval.getDateInterval(), shift);
            }
        } else if (isRelative) {
            return relativeBucketShift(dateInterval, shift);
        } else {
            return absoluteBucketShift(dateInterval, shift);
        }
    }

    public final DateBasedSurface1D bucketShift(ContingentDateInterval dateInterval, double shift, boolean isRelative) {
        if (isRelative) {
            return bucketShift(dateInterval, shift, true, false);
        } else {
            return bucketShift(dateInterval, shift, false, true);
        }
    }

    /**
     * Returns a new curve whose values have been shifted by an absolute or
     * relative amount.
     * <p>
     * A basis-point shift equates to <code>0.0001</code> being supplied.
     *
     * @return a new curve whose values have been shifted by an absolute or
     *         relative amount.
     */
    public final DateBasedSurface1D shift(double shift, boolean isRelative) {
        if (isRelative) {
            return relativeShift(shift);
        } else {
            return absoluteShift(shift);
        }
    }

    public final DateBasedSurface1D multiplicationShift(double shift) {
        DateBasedSurface1D copy = (DateBasedSurface1D) clone();
        if (shift != 0.0) {
            copy.valueArray = new double[valueArray.length];
            for (int i = 0; i < valueArray.length; i++) {
                copy.valueArray[i] = multiplicationShift(xArray[i], valueArray[i], shift);
            }
        }
        return copy;
    }

    /**
     * Tilts the curve in a relative or absolute fashion at the supplied fulcrum
     * through a range of -pi/2 to pi/2.
     */
    public final DateBasedSurface1D tilt(LocalDate fulcrum, double degrees, double shiftProportion, boolean isRelative) {
        if (degrees < -45 || degrees > 45) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument degrees must be in range [-45`, 45`]");
        }
        if (!dateInterval.contains(fulcrum)) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument fulcrum out of " + "range [found=" + fulcrum + ", required="
                    + dateInterval + "]");
        }
        if (isRelative) {
            return relativeTilt(fulcrum, degrees, shiftProportion);
        } else {
            return absoluteTilt(fulcrum, degrees, shiftProportion);
        }
    }

    /**
     * Tilts the curve absolutely at the supplied fulcrum through a range of
     * -45` to 45`.
     */
    protected final DateBasedSurface1D absoluteTilt(LocalDate fulcrum, double degrees, double shiftProportion) {
        DateBasedSurface1D copy = (DateBasedSurface1D) clone();
        if (degrees != 0.0 && shiftProportion != 0.0) {
            double radians = Math.toRadians(degrees);
            double radianShift = radians / degrees * shiftProportion;
            long time = fulcrum.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long length = dateInterval.getLengthInDays();
            copy.valueArray = new double[valueArray.length];
            for (int i = 0; i < xArray.length; i++) {
                copy.valueArray[i] = absoluteTilt(xArray[i], valueArray[i], length, time, radians, radianShift);
            }
        }
        return copy;
    }

    /**
     * Tilts the curve relatively at the supplied fulcrum through a range of
     * -45` to 45`.
     */
    protected final DateBasedSurface1D relativeTilt(LocalDate fulcrum, double degrees, double shiftProportion) {
        DateBasedSurface1D copy = (DateBasedSurface1D) clone();
        if (degrees != 0.0 && shiftProportion != 0.0) {
            double radians = Math.toRadians(degrees);
            double radianShift = radians / degrees * shiftProportion;
            long time = fulcrum.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long length = dateInterval.getLengthInDays();
            copy.valueArray = new double[valueArray.length];
            for (int i = 0; i < xArray.length; i++) {
                copy.valueArray[i] = relativeTilt(xArray[i], valueArray[i], length, time, radians, radianShift);
            }
        }
        return copy;
    }

    /**
     * Flexes the curve in a relative or absolute fashion.
     */
    public final DateBasedSurface1D flex(LocalDate date1, LocalDate date2, double shiftProportion, boolean isRelative) {
        if (!dateInterval.contains(date1)) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument date1 out of " + "range [found=" + date1 + ", required="
                    + dateInterval + "]");
        }
        if (!dateInterval.contains(date2)) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument date2 out of " + "range [found=" + date2 + ", required="
                    + dateInterval + "]");
        }
        DateBasedSurface1D copy = (DateBasedSurface1D) clone();
        if (shiftProportion != 0.0) {
            long radius = dateInterval.getLengthInDays();
            long segment = Math.abs(date1.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()


            - date2.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());


            long center = (date1.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() +
                    date2.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()) / 2L;
            double height = radius;
            if (segment != 0) {
                double theta = Math.acos(segment / 2.0 / radius);
                height = radius * Math.sin(theta);
            }
            double increment = radius - height;
            copy.valueArray = new double[valueArray.length];
            for (int i = 0; i < xArray.length; i++) {
                double x = Math.abs(xArray[i] - center);
                double a = Math.acos(x / radius);
                double y = radius * Math.sin(a);
                double value = valueArray[i];
                if (isRelative) {
                    value = value + value * (radius - (y + increment)) * shiftProportion / radius;
                } else {
                    value = value + (radius - (y + increment)) * shiftProportion / radius;
                }
                copy.valueArray[i] = value;
            }
        }
        return copy;
    }

    protected final DateBasedSurface1D absoluteFlex(LocalDate date1, LocalDate date2, double shiftProportion)
            throws IllegalArgumentException {
        return flex(date1, date2, shiftProportion, false);
    }

    protected final DateBasedSurface1D relativeFlex(LocalDate date1, LocalDate date2, double shiftProportion) {
        return flex(date1, date2, shiftProportion, true);
    }

    /**
     * Shifts all the values by a relative shift amount, and returns the
     * resultant curve.
     * <p>
     * Shifts all the values of the internally constructed copy by a relative
     * shift amount.
     */
    protected DateBasedSurface1D relativeShift(double shift) {
        DateBasedSurface1D copy = (DateBasedSurface1D) clone();
        if (shift != 0.0) {
            copy.valueArray = new double[valueArray.length];
            for (int i = 0; i < valueArray.length; i++) {
                copy.valueArray[i] = relativeShift(xArray[i], valueArray[i], shift);
            }
        }
        return copy;
    }

    /**
     * Shifts all the values by an absolute shift amount, and returns the
     * resultant curve.
     * <p>
     * Shifts all the values of the internally constructed copy by an absolute
     * shift amount.
     */
    protected DateBasedSurface1D absoluteShift(double shift) {
        DateBasedSurface1D copy = (DateBasedSurface1D) clone();
        if (shift != 0.0) {
            copy.valueArray = new double[valueArray.length];
            for (int i = 0; i < valueArray.length; i++) {
                copy.valueArray[i] = absoluteShift(xArray[i], valueArray[i], shift);
            }
        }
        return copy;
    }

    public DateBasedSurface1D floorNegatives() {
        DateBasedSurface1D copy = (DateBasedSurface1D) clone();
        copy.valueArray = new double[valueArray.length];
        for (int i = 0; i < valueArray.length; i++) {
            if (valueArray[i] < 0.0) {
                copy.valueArray[i] = 0.0;
            } else {
                copy.valueArray[i] = valueArray[i];
            }
        }
        return copy;
    }

    protected DateBasedSurface1D absoluteShift(int index, double shift) {
        DateBasedSurface1D copy = (DateBasedSurface1D) clone();
        if (shift != 0.0) {
            copy.valueArray[index] = absoluteShift(xArray[index], valueArray[index], shift);
        }
        return copy;
    }

    protected DateBasedSurface1D relativeShift(int index, double shift) {
        DateBasedSurface1D copy = (DateBasedSurface1D) clone();
        if (shift != 0.0) {
            copy.valueArray[index] = relativeShift(xArray[index], valueArray[index], shift);
        }
        return copy;
    }

    /**
     * Shifts all the values by a relative bucketed shift amount, and returns
     * the resultant curve.
     * <p>
     * Shifts all the values of the internally constructed copy by a relative
     * shift amount according to their distance from the contingent date and
     * whether or not they fall within the interval.
     */
    protected DateBasedSurface1D relativeBucketShift(ContingentDateInterval dateInterval, double shift) {
        long bucketTime = dateInterval.getContingentDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();


        DateBasedSurface1D copy = setValue(bucketTime); /* Gets cloned */
        if (shift != 0.0) {
            DateInterval interval = dateInterval.getDateInterval();
            double bucketLength = interval.getLengthInDays();
            for (int i = 0; i < copy.xArray.length; i++) {
                long time = copy.xArray[i];
                if (interval.contains(Instant.ofEpochMilli((time)).atZone(ZoneId.systemDefault()).toLocalDate())) {
                    double weight = 1.0 - Math.abs((bucketTime - time)) / bucketLength;
                    copy.valueArray[i] = relativeShift(time, copy.valueArray[i], weight * shift);
                }
            }
        }
        return copy;
    }

    /**
     * Shifts all the values by an absolute bucketed shift amount, and returns
     * the resultant curve.
     * <p>
     * Shifts all the values of the internally constructed copy by an absolute
     * shift amount according to their distance from the contingent date and
     * whether or not they fall within the interval.
     */
    protected DateBasedSurface1D absoluteBucketShift(ContingentDateInterval dateInterval, double shift) {
        long bucketTime = dateInterval.getContingentDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();


        DateBasedSurface1D copy = setValue(bucketTime); /* Gets cloned */
        if (shift != 0.0) {
            DateInterval interval = dateInterval.getDateInterval();
            double bucketLength = interval.getLengthInDays();
            for (int i = 0; i < copy.xArray.length; i++) {
                long time = copy.xArray[i];
                if (interval.contains(Instant.ofEpochMilli((time)).atZone(ZoneId.systemDefault()).toLocalDate())) {
                    double weight = 1.0 - Math.abs((bucketTime - time)) / bucketLength;
                    copy.valueArray[i] = absoluteShift(time, copy.valueArray[i], weight * shift);
                }
            }
        }
        return copy;
    }

    /**
     * Shifts all the values by a relative bucketed shift amount, and returns
     * the resultant curve.
     * <p>
     * Shifts all the values in the Date interval.
     */
    protected DateBasedSurface1D relativeNoWeightingBucketShift(DateInterval dateInterval, double shift) {
        DateBasedSurface1D copy = (DateBasedSurface1D) clone();
        if (shift != 0.0) {
            for (int i = 0; i < copy.xArray.length; i++) {
                long time = copy.xArray[i];
                if (dateInterval.contains(Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate())) {
                    copy.valueArray[i] = relativeShift(time, copy.valueArray[i], shift);
                }
            }
        }
        return copy;
    }

    /**
     * Shifts all the values by an absolute bucketed shift amount, and returns
     * the resultant curve.
     * <p>
     * Shifts all the values of the internally constructed copy by an absolute
     * shift amount according to their distance from the contingent date and
     * whether or not they fall within the interval.
     */
    protected DateBasedSurface1D absoluteNoWeightingBucketShift(DateInterval dateInterval, double shift) {
        DateBasedSurface1D copy = (DateBasedSurface1D) clone();
        if (shift != 0.0) {
            for (int i = 0; i < copy.xArray.length; i++) {
                long time = copy.xArray[i];
                if (dateInterval.contains(Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate())) {
                    copy.valueArray[i] = absoluteShift(time, copy.valueArray[i], shift);
                }
            }
        }
        return copy;
    }

    /**
     * Translates everything relative to the new anchor date, and returns the
     * resultant curve.Values remain invariant by default.
     * <p>
     * Translates all the dates and values of the internally constructed copy.
     *
     * @throws UnsupportedOperationException if the implementing class does not
     *         cater for this operation
     * @throws NullPointerException if the supplied date argument is null
     */
    protected Object translate(Date newAnchorDate) {
        DateBasedSurface1D copy = (DateBasedSurface1D) clone();
        long anchorTime = getAnchorTime();
        if (newAnchorDate.getTime() != anchorTime) {
            long translation = newAnchorDate.getTime() - anchorTime;
            copy.xArray = new long[xArray.length];
            for (int i = 0; i < xArray.length; i++) {
                long date = xArray[i];
                long translatedDate = date + translation;
                copy.xArray[i] = translatedDate;
            }
            // Diana Naidoo
            // Added to set the new date interval
            copy.dateInterval = new DateInterval(Instant.ofEpochMilli((dateInterval.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()+ translation)).atZone(ZoneId.systemDefault()).toLocalDate(),
                    dateInterval.isStartInclusive(), Instant.ofEpochMilli((dateInterval.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() + translation)).atZone(ZoneId.systemDefault()).toLocalDate(),
                    dateInterval.isEndInclusive());
        }

        return copy;
    }

    /**
     * Returns the result of a call to <code>interpolator.isThreadSafe()</code>)
     *
     */


    /**
     * Returns the anchor date.
     * <p>
     * Consider <code>getAnchorTime()</code> for efficiency.
     *
     * @return the anchor date.
     */
    public final LocalDate getAnchorDate() {
        return dateInterval.getStartDate();
    }

    /**
     * Returns the anchor date as a <code>long</code>.
     *
     * @return the anchor date as a <code>long</code>.
     */
    public final long getAnchorTime() {
        return dateInterval.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Returns the boundaryType.
     *
     * @return the boundaryType.
     */
    public final int getBoundaryType() {
        return boundaryType;
    }

    /**
     * Returns the dateInterval.
     *
     * @return the dateInterval.
     */
    public final DateInterval getDateInterval() {
        return dateInterval;
    }

    /**
     * Returns <code>true</code>. This is a preemptive declaration, since it is
     * envisaged that sub-classes will implement the
     * <code>TimeTranslationInvariant</code> interface specification.
     *
     * @return <code>true</code>.
     */
    public final boolean isAnchored() {
        return true;
    }

    /**
     * Returns the daycount convention associated with this curve.
     *
     * @return the daycount convention associated with this curve.
     */
    public final DayCountConvention getDayCountConvention() {
        return dayCountConvention;
    }

    /**
     * Returns the interpolator.
     *
     * @return the interpolator.
     */
    public final Interpolator1D getInterpolator() {
        return interpolator;
    }

    /**
     * Returns a map of this curve's dates to their corresponding values.
     * <p>
     * The keys are <code>Date</code> instances and the values are those that
     * characterize this curve, for example interest rates.
     *
     * @return a map of this curve's dates to their corresponding values.
     */
    public SortedMap<Date, Object> getValues() {
        TreeMap<Date, Object> map = new TreeMap<Date, Object>();
        for (int i = 0; i < xArray.length; i++) {
            long date = xArray[i];
            map.put(new Date(date), createValue(date, valueArray[i]));
        }
        return map;
    }

    /**
     * Returns a map of this curve's dates to their corresponding values.
     * <p>
     * The keys are <code>Date</code> instances and the values are those that
     * characterize this curve, for example interest rates.
     *
     * @return a map of this curve's dates to their corresponding values.
     */
    public SortedMap<LocalDate, Object> getValues(DateInterval dateInterval) {
        TreeMap<LocalDate, Object> map = new TreeMap<LocalDate, Object>();
        for (int i = 0; i < xArray.length; i++) {
            long date = xArray[i];
            if (dateInterval.contains(Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate())) {
                map.put(Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate(), createValue(date, valueArray[i]));
            }
        }
        return map;
    }

    public SortedMap<Date, Double> getDoubleValues(DateInterval dateInterval) {
        TreeMap<Date, Double> map = new TreeMap<Date, Double>();
        for (int i = 0; i < xArray.length; i++) {
            long date = xArray[i];
            if (dateInterval.contains(Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate())) {
                map.put(new Date(date), valueArray[i]);
            }
        }
        return map;
    }

    /**
     * Returns a map of this curve's dates to their corresponding
     * <code>Double</code> values.
     * <p>
     * The keys are <code>Date</code> instances and the values are
     * <code>Double</code> instances.
     *
     * @return a map of this curve's dates to their corresponding
     *         <code>Double</code> values.
     */
    public SortedMap<Date, Double> getDoubleValues() {
        TreeMap<Date, Double> map = new TreeMap<Date, Double>();
        for (int i = 0; i < xArray.length; i++) {
            long date = xArray[i];
            map.put(new Date(date), Double.valueOf(valueArray[i]));
        }
        return map;
    }

    /**
     * Returns a set of this curve's explicitly defined dates. That is, those
     * dates for which values have been defined.
     */
    public NavigableSet<Date> getDates() {
        TreeSet<Date> set = new TreeSet<Date>();
        for (long date : xArray) {
            set.add(new Date(date));
        }
        return set;
    }

    /**
     * Returns a copy of the internal date array as <code>Date</code> instances.
     * <p>
     * The returned array is sorted in ascending order.
     *
     * @return a copy of the internal date array as <code>Date</code> instances.
     */
    public LocalDate[] getDatesArray() {
        LocalDate[] copy = new LocalDate[xArray.length];
        for (int i = 0; i < xArray.length; i++) {
            long time = xArray[i];
            copy[i] = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate(); // or specify a zone: ZoneId.of("UTC")

        }
        return copy;
    }

    /**
     * Returns a copy of the internal date array as <code>long</code> instances.
     * <p>
     * The returned array is sorted in ascending order.
     *
     * @return a copy of the internal date array as <code>long</code> instances.
     */
    public long[] getDatesLArray() {
        long[] copy = new long[xArray.length];
        System.arraycopy(xArray, 0, copy, 0, xArray.length);
        return copy;
    }

    /**
     * Returns the explicitly defined dates as year fractions from anchor date.
     * <p>
     * The returned array is sorted in ascending order.
     *
     * @return the explicitly defined dates as year fractions from anchor date.
     */
    public double[] getDatesYFArray() {
        double[] yearFractions = new double[xArray.length];
        for (int i = 0; i < xArray.length; i++) {
            yearFractions[i] = dayCountConvention.calculateDayCountFraction(Instant.ofEpochMilli(getAnchorTime())
                    .atZone(ZoneId.of("UTC"))
                    .toLocalDate(), Instant.ofEpochMilli(xArray[i])
                    .atZone(ZoneId.of("UTC"))
                    .toLocalDate());
        }
        return yearFractions;
    }

    /**
     * Returns the explicitly defined dates as year fractions from anchor date.
     * <p>
     * The returned array is sorted in ascending order.
     *
     * @return the explicitly defined dates as year fractions from anchor date.
     */
    public double[] getDaysArray() {
        double[] days = new double[xArray.length];
        for (int i = 0; i < xArray.length; i++) {
            days[i] = dayCountConvention.getDayCount(getAnchorTime(), xArray[i]);
        }
        return days;
    }

    /**
     * Returns the value information as specialized objects such as
     * <code>InterestRate</code> s etc.
     * <p>
     * The returned array is sorted in the same ascending order as the
     * internally stored dates.
     *
     * @return the value information as specialized objects such as
     *         <code>InterestRate</code> s etc.
     */
    public Object[] getValuesArray() {
        Object[] copy = new Object[xArray.length];
        for (int i = 0; i < xArray.length; i++) {
            copy[i] = createValue(xArray[i], valueArray[i]);
        }
        return copy;
    }

    /**
     * Returns the value information as <code>double</code> instances
     * <p>
     * The returned array is sorted in the same ascending order as the
     * internally stored dates.
     *
     * @return the value information as <code>double</code> instances
     */
    public double[] getValuesDArray() {
        double[] copy = new double[valueArray.length];
        System.arraycopy(valueArray, 0, copy, 0, valueArray.length);
        return copy;
    }

    public double[] getValuesIvertedDArray() {
        double[] copy = new double[valueArray.length];
        for (int i = 0; i < valueArray.length; i++) {
            copy[i] = 1 / valueArray[i];
        }

        return copy;
    }

    /**
     * Returns a copy of this curve. If subclasses have additional thread-unsafe
     * components , they may need to override this method.
     *
     * @return a copy of this curve.
     */
    @Override
    public Object clone() {
        DateBasedSurface1D copy = null;
        try {
            copy = (DateBasedSurface1D) super.clone();
        } catch (CloneNotSupportedException e) {
        } /* Wont happen */
        return copy;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(super.toString());
        buffer.append("\nInterval=" + dateInterval);
        buffer.append("\nInterpolator=" + interpolator);
        buffer.append("\nDayCountConvention=" + dayCountConvention);
        Date date = null;
        for (int n = 0; n < xArray.length; n++) {
            date = new Date(xArray[n]);
            buffer.append("\n" + date + "=\t" + valueArray[n]);
        }
        return buffer.toString();
    }

    /**
     * Returns <tt>true</tt> if this surface contains the specified date
     * explicitly, <code>false</code> otherwise.
     *
     * @param date date whose (explicit) existence in this set is to be tested.
     * @param isIncludeEndpoints flag indicating if the test is to be done on
     *        explicitly defined dates only, or alternatively to include the
     *        end-points.
     * @return <tt>true</tt> if this source contains the specified date.
     */
    public final boolean contains(LocalDate date, boolean isIncludeEndpoints) {
        long time = date.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        if (isIncludeEndpoints) {
            if (dateInterval.getStartDate().atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli() == time || dateInterval.getEndDate().atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli() == time) {
                return true;
            }
        } else {
            for (long element : xArray) {
                if (time == element) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the date following the supplied date. If the supplied argument is
     * out of right-bounds, <code>null</code> is returned.
     *
     * @param date date for which the next date is required
     * @param isStrictlyNext indicates whether the strictly next date is
     *        required.
     * @param isIncludeEndpoints flag indicating if the test is to be done on
     *        explicitly defined dates only, or alternatively to include the
     *        end-points.
     */
    public final Date getNextDate(Date date, boolean isStrictlyNext, boolean isIncludeEndpoints) {
        /*
         * This method has been implemented as follows for clarity more than
         * performance
         */

        long time = date.getTime();
        /* Can do following cause interval always closed */
        long leftBoundary = dateInterval.getStartDate().atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        long rightBoundary = dateInterval.getEndDate().atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        /* Small optimization */
        if (time > rightBoundary) {
            return null;
        }

        long[] timeArray = null;
        if (isIncludeEndpoints) {
            if (leftBoundary != xArray[0]) {
                if (rightBoundary != xArray[xArray.length - 1]) {
                    timeArray = new long[xArray.length + 2];
                    timeArray[0] = leftBoundary;
                    timeArray[timeArray.length - 1] = rightBoundary;
                    System.arraycopy(xArray, 0, timeArray, 1, xArray.length);
                } else {
                    timeArray = new long[xArray.length + 1];
                    timeArray[0] = leftBoundary;
                    System.arraycopy(xArray, 0, timeArray, 1, xArray.length);
                }
            } else {
                if (rightBoundary != xArray[xArray.length - 1]) {
                    timeArray = new long[xArray.length + 1];
                    timeArray[timeArray.length - 1] = rightBoundary;
                    System.arraycopy(xArray, 0, timeArray, 0, xArray.length);
                } else {
                    timeArray = xArray;
                }
            }
        } else {
            timeArray = xArray;
        }

        for (long point : timeArray) {
            if (time < point) {
                return new Date(point);
            } else if (time == point) {
                if (!isStrictlyNext) {
                    return new Date(point);
                }
            }
        }
        return null;
    }

    /**
     * Returns the date preceding the supplied date. If the supplied argument is
     * out of left-bounds, <code>null</code> is returned.
     *
     * @param date date for which the previous date is required
     * @param isStrictlyPrevious indicates whether the strictly previous date is
     *        required.
     * @param isIncludeEndpoints flag indicating if the test is to be done on
     *        explicitly defined dates only, or alternatively to include the
     *        end-points.
     */
    public final LocalDate getPreviousDate(LocalDate date, boolean isStrictlyPrevious, boolean isIncludeEndpoints) {
        /*
         * This method has been implemented as follows for clarity more than
         * performance
         */

        long time = date.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        /* Can do following cause interval always closed */
        long leftBoundary = dateInterval.getStartDate().atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        long rightBoundary = dateInterval.getEndDate().atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        /* Small optimization */
        if (time < leftBoundary) {
            return null;
        }

        long[] timeArray = null;
        if (isIncludeEndpoints) {
            if (leftBoundary != xArray[0]) {
                if (rightBoundary != xArray[xArray.length - 1]) {
                    timeArray = new long[xArray.length + 2];
                    timeArray[0] = leftBoundary;
                    timeArray[timeArray.length - 1] = rightBoundary;
                    System.arraycopy(xArray, 0, timeArray, 1, xArray.length);
                } else {
                    timeArray = new long[xArray.length + 1];
                    timeArray[0] = leftBoundary;
                    System.arraycopy(xArray, 0, timeArray, 1, xArray.length);
                }
            } else {
                if (rightBoundary != xArray[xArray.length - 1]) {
                    timeArray = new long[xArray.length + 1];
                    timeArray[timeArray.length - 1] = rightBoundary;
                    System.arraycopy(xArray, 0, timeArray, 0, xArray.length);
                } else {
                    timeArray = xArray;
                }
            }
        } else {
            timeArray = xArray;
        }

        int max = timeArray.length - 1;

        for (int i = max; i >= 0; i--) {
            long point = timeArray[i];
            if (time > point) {
                return  Instant.ofEpochMilli((point)).atZone(ZoneId.systemDefault()).toLocalDate();
            } else if (time == point) {
                if (!isStrictlyPrevious) {
                    return Instant.ofEpochMilli((point)).atZone(ZoneId.systemDefault()).toLocalDate();
                }
            }
        }
        return null;
    }

    // ************************************************************************//
    // ************************ Protected Stuff
    // *******************************//
    // ************************************************************************//

    /**
     * Subclasses must override this method to return a specialized rate object.
     * <p>
     * Currently returns a Double
     */
    protected abstract Object createValue(long date, double value);

    /**
     * Subclasses can override this method to return an absolutely tilted value.
     * <p>
     * Currently returns
     *
     * <pre>
     * double tan = Math.tan(radians);
     * long time = Math.abs(date - fulcrum);
     * return tan * radianShift * (time / length) + value;
     * </pre>
     */
    protected double absoluteTilt(long date, double value, long length, long fulcrum, double radians,
                                  double radianShift) {
        double tan = Math.tan(radians);
        long time = date - fulcrum;
        return tan * radianShift * time / length + value;
    }

    /**
     * Subclasses can override this method to return a relatively tilted value.
     * <p>
     * Currently returns
     *
     * <pre>
     * double tan = Math.tan(radians);
     * long time = Math.abs(date - fulcrum);
     * return value * tan * shiftProportion * (time / length) + value;
     * </pre>
     */
    protected double relativeTilt(long date, double value, long length, long fulcrum, double radians,
                                  double radianShift) {
        double tan = Math.tan(radians);
        long time = date - fulcrum;
        return value * tan * radianShift * time / length + value;
    }

    /**
     * Subclasses can override this method to return a relatively shifted value.
     * <p>
     * Currently returns
     *
     * <pre>
     * value * shift + value
     * </pre>
     */
    protected double relativeShift(long date, double value, double shift) {
        return value * shift + value;
    }

    protected double relativeShift(double value, double shift) {
        return value * shift + value;
    }

    /**
     * Subclasses can override this method to return a relatively shifted value.
     * <p>
     * Currently returns
     *
     * <pre>
     * shift + value
     * </pre>
     */
    protected double absoluteShift(long date, double value, double shift) {
        return shift + value;
    }

    protected double multiplicationShift(long date, double value, double shift) {
        return shift * value;
    }

    protected double absoluteShift(double value, double shift) {
        return shift + value;
    }

    //used to give enrich exceptions thrown in this class with info of the specific curve it is in
    protected abstract String getSpecificCurveType();

    public abstract String getCurveReference();


    // ************************************************************************//
    // ************************ Private Stuff
    // *********************************//
    // ************************************************************************//

    private final void initialize(LocalDate boundaryDate, int boundaryType, Map<LocalDate, Double> values)
            throws ClassCastException, NullPointerException {
        if (boundaryType != LEFT_BOUNDARY && boundaryType != RIGHT_BOUNDARY) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument boundaryType invalid [found=" + boundaryType + ", required="
                    + LEFT_BOUNDARY + "," + RIGHT_BOUNDARY + "]");
        }
        if (values.isEmpty()) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument values cannot be empty.");
        }
        TreeMap sortedValues = new TreeMap(values);
        LocalDate firstDate = (LocalDate) sortedValues.firstKey();
        LocalDate lastDate = (LocalDate) sortedValues.lastKey();
        if (boundaryType == LEFT_BOUNDARY && boundaryDate.isAfter(firstDate)) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument boundaryDate must on or before " + firstDate);
        } if (boundaryType == RIGHT_BOUNDARY && boundaryDate.isBefore(lastDate)) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument boundaryDate must on or after " + lastDate);
        }
        int nValues = sortedValues.size();
        xArray = new long[nValues];
        valueArray = new double[nValues];
        int counter = 0;
        Iterator iterator = sortedValues.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            //xArray[counter] = ((LocalDate) entry.getKey()).getTime();
            xArray[counter] = ((LocalDate) entry.getKey()).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            valueArray[counter] = ((Double) entry.getValue()).doubleValue();
            counter++;
        }
        if (boundaryType == LEFT_BOUNDARY) {
            dateInterval = new DateInterval(boundaryDate, true, lastDate, true);
        } else {
            dateInterval = new DateInterval(firstDate, true, boundaryDate, true);
        }
        this.boundaryType = boundaryType;
    }

    private final void initialize(LocalDate boundaryDate, int boundaryType, LocalDate[] x, double[] values)
            throws ClassCastException, NullPointerException {
        if (boundaryType != LEFT_BOUNDARY && boundaryType != RIGHT_BOUNDARY) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument boundaryType invalid [found=" + boundaryType + ", required="
                    + LEFT_BOUNDARY + "," + RIGHT_BOUNDARY + "]");
        }
        if (x.length == 0) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument dates cannot be empty.");
        }
        if (x.length != values.length) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Arguments dates and values must be equal length [dates=" + x.length
                    + ", " + values.length + "]");
        }
        LocalDate firstDate = x[0];
        LocalDate lastDate = x[x.length - 1];
        if (boundaryType == LEFT_BOUNDARY && boundaryDate.isAfter(firstDate)) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument boundaryDate must on or before " + firstDate);
        } else if (boundaryType == RIGHT_BOUNDARY && boundaryDate.isBefore(lastDate)) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument boundaryDate must on or after " + lastDate);
        }
        xArray = new long[x.length];
        xArray[0] = firstDate.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();;
        valueArray = new double[x.length];
        System.arraycopy(values, 0, valueArray, 0, values.length);
        for (int i = 1; i < x.length; i++) {
            xArray[i] = x[i].atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();
            if (xArray[i] <= xArray[i - 1]) {
                throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                        + getCurveReference() + ": Argument dates must be sorted in ascending order [index=" + i + "]");
            }
        }
        if (boundaryType == LEFT_BOUNDARY) {
            dateInterval = new DateInterval(boundaryDate, true, lastDate, true);
        } else {
            dateInterval = new DateInterval(firstDate, true, boundaryDate, true);
        }
        this.boundaryType = boundaryType;
    }

    private final void initialize(long boundaryDate, int boundaryType, long[] x, double[] values)
            throws ClassCastException, NullPointerException {
        if (boundaryType != LEFT_BOUNDARY && boundaryType != RIGHT_BOUNDARY) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument boundaryType invalid [found=" + boundaryType + ", required="
                    + LEFT_BOUNDARY + "," + RIGHT_BOUNDARY + "]");
        }
        if (x.length == 0) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument dates cannot be empty.");
        }
        if (x.length != values.length) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Arguments dates and values must be equal length [dates=" + x.length
                    + ", " + values.length + "]");
        }
        long firstDate = x[0];
        long lastDate = x[x.length - 1];
        if (boundaryType == LEFT_BOUNDARY && boundaryDate > firstDate) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument boundaryDate must on or before " + Instant.ofEpochMilli(firstDate).atZone(ZoneId.systemDefault()).toLocalDate());
        } else if (boundaryType == RIGHT_BOUNDARY && boundaryDate < lastDate) {
            throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                    + getCurveReference() + ": Argument boundaryDate must on or after " + Instant.ofEpochMilli(lastDate).atZone(ZoneId.systemDefault()).toLocalDate());
        }
        xArray = new long[x.length];
        System.arraycopy(x, 0, xArray, 0, x.length);
        valueArray = new double[x.length];
        System.arraycopy(values, 0, valueArray, 0, values.length);
        for (int i = 1; i < x.length; i++) {
            if (xArray[i] <= xArray[i - 1]) {
                throw new IllegalArgumentException("In the curve " + getSpecificCurveType() + " with reference "
                        + getCurveReference() + ": Argument dates must be sorted in ascending order [index=" + i + "]");
            }
        }
        if (boundaryType == LEFT_BOUNDARY) {
            dateInterval = new DateInterval(Instant.ofEpochMilli(boundaryDate).atZone(ZoneId.systemDefault()).toLocalDate() , true, Instant.ofEpochMilli(lastDate).atZone(ZoneId.systemDefault()).toLocalDate(), true);
        } else {
            dateInterval = new DateInterval(Instant.ofEpochMilli(firstDate).atZone(ZoneId.systemDefault()).toLocalDate() , true, Instant.ofEpochMilli(boundaryDate).atZone(ZoneId.systemDefault()).toLocalDate(), true);
        }
        this.boundaryType = boundaryType;
    }

    //@Override
    /*public DateBasedSurface1D operate(ScenarioOperator operator) {
        DateBasedSurface1D copy = (DateBasedSurface1D) this.clone();
        DateBasedSurface1DOperator dateOperator = (DateBasedSurface1DOperator) operator;
        for (long xValue : xArray) {
            double value = this.getDoubleValue(xValue);
            double perturbedValue = dateOperator.perturb(xValue, value);
            copy = copy.setValue(xValue, perturbedValue);
        }

        return copy;
    }*/

    /*public DateBasedSurface1D scenarioSurface(BusinessCenter businessCenter, BusinessDayConvention dayConvention,
                                              Object[] dateArray, Double[] valueArray) {
        DateBasedSurface1DData data = DateBasedSurface1DData.newInstance(businessCenter, dayConvention, getAnchorDate(),
                dateArray, valueArray);
        return new InternalSurface(getAnchorDate(), data.getData(), interpolator, getDayCountConvention(),
                getCurrency());
    }*/

    public Currency getCurrency() {
        return currency;
    }

    public Double[] getInterpolationPoints() {

        ArrayList<Double> interpolationPoints = new ArrayList<Double>();
        for (int i = 0; i < getDatesArray().length; i++) {
            interpolationPoints.add(Double.valueOf(ChronoUnit.DAYS.between(this.getAnchorDate(), getDatesArray()[i])));
        }
        return interpolationPoints.toArray(new Double[interpolationPoints.size()]);
    }

}

