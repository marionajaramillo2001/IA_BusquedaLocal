
package comparticio;

import IA.Comparticion.Usuarios;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

public class Main {

    private static State executeHillClimbing(State initialState) throws Exception {
        Heuristic heuristic = new Heuristic();
        Problem problem = new Problem(initialState, new SuccesorsHC(), new GoalState(), heuristic);
        Search search = new HillClimbingSearch();
        SearchAgent agent = new SearchAgent(problem, search);
        return (State) search.getGoalState();
    }

    public static void main(String[] args){
        Usuarios users = new Usuarios(200, 100, 1234);
        State initialState = new State(users);

        System.out.println("Initial solution");
        System.out.println("Path distance: " + initialState.getTotalDistance());
        System.out.println("Drivers: " + initialState.getDrivers());
        System.out.println();

        try {
            State res = executeHillClimbing(initialState);
            System.out.println("Solution found with hill climbing");
            System.out.println("Path distance: " + res.getTotalDistance());
            System.out.println("Drivers: " + res.getDrivers());
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
