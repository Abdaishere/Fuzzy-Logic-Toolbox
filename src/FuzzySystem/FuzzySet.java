package FuzzySystem;

import java.util.ArrayList;

public class FuzzySet {
    ArrayList<FuzzyValue> values = null;

    public void getValueOf(Variable variable) {
        int inputValue = variable.value;
        for (FuzzyValue val : values) {
            if (val.in(inputValue)) {
                variable.addFuzzyValue(val.name, val.fuzzify(inputValue));
            }
        }
    }
}
