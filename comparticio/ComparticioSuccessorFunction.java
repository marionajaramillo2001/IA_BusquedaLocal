package comparticio;

import IA.Comparticion.*;

import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class ComparticioSuccessorFunction implements SuccessorFunction {
    public List getSuccessors(Object state) {
        ComparticioEstat estat = (ComparticioEstat) state;
        List successors = new ArrayList();

        return successors;
    }
}
