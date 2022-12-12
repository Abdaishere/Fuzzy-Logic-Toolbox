package FuzzySystem;

public class Condition {
    double check(Variable variable, String name) {
        return variable.getFuzzyValue(name);

    }
}
