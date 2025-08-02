package com.ats.pricing.instrumentsFactory;

import com.ats.pricing.foundation.TransactionInfo;

public interface InstrumentFactoryGUIBase {
    //void display();
    public Object getInstrumentInstance(TransactionInfo info);
}
