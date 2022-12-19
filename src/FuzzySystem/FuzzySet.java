package FuzzySystem;

import java.util.ArrayList;

public class FuzzySet {

    public String name;

    public FuzzySet(String n) {
        name = n;
    }

    public ArrayList<FuzzyValue> values = new ArrayList<>();

    public void fuzzify(Variable variable) {
        double inputValue = variable.value;
        for (FuzzyValue val : values) {
            variable.addFuzzyValue(val.name, val.fuzzify(inputValue));
        }
    }

    public void defuzzify(Variable variable) {
        double[] values_average = new double[values.size()];
        double weight_products = 0;
        double weights_sum = 0;
        for (int i = 0; i < values_average.length; i++) {
            values_average[i] = values.get(i).average();
            double value = variable.getFuzzyValue(values.get(i).name);
            weight_products += value * values_average[i];
            weights_sum += value;
        }
        double z = Math.round((weight_products * 100.0) / weights_sum) / 100.0;
        double min_dist = Math.abs(values_average[0] - z);
        int min_indx = 0;
        for (int i = 1; i < values_average.length; i++) {
            double curr_dist = Math.abs(values_average[i] - z);
            if (min_dist >= curr_dist) {
                min_indx = i;
                min_dist = curr_dist;
            }
        }
        variable.defuzzified_value = values.get(min_indx).name;
        variable.value = z;
    }

    @Override
    public String toString() {
        return name;
    }
}
