package application.dak.DAK.backend.common.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StrategyPrice1 implements StrategyPrice {

    public Integer tripTax;

    @Override
    public Float execute() {
        return Float.valueOf(tripTax);
    }
}
