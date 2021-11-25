package application.dak.DAK.backend.common.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StrategyPrice3 implements StrategyPrice {

    public Float KG;
    public Float KM;
    public Integer tripTax;

    @Override
    public double execute() {
        return tripTax * KM * 0.25;
    }
}
