package com.ats.pricing.operator;

import com.ats.pricing.scenario.ScenarioOperator;

public interface ScenarioOperation<T> {

    public T operate(ScenarioOperator operator);

}

