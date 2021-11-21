package application.dak.DAK.backend.packages.services;

import application.dak.DAK.backend.common.models.*;

public class PackagesService {
    public Integer tripTax;
    public Float KG;
    public Float KM;
    private StrategyPrice strategyPrice;

    public String calculatePrice() {
        return strategyPrice.execute().toString();
    }

    public void setStrategy(Integer groupId) {
        switch (groupId) {
            case 1:
                this.strategyPrice = new StrategyPrice1(tripTax);
                break;
            case 2:
                this.strategyPrice = new StrategyPrice2(KG, tripTax);
                break;
            case 3:
                this.strategyPrice = new StrategyPrice3(KG, KM);
                break;
        }
    }
}
