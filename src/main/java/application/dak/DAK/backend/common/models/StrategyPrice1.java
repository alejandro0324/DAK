package application.dak.DAK.backend.common.models;

import lombok.AllArgsConstructor;

import static application.dak.DAK.backend.utils.Constants.TAX;

@AllArgsConstructor
public class StrategyPrice1 implements StrategyPrice {

    @Override
    public double execute() {
        return Float.parseFloat(String.valueOf(TAX));
    }
}
