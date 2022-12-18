package FuzzySystem;

import java.util.HashMap;

public class FuzzyRule {
    // proj_funding high or exp_level expert => risk low
    public Condition c1;
    public Condition c2;
    public FuzzyOperator operator;

    public String output_variable;
    public String output_value;

    public void apply(HashMap<String,Variable> variables) {
        double var1 = variables.get(c1.variable_name).getFuzzyValue(c1.value);
        double var2 = variables.get(c2.variable_name).getFuzzyValue(c2.value);
        double res;
        res = switch (operator){
            case AND -> Math.min(var1,var2);
            case OR -> Math.max(var1,var2);
            case AND_NOT -> Math.min(var1,1 - var2);
            case OR_NOT -> Math.max(var1,1 - var2);
        };
        variables.get(output_variable).addFuzzyValue(output_value,res);
    }

    public enum FuzzyOperator{
        AND,OR,AND_NOT,OR_NOT
    }

}
