package FuzzySystem;

public class Condition {
    String variable_name;
    String value;

    double check(Variable variable, String name) {
        return variable.getFuzzyValue(name);

    }

    public Condition(String name, String value) {
        this.variable_name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return variable_name + " " + value;
    }
}
