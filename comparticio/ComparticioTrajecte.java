package comparticio;

import java.util.ArrayList;

public class ComparticioTrajecte {
    public ArrayList<ComparticioAccio> trajecte;
    public int distancia;

    public ComparticioTrajecte()
    {
        trajecte = new ArrayList<>();
    }

    public void calculaDistancia()
    {
        distancia = 0;
        if (trajecte.size() < 2) return;
        Util.Pos prev = trajecte.get(0).posicio;

        for (int i = 1; i < trajecte.size(); ++i)
        {
            Util.Pos next = trajecte.get(i).posicio;
            distancia += Util.dist(prev, next);
            prev = next;
        }
    }
}
