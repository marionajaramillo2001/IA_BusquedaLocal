package comparticio;

import java.util.ArrayList;
import java.util.Collections;

public class Path {
    public ArrayList<Action> trajecte;
    public int distancia;

    public Path()
    {
        trajecte = new ArrayList<>();
    }

    public Path(Path copy)
    {
        trajecte = new ArrayList<>();
        for (Action a : copy.trajecte)
            trajecte.add(new Action(a));
        distancia = copy.distancia;
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
        Action a0 = trajecte.get(i - 1);
        Action a1 = trajecte.get(i);
        Action a2 = trajecte.get(i + 1);
        Action a3 = trajecte.get(i + 2);

        if (a1.action == Action.DriverAction.DEIXA && a2.action == Action.DriverAction.RECULL)
        {
            a2.npassengers = 2;
            a1.npassengers = 1;
        }
        else if (a1.action == Action.DriverAction.RECULL && a2.action == Action.DriverAction.DEIXA)
        {
            a2.npassengers = 0;
            a1.npassengers = 1;
        }
        else
        {
            int t = a1.npassengers;
            a1.npassengers = a2.npassengers;
            a2.npassengers = t;
        }

        trajecte.set(i, a2);
        trajecte.set(i + 1, a1);

        int distdiff = Util.dist(a0.position, a2.position) + Util.dist(a1.position, a3.position)
                - Util.dist(a0.position, a1.position) - Util.dist(a2.position, a3.position);
        distancia += distdiff;
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
