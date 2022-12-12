package FuzzySystem;

import java.util.ArrayList;

public class FuzzyValue {
    String name;
    ArrayList<Integer> range;
    FuzzyType type;

    public boolean in(int val) {
        return range.get(0) <= val && range.get(range.size() - 1) >= val;
    }

    public double fuzzify(int val){
        for(int i = 0; i < range.size(); i++){
            if(range.get(i) < val){
                if(type == FuzzyType.TRI){
                    return lineEquation(range.get(i),i%2, range.get(i - 1),(i-1)%2, val);
                }
                // TRAP
                if(i == 3)
                    return lineEquation(range.get(i),0, range.get(i - 1),1, val);
                else if(i == 2)
                    return lineEquation(range.get(i),1, range.get(i - 1),1, val);
                return lineEquation(range.get(i),1, range.get(i - 1),0, val);
            }
        }
        return 0.0;
    }

    private double lineEquation(int x1,int y1,int x2,int y2, int x){
        return ((y1-y2)/(double)(x1-x2))*(x - x1) + y1;
    }

    public enum FuzzyType {
        TRI, TRAP
    }
}
