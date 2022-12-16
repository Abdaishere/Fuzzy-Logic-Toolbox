package FuzzySystem;

import java.util.ArrayList;
import java.util.Arrays;

public class FuzzyValue {
    public String name;
    public ArrayList<Integer> range = new ArrayList<>();
    public FuzzyType type;

    public double fuzzify(double val){
        if(range.get(0) > val) return 0.0; // less than the range
        for(int i = 0; i < range.size(); i++){
            if(range.get(i) > val){
                if(type == FuzzyType.TRI){
                    return lineEquation(range.get(i),i%2, range.get(i - 1),(i-1)%2, val);
                }
                if(type == FuzzyType.TRAP){
                    switch (i){
                        case 3 -> { // between c and d
                            return lineEquation(range.get(i),0, range.get(i - 1),1, val);
                        }
                        case 2 -> { // between b and c
                            return lineEquation(range.get(i),1, range.get(i - 1),1, val);
                        }
                        case 1 -> { // between a and b
                            return lineEquation(range.get(i),1, range.get(0),0, val);
                        }
                    }
                }
            }
        }
        return 0.0; // more than the range
    }



    private double lineEquation(int x1,int y1,int x2,int y2, double x){
        return ((y1-y2)/(double)(x1-x2))*(x - x1) + y1;
    }

    public double average() {
        double sum = 0;
        for(int i : range) sum+= i;
        return sum/range.size();
    }


    public enum FuzzyType {
        TRI, TRAP
    }

    public static void main(String[] args) {
        FuzzyValue v = new FuzzyValue();
        v.range = new ArrayList<>(Arrays.asList(0,50,100,150));
        v.type = FuzzyType.TRAP;
        System.out.println(v.fuzzify(25));
        System.out.println(v.fuzzify(60));
        System.out.println(v.fuzzify(70));
        System.out.println(v.fuzzify(125));
        System.out.println(v.fuzzify(160));
        System.out.println(v.fuzzify(-20));

    }

    @Override
    public String toString() {
        return name;
    }
}
