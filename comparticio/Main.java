
package comparticio;

import IA.Comparticion.Usuarios;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

public class Main {

    private static State executeHillClimbing(State initialState) throws Exception {
        Heuristic heuristic = new Heuristic();
        Problem problem = new Problem(initialState, new SuccesorsHC(), new IsGoalState(), heuristic);
        Search search = new HillClimbingSearch();
        SearchAgent agent = new SearchAgent(problem, search);
        State result = (State) search.getGoalState();
        return result;
    }

    public static void main(String[] args){
        Usuarios usuaris = new Usuarios(100, 50, 435);
        State initialState = new State(usuaris);
        initialState.print();

        try {
            State res = executeHillClimbing(initialState);
            res.print();
            System.out.println("Path distance: " + res.getTotalDistance());
            System.out.println("Drivers: " + res.getDrivers());
        } catch (Exception e) {}
    }
}
