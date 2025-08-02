package com.ats.pricing.instruments;

import com.ats.pricing.enums.DayCountConventionType;
import com.ats.pricing.foundation.Currency;
import com.ats.pricing.foundation.Payment;
import com.ats.pricing.foundation.ReferenceTimePeriod;
import com.ats.pricing.foundation.TransactionInfo;
import com.ats.pricing.foundation.interestRate.LegType;
import com.ats.pricing.tools.Actual360;
import com.ats.pricing.tools.BusinessDayConvention;
import com.ats.pricing.tools.DayCountConvention;
import com.ats.pricing.tools.ModifiedFollowing;

import java.time.LocalDate;
import java.util.Iterator;

public class OvernightIndexSwap {

    public OvernightIndexSwap() {
    }

    private transient TransactionInfo transactionInfo;
    //private static final TimeOfDay NORMALIZED_TOD = TimeOfDay.SIX_IN_AFTERNOON;
    public static String INTEREST_STRING = "Interest";
    public static String NOTIONAL_STRING = "Notional";
    private LegType legType = LegType.PAYER;
    private double basisSpread = 0d;
    private ReferenceTimePeriod paymentLagTenor = ReferenceTimePeriod.getInstance("2D");
    private ReferenceTimePeriod fixingLagTenor = ReferenceTimePeriod.getInstance("0D");
    //private ReferenceIndex referenceIndex;
    private String referenceIndexId;
    private DayCountConvention referenceDayCount = new Actual360();
    //private OvernightRateType overnightRateType = OvernightRateType.DCON;
    private Boolean adjustForecastDates = false;
    private Boolean adjustAccrualDates = false;
    private Currency currency = Currency.getDefault();
    private DayCountConvention interestDayCount = new Actual360();
    //private BusinessCenterAssociation businessCenters;
    private LocalDate startDate = LocalDate.now();//DateUtils.setTimeOfDay(Util.today(), NORMALIZED_TOD);
    private ReferenceTimePeriod paymentFrequency = ReferenceTimePeriod.getInstance("1Y");
    private ReferenceTimePeriod tenor = ReferenceTimePeriod.getInstance("5Y");
    private LocalDate maturityDate = LocalDate.now().plusYears(5);//DateUtils.setTimeOfDay(Util.today(), NORMALIZED_TOD);
    private boolean tenorOrDate = true;
    private BusinessDayConvention businessDayConvention = ModifiedFollowing.instance;
    //private RollDirection rollDirection = RollDirection.BACKWARDS;
    //private StubConvention stubType = StubConvention.SHORT;
    private double notional = 1000000.0;
    private double initialNotionalExchange = 1000000.0;
    private double finalNotionalExchange = 1000000.0;
    private boolean isCustomSchedule = true;
    //private Set<OisCreditNoteCustomScheduleItem> customSchedule;

    /*public OvernightIndexSwap() {
        this.customSchedule = new ComponentTreeSet<OisCreditNoteCustomScheduleItem>(OisCreditNoteCustomScheduleItem.class);
        try {
            // getDefault() must be called prior to any other or else an exception is thrown
            referenceIndex = ReferenceIndex.getDefault();
            referenceIndex = ReferenceIndex.getInstance(FixingIndex.SOFR_DCON, ReferenceTimePeriod.getInstance("1D"));
        } catch (Exception ex) {
            referenceIndex = ReferenceIndex.getDefault();
        }

        try {
            businessCenters = BusinessCenterAssociation.newInstance(BusinessCenter.getDefault());
            businessCenters = BusinessCenterAssociation.newInstance(BusinessCenter.getInstance("USGS"));
        } catch (Exception ex) {
            businessCenters = BusinessCenterAssociation.newInstance(BusinessCenter.getDefault());
        }

        try {
            if (businessDayConvention != null && businessCenters != null) {
                // This should be one method call, but incorrect behaviour arises - a bug to investigate
                startDate = businessDayConvention.getAdjustedDate(startDate, businessCenters.getPrimaryCalendar(), businessCenters.getSecondaryCalendars());
                startDate = businessCenters.addBusinessDays(startDate, businessDayConvention, 2);
                maturityDate = new Date(startDate.getTime());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.enforceReferenceIndexId();
    }*/


