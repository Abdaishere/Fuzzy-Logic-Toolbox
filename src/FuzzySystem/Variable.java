package FuzzySystem;

import java.util.ArrayList;
import java.util.HashMap;

public class Variable {
    public String name;
    public ArrayList<FuzzySet> fuzzySets = new ArrayList<>();
    public HashMap<String, Double> fuzzyValues;

    public double value;
    public String defuzzified_value;
    public int min, max;
    public VariableType type;

    public Variable(String name, int min, int max, VariableType type) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.type = type;
        fuzzyValues = new HashMap<>();
    }


    public void Fuzzification() {
        for (FuzzySet fs : fuzzySets) {
            fs.fuzzify(this);
        }
    }

    public void Defuzzification() {
        for (FuzzySet fs : fuzzySets) {
            fs.defuzzify(this);
        }
    }

    public void addFuzzyValue(String name, double value) {
        fuzzyValues.put(name, Math.max(fuzzyValues.getOrDefault(name,0.0),value));
    }

    public double getFuzzyValue(String name) {
        return fuzzyValues.get(name);
    }



    public enum VariableType {
        IN, OUT
    }

    @Override
    public String toString() {
        return name;
    }
}
