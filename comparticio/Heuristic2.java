package comparticio;

import IA.Comparticion.Usuario;
import aima.search.framework.HeuristicFunction;

import java.util.Map;

public class Heuristic2 implements HeuristicFunction {

    private final State initialState;

    public Heuristic2(State initialState) {
        this.initialState = initialState;
    }

    @Override
    public double getHeuristicValue(Object state) {
        State s = (State)state;
        int maxlen = 0;

        for (Map.Entry<Usuario, Path> i : s.assignacioConductors.entrySet())
            maxlen = Math.max(maxlen, i.getValue().distancia);

        int scale = 1;

        if ((double)maxlen * 0.1 / 30 > 1.0)
            scale = 2;

        return scale * (s.getTotalDistance() + s.getDrivers() * initialState.getTotalDistance() / initialState.getDrivers());
    }
}
