package application.dak.DAK.backend.common.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StrategyPrice2 implements StrategyPrice {

    public Float KG;
    public Integer tripTax;

    @Override
    public double execute() {
        return (float) (tripTax * KG);
    }
}
