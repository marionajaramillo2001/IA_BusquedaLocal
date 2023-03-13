package comparticio;

import java.util.ArrayList;

public class Path {
    public ArrayList<Action> trajecte;
    public int distancia;

    public Path()
    {
        trajecte = new ArrayList<>();
    }

    public boolean canSwap(int i)
    {
        Action a1 = trajecte.get(i);
        Action a2 = trajecte.get(i + 1);

        if (a1.action == Action.DriverAction.DEIXA && a2.action == Action.DriverAction.RECULL)
            return a1.npassengers == 0;
        else if (a1.action == Action.DriverAction.RECULL && a2.action == Action.DriverAction.DEIXA)
            return a1.npassengers == 2;
        else
            return true;
    }

    public void swap(int i)
    {

    }

    public void calculaDistancia()
    {
        distancia = 0;
        if (trajecte.size() < 2) return;
        Util.Pos prev = trajecte.get(0).position;

        for (int i = 1; i < trajecte.size(); ++i)
        {
            Util.Pos next = trajecte.get(i).position;
            distancia += Util.dist(prev, next);
            prev = next;
        }
    }
}
