package com.ats.pricing.marketData;

import com.ats.math.numeric.Interpolator1D;
import com.ats.pricing.foundation.Currency;
import com.ats.pricing.tools.DayCountConvention;

import java.util.Date;
import java.util.Map;



    public class InterestRateCurve extends DateBasedSurface1D {


        /**
         * The compounding convention in which the values are defined.
         */
        private Compounding compounding;

        private boolean isTranslatable = true;

        private long boundaryDate;

        protected Entity entity;

        private String referenceName;

        public InterestRateCurve(Date boundaryDate, int boundaryType, String datesName, Map<Date, Double> values,
                                 Interpolator1D interpolator, Compounding compounding, DayCountConvention dayCountConvention,
                                 boolean isTranslatable, Entity entity, String reference, Currency currency)
                throws IllegalArgumentException {
            super(boundaryDate, boundaryType, datesName, "Interest Rates", values, interpolator, dayCountConvention,
                    currency);
            this.compounding = compounding;
            this.isTranslatable = isTranslatable;
            this.boundaryDate = boundaryDate.getTime();
            this.entity = entity;
            this.referenceName = reference;
        }

        public InterestRateCurve(Date boundaryDate, int boundaryType, String datesName, Date[] dates, double[] values,
                                 Interpolator1D interpolator, Compounding compounding, DayCountConvention dayCountConvention,
                                 boolean isTranslatable, String reference, Currency currency) throws IllegalArgumentException {
            super(boundaryDate, boundaryType, datesName, dates, "Interest Rates", values, interpolator, dayCountConvention,
                    currency);
            this.compounding = compounding;
            this.isTranslatable = isTranslatable;
            this.boundaryDate = boundaryDate.getTime();
            this.referenceName = reference;
        }

        public InterestRateCurve(Date boundaryDate, int boundaryType, String datesName, Date[] dates, double[] values,
                                 Interpolator1D interpolator, Compounding compounding, DayCountConvention dayCountConvention,
                                 boolean isTranslatable, Entity entity, String reference, Currency currency)
                throws IllegalArgumentException {
            super(boundaryDate, boundaryType, datesName, dates, "Interest Rates", values, interpolator, dayCountConvention,
                    currency);
            this.compounding = compounding;
            this.isTranslatable = isTranslatable;
            this.boundaryDate = boundaryDate.getTime();
            this.entity = entity;
            this.referenceName = reference;
        }

        public InterestRateCurve(long boundaryDate, int boundaryType, String datesName, long[] dates, double[] values,
                                 Interpolator1D interpolator, Compounding compounding, DayCountConvention dayCountConvention,
                                 boolean isTranslatable, String reference, Currency currency) throws IllegalArgumentException {
            super(boundaryDate, boundaryType, datesName, dates, "Interest Rates", values, interpolator, dayCountConvention,
                    currency);
            this.compounding = compounding;
            this.isTranslatable = isTranslatable;
            this.boundaryDate = boundaryDate;
            this.referenceName = reference;
        }

        public InterestRateCurve(long boundaryDate, int boundaryType, String datesName, long[] dates, double[] values,
                                 Interpolator1D interpolator, Compounding compounding, DayCountConvention dayCountConvention,
                                 boolean isTranslatable, Entity entity, String reference, Currency currency)
                throws IllegalArgumentException {
            super(boundaryDate, boundaryType, datesName, dates, "Interest Rates", values, interpolator, dayCountConvention,
                    currency);
            this.compounding = compounding;
            this.isTranslatable = isTranslatable;
            this.boundaryDate = boundaryDate;
            this.entity = entity;
            this.referenceName = reference;
        }

        public final boolean isTranslatable() {
            return isTranslatable;
        }

        /**
         * Returns the compounding convention in which the rates have been defined.
         *
         * @return the compounding convention in which the rates have been defined.
         */
        public final Compounding getCompounding() {
            return compounding;
        }

        public long getBoundaryDate() {
            return boundaryDate;
        }

        public void setEntity(Entity entity) {
            this.entity = entity;
        }

        /**
         * Returns the entity.
         *
         * @return the entity that have been defined.
         */
        public Entity getEntity() {
            return entity;
        }

        /* Returns the zero rate from anchor to the supplied date */
        public final InterestRate getInterestRate(Date date) throws IllegalArgumentException {
            return (InterestRate) getValue(date);
        }

        public InterestRate getFixingInInterval(Date startDate, Date endDate) {
            return (InterestRate) getValue(startDate, endDate);
        }

        public final InterestRateCurve setInterestRate(Date date, double annualizedRate) {
            return (InterestRateCurve) setValue(date, annualizedRate);
        }

        public final InterestRateCurve setInterestRate(long time, double annualizedRate) {
            return (InterestRateCurve) setValue(time, annualizedRate);
        }

        /**
         * Returns a new <code>InterestRateCurve</code> instance with the supplied
         * conventions and appropriately adjusted rates.
         *
         * @return a new <code>InterestRateCurve</code> instance with the supplied
         *         conventions and appropriately adjusted rates.
         */
        public InterestRateCurve convert(Compounding newCompounding, DayCountConvention newDayCountConvention)
                throws NullPointerException {
            Compounding compounding = getCompounding();
            if (newCompounding.equals(compounding) && newDayCountConvention.equals(getDayCountConvention())) {
                return this;
            }
            long[] dates = getDatesLArray();
            double[] values = getValuesDArray();
            for (int i = 0; i < values.length; i++) {
                double d = values[i];
                values[i] = new InterestRate(d, compounding, getDayCountConvention())
                        .convert(newCompounding, newDayCountConvention).getAnnualizedRate();
            }
            InterestRateCurve copy = new InterestRateCurve(boundaryDate, getBoundaryType(), getXName(), dates, values,
                    getInterpolator(), newCompounding, newDayCountConvention, isTranslatable, getCurveReference(),
                    getCurrency());
            return copy;
        }

        public Object translate(Date newAnchorDate) {
            if (isTranslatable) {
                return super.translate(newAnchorDate);
            } else {
                return this;
            }
        }

        // ************************************************************************//
        // ************************ Protected Stuff
        // *******************************//
        // ************************************************************************//

        protected final Object createValue(long date, double rate) {
            return new InterestRate(rate, compounding, getDayCountConvention());
        }

        protected Object getValue(Date date1, Date date2)
                throws UnsupportedOperationException, IllegalArgumentException, NullPointerException {
            for (Date fixingDate : getDatesArray()) {
                if (fixingDate.after(date1) && fixingDate.before(date2))
                    return getValue(fixingDate);
            }

            throw new IllegalArgumentException(
                    String.format("No fixings were found in the interval %s to %s", date1, date2));
        }

        protected double getDoubleValue(Date date1, Date date2)
                throws UnsupportedOperationException, IllegalArgumentException, NullPointerException {
            throw new UnsupportedOperationException();
        }

        @Override
        protected String getSpecificCurveType() {
            return "InterestRateCurve";
        }

        @Override
        public String getCurveReference() {
            return this.referenceName;
        }

        public void setCurveReference(String referenceName) {
            this.referenceName = referenceName;
        }

        public Date getLastFixingDate() {
            return new Date(boundaryDate);
        }

    }
