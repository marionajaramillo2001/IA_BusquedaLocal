package comparticio;

import java.util.ArrayList;

public class Path {
    public ArrayList<Action> trajecte;
    public int distancia;

    public Path()
    {
        trajecte = new ArrayList<>();
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
