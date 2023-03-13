package comparticio;

import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class SuccesorsHC implements SuccessorFunction {
    @Override
    public List<State> getSuccessors(Object state) {
        State s = (State) state;
        ArrayList<State> successors = new ArrayList<>();

        // Operadors de girs dins un mateix cotxe
        s.swapPassengers(successors);

        return successors;
    }
}
