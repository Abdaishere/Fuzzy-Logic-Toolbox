import FuzzySystem.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HashMap<String ,Variable> variables = new HashMap<String, Variable>();;

        Scanner sc = new Scanner(System.in);
        int choice;
        String input;
        String system_name;
        String system_description;

        System.out.println("Fuzzy Logic Toolbox");
        System.out.println("===================");
        System.out.println("1- Create a new fuzzy system");
        System.out.println("2- Quit");

        choice = sc.nextInt();

        if (choice != 1) {
            return;
        }

        System.out.println("Enter the system’s name and a brief description:");
        System.out.println("------------------------------------------------");
        system_name = sc.nextLine();
        system_description = sc.nextLine();


        System.out.println("Main Menu:");
        System.out.println("==========");
        System.out.println("1- Add variables.");
        System.out.println("2- Add fuzzy sets to an existing variable.");
        System.out.println("3- Add rule.");
        System.out.println("4- Run the simulation on crisp values.");

        choice = sc.nextInt();

        if(choice == 1) {
            System.out.println("Enter the variable’s name, type (IN/OUT) and range ([lower, upper]):\n" +
                                "(Press x to finish)");

            input = sc.nextLine();
            while (!input.equalsIgnoreCase("x")) {
                Variable v;
                String[] splitStr = input.split("\\s+");
                String min = "", max = "";
                int flag = 0;

                for(int i = 1; i < splitStr[2].length(); i++) {
                    
                    if(Character.isDigit(splitStr[2].charAt(i)) && flag == 0) {
                        min += splitStr[2].charAt(i);
                    }
                    else if (Character.isDigit(splitStr[2].charAt(i)) && flag == 1) {
                        max += splitStr[2].charAt(i);
                    }
                    else {
                        flag = 1;
                    }
                }

                if(splitStr[1].equalsIgnoreCase("in")) {
                    v = new Variable(splitStr[0], Integer.parseInt(min), Integer.parseInt(max), Variable.VariableType.IN);
                }

                else {
                    v = new Variable(splitStr[0], Integer.parseInt(min), Integer.parseInt(max), Variable.VariableType.OUT);
                }

                variables.put(v.getName(), v);
                input = sc.nextLine();
            }

        } else if (choice == 2) {

            input = sc.nextLine();
            System.out.println("Enter the variable’s name:");
            System.out.println("--------------------------");

            if(variables.containsKey(input)){
                System.out.println("Enter the fuzzy set name, type (TRI/TRAP) and values: (Press x to finish)");
                System.out.println("-----------------------------------------------------");
                input = sc.nextLine();
                while (!input.equalsIgnoreCase("x")) {

                    input = sc.nextLine();
                }

            }else {
                System.out.println("No variable found");
            }



            
        } else if (choice == 3) {
            System.out.println("Enter the rules in this format: (Press x to finish)\n" +
                    "IN_variable set operator IN_variable set => OUT_variable set");
            System.out.println("------------------------------------------------------------");
            input = sc.nextLine();
            while (!input.equalsIgnoreCase("x")) {

                input = sc.nextLine();
            }
            
        } else if (choice == 4) {
            System.out.println("Enter the crisp values:");
            System.out.println("-----------------------");

        }


    }

}

// proj_funding high or exp_level expert => risk low
