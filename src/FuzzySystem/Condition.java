package FuzzySystem;

public class Condition {
    String variable_name;
    String value;

    double check(Variable variable, String name) {
        return variable.getFuzzyValue(name);

    }
}