    public Currency getContractBaseCurrency() {
        return currency;
    }


    /*public OvernightIndexSwapType getProductType() {
        return productType;
    }

    public void setProductType(OvernightIndexSwapType productType) {
        this.productType = productType;
    }


    public void setClnReferenceEntity(Counterparty clnReferenceEntity) {
        this.clnReferenceEntity = clnReferenceEntity;
    }*/

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LegType getLegType() {
        return legType;
    }

    public void setLegType(LegType legType) {
        this.legType = legType;
    }

    public double getBasisSpread() {
        return basisSpread;
    }

    public void setBasisSpread(double basisSpread) {
        this.basisSpread = basisSpread;
    }

    public ReferenceTimePeriod getPaymentLagTenor() {
        return paymentLagTenor;
    }

    public void setPaymentLagTenor(ReferenceTimePeriod paymentLagTenor) {
        this.paymentLagTenor = paymentLagTenor;
    }

    public ReferenceTimePeriod getFixingLagTenor() {
        return fixingLagTenor;
    }

    public void setFixingLagTenor(ReferenceTimePeriod fixingLagTenor) {
        this.fixingLagTenor = fixingLagTenor;
    }

    /*public ReferenceIndex getReferenceIndex() {
        enforceReferenceIndexObject();
        if (referenceIndex == null) {
            referenceIndex = ReferenceIndex.getDefault();
        }
        return referenceIndex;
    }

    public void setReferenceIndex(ReferenceIndex referenceIndex) {
        this.referenceIndex = referenceIndex;
        enforceReferenceIndexId();
    }*/

    public DayCountConvention getReferenceDayCount() {
        return referenceDayCount;
    }

    public void setReferenceDayCount(DayCountConvention referenceDayCount) {
        this.referenceDayCount = referenceDayCount;
    }

    /*public OvernightRateType getOvernightRateType() {
        return overnightRateType;
    }

    public void setOvernightRateType(OvernightRateType overnightRateType) {
        this.overnightRateType = overnightRateType;
    }*/

    public Boolean getAdjustForecastDates() {
        return adjustForecastDates;
    }

    public void setAdjustForecastDates(Boolean adjustForecastDates) {
        this.adjustForecastDates = adjustForecastDates;
    }

    public Boolean getAdjustAccrualDates() {
        return adjustAccrualDates;
    }

