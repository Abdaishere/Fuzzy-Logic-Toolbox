import FuzzySystem.Variable;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        Scanner sc = new Scanner(new File("input.txt"));
        int choice;
        String input;
        String system_name;
        String system_description;

        System.out.println("Fuzzy Logic Toolbox");
        System.out.println("===================");
        System.out.println("1- Create a new fuzzy system");
        System.out.println("2- Quit");

        choice = sc.nextInt();
        sc.nextLine();
        if (choice != 1) {
            return;
        }

        System.out.println("Enter the system’s name and a brief description:");
        System.out.println("------------------------------------------------");
        system_name = sc.nextLine();
        system_description = sc.nextLine();
        FuzzySystem fuzzySystem = new FuzzySystem();

        while(true){

            System.out.println("Main Menu:");
            System.out.println("==========");
            System.out.println("1- Add variables.");
            System.out.println("2- Add fuzzy sets to an existing variable.");
            System.out.println("3- Add rule.");
            System.out.println("4- Run the simulation on crisp values.");

            choice = sc.nextInt();
            sc.nextLine();
            if (choice == 1)
                addVariablesMenu(sc,fuzzySystem);
            else if (choice == 2)
                addFuzzySetToVariableMenu(sc,fuzzySystem);
            else if (choice == 3)
                addRule(sc,fuzzySystem);
            else if (choice == 4)
                runSimulation(sc,fuzzySystem);
            else
                break;
        }

    }

    private static void runSimulation(Scanner sc,FuzzySystem fuzzySystem) {
        System.out.println("Enter the crisp values:");
        System.out.println("-----------------------");
        for (Variable variable : fuzzySystem.getVariablesList()) {
            if(variable.type == Variable.VariableType.IN){
                System.out.print(variable.name + ": ");
                variable.value = sc.nextInt();
            }
        }
        sc.nextLine();

        fuzzySystem.Fuzzification();
        System.out.println("Fuzzification => done");
        fuzzySystem.Inference();
        System.out.println("Inference => done");
        fuzzySystem.Defuzzification();
        System.out.println("Defuzzification => done");
        for(Variable variable: fuzzySystem.getVariablesList()){
            if(variable.type == Variable.VariableType.OUT){
                System.out.printf("The predicted %s is %s (%.1f)%n",variable.name,variable.defuzzified_value,variable.value);
            }
        }
    }

    private static void addRule(Scanner sc,FuzzySystem fuzzySystem) {
        String input;
        System.out.println("Enter the rules in this format: (Press x to finish)\n" +
                "IN_variable set operator IN_variable set => OUT_variable set");
        System.out.println("------------------------------------------------------------");
        input = sc.nextLine();
        while (!input.equalsIgnoreCase("x")) {
            FuzzyRule rule = new FuzzyRule();
            String[] splitStr = input.split("\\s+");
            // TODO make sure that the in variable is of type IN and the out of type Out
            rule.c1 = new Condition(splitStr[0],splitStr[1]);
            rule.operator = FuzzyRule.FuzzyOperator.valueOf(splitStr[2].toUpperCase());
            rule.c2 = new Condition(splitStr[3],splitStr[4]);
            rule.output_variable = splitStr[6];
            rule.output_value = splitStr[7];
            fuzzySystem.addRule(rule);

            input = sc.nextLine();
        }
    }

    private static void addFuzzySetToVariableMenu(Scanner sc,FuzzySystem fuzzySystem) {
        String input;
        System.out.println("Enter the variable’s name:");
        System.out.println("--------------------------");
        input = sc.nextLine();

        if (fuzzySystem.hasVariable(input)) {
            System.out.println("Enter the fuzzy set name, type (TRI/TRAP) and values: (Press x to finish)");
            System.out.println("-----------------------------------------------------");
            Variable var = fuzzySystem.getVariable(input);
            FuzzySet fuzzySet = new FuzzySet();
            var.fuzzySets.add(fuzzySet);
            input = sc.nextLine();
            while (!input.equalsIgnoreCase("x")) {
                FuzzyValue fuzzyValue = new FuzzyValue();
                String[] splitStr = input.split("\\s+");
                fuzzyValue.name = splitStr[0];

                if (splitStr[1].equalsIgnoreCase("tri"))
                    fuzzyValue.type = FuzzyValue.FuzzyType.TRI;
                else
                    fuzzyValue.type = FuzzyValue.FuzzyType.TRAP;

                for(int i = 2; i < splitStr.length;i++)
                    fuzzyValue.range.add(Integer.parseInt(splitStr[i]));
                fuzzySet.values.add(fuzzyValue);
                input = sc.nextLine();
            }
        }
        else {
            System.out.println("No variable found");
        }
    }

    private static void addVariablesMenu(Scanner sc,FuzzySystem fuzzySystem) {
        String input;
        System.out.println("Enter the variable’s name, type (IN/OUT) and range ([lower, upper]):\n" +
                "(Press x to finish)");

        input = sc.nextLine();
        while (!input.equalsIgnoreCase("x")) {
            Variable v;
            String[] splitStr = input.split("\\s+");

            splitStr[2] = splitStr[2].replace("[","");
            splitStr[2] = splitStr[2].replace(",","");
            splitStr[3] = splitStr[3].replace("]","");
            int min = Integer.parseInt(splitStr[2]);
            int max = Integer.parseInt(splitStr[3]);

            if (splitStr[1].equalsIgnoreCase("in")) {
                v = new Variable(splitStr[0], min, max, Variable.VariableType.IN);
            }
            else {
                v = new Variable(splitStr[0], min, max, Variable.VariableType.OUT);
            }

            fuzzySystem.addVariable(v);
            input = sc.nextLine();
        }
    }


}


// proj_funding high or exp_level expert => risk low
