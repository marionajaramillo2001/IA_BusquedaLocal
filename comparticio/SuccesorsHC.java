package comparticio;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class SuccesorsHC implements SuccessorFunction {
    @Override
    public List<Successor> getSuccessors(Object state) {
        State s = (State) state;
        ArrayList<Successor> successors = new ArrayList<>();

        // Operadors de girs dins un mateix cotxe
        s.swapPassengers(successors);
        s.unassignAloneDriver(successors);

        return successors;
    }
}
