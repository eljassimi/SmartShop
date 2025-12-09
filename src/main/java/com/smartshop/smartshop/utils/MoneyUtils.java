package com.smartshop.smartshop.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MoneyUtils {

    public double round(double value) {
        return Math.round(value * 100.0d) / 100.0d;
    }
}

