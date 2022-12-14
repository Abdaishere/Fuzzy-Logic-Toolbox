package FuzzySystem;

import java.util.ArrayList;
import java.util.HashMap;

public class FuzzySystem {
     HashMap<String ,Variable> variables = new HashMap<>();
     ArrayList<FuzzyRule> rules = new ArrayList<>();

     public void addRule(FuzzyRule rule){
         // TODO we may add rule check
         rules.add(rule);
     }

    public ArrayList<Variable> getVariablesList(){
         return new ArrayList<>(variables.values());
    }

    public boolean hasVariable(String name){
         return variables.containsKey(name);
    }

    public Variable getVariable(String name){
         return variables.get(name);
    }

    public void addVariable(Variable variable){
         variables.put(variable.name,variable);
    }
    /*
     * Uses Hashmap of the variables and apply the fuzzification to it
     */
    public void Fuzzification(){
        for (Variable variable : variables.values()) {
            variable.Fuzzification();
        }
    }

    public  void Inference() {
        for(FuzzyRule rule:rules){
            rule.apply(variables);
        }
    }
    /*
     * This will also apply the Defuzzification
     */
    public void Defuzzification() {
        for (Variable variable : variables.values()) {
            if(variable.type == Variable.VariableType.OUT)
                variable.Defuzzification();
        }

    }
}
