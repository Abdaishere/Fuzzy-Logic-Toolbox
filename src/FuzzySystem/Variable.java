package FuzzySystem;

import java.util.ArrayList;
import java.util.HashMap;

public class Variable {
    String name;
    private ArrayList<FuzzySet> FuzzySets;

    private HashMap<String, Double> fuzzyValues;

    int value;
    int min = 0, max = 1;
    VariableType type;

    public Variable(String name, int min, int max, VariableType type) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.type = type;
        fuzzyValues = new HashMap<>();
    }


    public void Fuzzification() {
        for (FuzzySet fs : FuzzySets) {
            fs.getValueOf(this);
        }
    }

    public String getName() {
        return this.name;
    }

    public void addFuzzyValue(String name, double value) {
        fuzzyValues.put(name, value);
    }

    public double getFuzzyValue(String name) {
        return fuzzyValues.get(name);
    }

    public enum VariableType {
        IN, OUT
    }
}