    public void setAdjustAccrualDates(Boolean adjustAccrualDates) {
        this.adjustAccrualDates = adjustAccrualDates;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public DayCountConvention getInterestDayCount() {
        return interestDayCount;
    }

    public void setInterestDayCount(DayCountConvention interestDayCount) {
        this.interestDayCount = interestDayCount;
    }

    /*public BusinessCenterAssociation getBusinessCenters() {
        return businessCenters;
    }

    public void setBusinessCenters(BusinessCenterAssociation businessCenters) {
        this.businessCenters = businessCenters;
    }*/

    public ReferenceTimePeriod getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(ReferenceTimePeriod paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public ReferenceTimePeriod getTenor() {
        return tenor;
    }

    public void setTenor(ReferenceTimePeriod tenor) {
        this.tenor = tenor;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public boolean isTenorOrDate() {
        return tenorOrDate;
    }

    public void setTenorOrDate(boolean tenorOrDate) {
        this.tenorOrDate = tenorOrDate;
    }

    public BusinessDayConvention getBusinessDayConvention() {
        return businessDayConvention;
    }

    public void setBusinessDayConvention(BusinessDayConvention businessDayConvention) {
        this.businessDayConvention = businessDayConvention;
    }

    /*public RollDirection getRollDirection() {
        return rollDirection;
    }

    public void setRollDirection(RollDirection rollDirection) {
        this.rollDirection = rollDirection;
    }

    public StubConvention getStubType() {
        return stubType;
    }

    public void setStubType(StubConvention stubType) {
        this.stubType = stubType;
    }*/

    public double getNotional() {
        return notional;
    }

    public void setNotional(double notional) {
        this.notional = notional;
    }

    public double getInitialNotionalExchange() {
        return initialNotionalExchange;
    }

    public void setInitialNotionalExchange(double initialNotionalExchange) {
        this.initialNotionalExchange = initialNotionalExchange;
    }

    public double getFinalNotionalExchange() {
        return finalNotionalExchange;
    }

    public void setFinalNotionalExchange(double finalNotionalExchange) {
        this.finalNotionalExchange = finalNotionalExchange;
    }

    public boolean isCustomSchedule() {
        return isCustomSchedule;
    }

    /*public void setCustomSchedule(boolean isCustomSchedule) {
        this.isCustomSchedule = isCustomSchedule;
    }

    public Set<OisCreditNoteCustomScheduleItem> getCustomSchedule() {
        return customSchedule;
    }

    public void setCustomSchedule(Set<OisCreditNoteCustomScheduleItem> customSchedule) {
        this.customSchedule = customSchedule;
    }*/


    public OvernightIndexSwap(TransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
        if (transactionInfo != null) {
            // TODO Temporary fix until all curves and matrices are entity
            // aware}

        }
    }


    public Payment getValue(LocalDate valueDate) {//}, OisCreditNoteContext context) throws MeasureException
        {
            Payment value = new Payment(0, this.getContractBaseCurrency());
            if (!valueDate.isBefore(getMaturityDate())) {
                return value;
            }
            //try {

            value = getCashFlowValue(valueDate);

            return value;
        }

        /* Assumes context cashflows have been initialized */
        private Payment getCashFlowValue (LocalDate valueDate){//throws MeasureException {
            double amount = 0;
            //get discounting curve
            //InterestRateSource discountingSource = context.getDiscountingSource(shifted);
            CashFlows expectedCashFlows = context.getECF();
            Iterator<CashFlow> iter = expectedCashFlows.iterator();
            while (iter.hasNext()) {
                CashFlow cashFlow = iter.next();
                amount += cashFlow.discount(valueDate, discountingSource).getAmount();
            }
            if (includeCapitalPayment) {
                /* TODO - assess impact of using unshifted risk-free for discounting */
                CashFlows capitalPaymentCashFlows = context.getCapitalPaymentECF();
                iter = capitalPaymentCashFlows.iterator();
                while (iter.hasNext()) {
                    CashFlow cashFlow = iter.next();
                    amount += cashFlow.discount(valueDate, discountingSource).getAmount();
                }
            }
            return new Payment(amount, getContractBaseCurrency());
        }

        public Payment getMarkToMarket (LocalDate valueDate){//, OisCreditNoteContext context) throws MeasureException {
            Payment value = new Payment(0, this.getContractBaseCurrency());
            if (!valueDate.before(getMaturityDate())) {
                return value;
            }
        }

        public CashFlows getExpectedCashFlows (LocalDate valueDate, DateInterval interval, OisCreditNoteContext context,
        boolean includeCapitalPayment) throws MeasureException {

            CashFlows cashFlows = getQTExpectedCashFlows(this, context, interval, valueDate);
            Collection<CashFlow> filteredCashFlows = new ArrayList<>();
            Iterator<CashFlow> iter = cashFlows.iterator();
            while (iter.hasNext()) {
                CashFlow cf = iter.next();
                if (!includeCapitalPayment && cf.getDate().equals(getStartDate()) && cf.isPrinciple()) {
                } else {
                    if ((cf.getPaymentAmount() != 0d)) {
                        filteredCashFlows.add(cf);
                    }
                }
            }
            return new CashFlows(filteredCashFlows);
        }

        public CashFlows getRealizedCashFlows (LocalDate valueDate, DateInterval interval, OisCreditNoteContext context) throws
        MeasureException {
            CashFlows cashFlows = getRealizedCashFlows(this, context, interval, valueDate);
            return cashFlows;
        }

    }
}




