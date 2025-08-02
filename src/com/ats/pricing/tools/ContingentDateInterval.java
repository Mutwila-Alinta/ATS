package com.ats.pricing.tools;


import ats.display.TradeFormFactory;

import java.time.LocalDate;

public class ContingentDateInterval {
    /**
     * Encapsulation of a <code>DateInterval</code> and a <code>Date</code> on which
     * this interval is somehow contingent.
     * <p>
     * An example of its usage is in the measurement of a contract's realized or
     * non-realized (aka expected) cashflow profile. This would then translate to
     * (respectively):
     *
     * <pre>
     *
     *        &quot;Return all cashflows occuring within the specified interval from this
     *         contingent date backwards (onwards)&quot;
     * </pre>
     *
     * In both cases, the interval represents the period over which the required
     * cashflows are to be calculated, and the contingent date represents that point
     * in time beyond which it is assumed no (realized) information is available.
     * <p>
     * Considering the non-realized cashflow profile, if the contingent date fell
     * within the interval, then the only cashflows deemed as non-realized would be
     * those due to occur from that contingent date onwards up until the interval's
     * end boundary. <br>
     * If, however, the contingent date fell before the start of the interval, all
     * cashflows over the interval would be deemed as non-realized and returned. <br>
     * Likewise, if the contingent date fell after the end of the interval, no
     * cashflow information would be deemed as non-realized for that interval and
     * nothing would be returned (empty cashflows).
     *
      */



        @SuppressWarnings("unused")

        private LocalDate contingentDate;

        private DateInterval dateInterval;

        public ContingentDateInterval(LocalDate contingentDate, DateInterval dateInterval) {
            super();
            this.contingentDate = contingentDate;
            this.dateInterval = dateInterval;
        }


        public ContingentDateInterval(
                 LocalDate contingentDate,
                 LocalDate startDate,
                 boolean startInclusive,
                 LocalDate endDate,
                 boolean endInclusive) {
            this(contingentDate, new DateInterval(startDate, startInclusive, endDate, endInclusive));
        }

        @SuppressWarnings("unused")
        private ContingentDateInterval() {
        }

        /**
         * Returns the contingentDate.
         */

        public final LocalDate getContingentDate() {
            return contingentDate;
        }

        /*
         * Persistence setter.
         */
        @SuppressWarnings("unused")
        private void setContingentDate(LocalDate contingentDate) {
            this.contingentDate = contingentDate;
        }

        /**
         * Returns the dateInterval.
         */

        public final DateInterval getDateInterval() {
            return dateInterval;
        }

        public final DateInterval getExpectedInterval() {
            DateInterval expectedInterval = DateInterval.getFromExclusive(contingentDate);
            return dateInterval.union(expectedInterval);
        }

        public final DateInterval getRealisedInterval() {
            DateInterval realisedInterval = DateInterval.getUpToInclusive(contingentDate);
            return dateInterval.union(realisedInterval);
        }

        @Override
        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Contingent Date = ");
            buffer.append(contingentDate);
            buffer.append(" Date Interval = ");
            buffer.append(dateInterval);
            return buffer.toString();
        }

        public ContingentDateInterval copy() {
            return new ContingentDateInterval(getContingentDate(), dateInterval.copy());
        }

        public ContingentDateInterval getRcfInterval(TradeFormFactory contract) {
            DateInterval interval = dateInterval.copy();
            if (contingentDate.isBefore(dateInterval.getStartDate())) {
                LocalDate transactionDate = contract.getTransactionInfo().getCaptureDate();
                interval = DateInterval.getUpToExclusive(transactionDate);
            } else if (dateInterval.getEndDate().isAfter(contingentDate)) {
                interval = new DateInterval(dateInterval.getStartDate(), dateInterval.isStartInclusive(), contingentDate,
                        true);
            }
            return new ContingentDateInterval(contingentDate, interval);
        }

        public ContingentDateInterval getEcfInterval(TradeFormFactory contract) {
            DateInterval interval = dateInterval.copy();
            if (contingentDate.isAfter(dateInterval.getEndDate())) {
                LocalDate transactionDate = contract.getTransactionInfo().getCaptureDate();
                interval = DateInterval.getUpToExclusive(transactionDate);
            } else if (dateInterval.getStartDate().isBefore(contingentDate)) {
                interval = new DateInterval(contingentDate, dateInterval.isStartInclusive(), dateInterval.getEndDate(),
                        true);
            }
            return new ContingentDateInterval(contingentDate, interval);
        }

    }

