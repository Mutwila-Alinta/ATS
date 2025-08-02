package com.ats.pricing.scenario;

import java.io.Serializable;

public abstract class ScenarioOperator implements Cloneable {

    //public abstract OperatorShiftType getShiftType();

    public Double getPerturbedValue(Double value, Double scenarioValue) {
        /*switch (getShiftType()) {
            case ABSOLUTE:
                value = value + scenarioValue;
                break;
            case VALUE:
                value = scenarioValue;
                break;
            case RELATIVE:
                value = value * scenarioValue;
                break;
            default:
                break;
        }*/
        return value;
    }

    /**
     * Enables a operator to perform any internal data transformations before
     * operation is executed
     *
     * @param operand
     * @param context TODO
     * @param context
     */
    public void transform(Object operand, String context) {

    }

    public abstract ScenarioOperator cloneOperator();

}
