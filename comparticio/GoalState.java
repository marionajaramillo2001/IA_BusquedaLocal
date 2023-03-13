package comparticio;

import aima.search.framework.GoalTest;

public class GoalState implements GoalTest {
    // Every solution is valid, we want to minimize the heuristic
    @Override
    public boolean isGoalState(Object state) {
        return false;
    }
}
