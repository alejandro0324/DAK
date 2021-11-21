package application.dak.DAK.backend.common.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StrategyPrice3 implements StrategyPrice {

    public Float KG;
    public Float KM;

    @Override
    public Float execute() {
        return null;
    }
}
