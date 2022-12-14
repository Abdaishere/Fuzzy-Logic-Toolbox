package FuzzySystem;

import java.util.ArrayList;

public class FuzzySet {
    public ArrayList<FuzzyValue> values = new ArrayList<>();

    public void fuzzify(Variable variable) {
        double inputValue = variable.value;
        for (FuzzyValue val : values) {
            variable.addFuzzyValue(val.name, val.fuzzify(inputValue));
        }
    }

    public void defuzzify(Variable variable){
        double[] values_average = new double[values.size()];
        double weight_products = 0;
        double weights_sum = 0;
        for(int i = 0; i < values_average.length;i++){
            values_average[i] = values.get(i).average();
            double value =  variable.getFuzzyValue(values.get(i).name);
            weight_products += value * values_average[i];
            weights_sum += value;
        }
        double z = weight_products/weights_sum;
        for(int i = 0;i < values_average.length; i++){
            if(values_average[i] < z){
                variable.defuzzified_value = values.get(i).name;
                variable.value = z;
                return;
            }
        }
    }
}
