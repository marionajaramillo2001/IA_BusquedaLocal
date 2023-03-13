package comparticio;

import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class SuccesorsHC implements SuccessorFunction {
    @Override
    public List getSuccessors(Object state) {
        State estat = (State) state;
        List successors = new ArrayList();

        return successors;
    }
}
