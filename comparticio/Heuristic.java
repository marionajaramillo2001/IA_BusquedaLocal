package comparticio;

import aima.search.framework.HeuristicFunction;

public class Heuristic implements HeuristicFunction {

    @Override
    public double getHeuristicValue(Object state) {
        State s = (State)state;
        return s.getTotalDistance();
    }
}
