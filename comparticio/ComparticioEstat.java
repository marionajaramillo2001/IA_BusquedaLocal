package comparticio;
import IA.Comparticion.*;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

public class ComparticioEstat {
    Usuarios usuaris;
    HashMap<Usuario, ComparticioTrajecte> assignacioCoductors;

    public ComparticioEstat(Usuarios users)
    {
        usuaris = users;
        assignacioCoductors = new HashMap<>();
        generaSolucioInicial1();
    }

    public ComparticioEstat(ComparticioEstat copia)
    {
        usuaris = copia.usuaris;
        assignacioCoductors = new HashMap<>(copia.assignacioCoductors);
    }

    private void generaSolucioInicial1()
    {
        // Genera solucio inicial on assigna cada passatger sense cotxe a un conductor
        LinkedList<Usuario> passatgers = new LinkedList<>();

        for (Usuario u : usuaris)
        {
            if (u.isConductor()) {
                ComparticioTrajecte ct = new ComparticioTrajecte();
                ct.trajecte.add(new ComparticioAccio(Accio.RECULL, u, Util.getOrigen(u)));
                ct.trajecte.add(new ComparticioAccio(Accio.DEIXA, u, Util.getDesti(u)));
                ct.calculaDistancia();
                assignacioCoductors.put(u, ct);
            }
            else passatgers.add(u);
        }

        while (!passatgers.isEmpty())
        {
            Usuario pas = passatgers.getFirst();
            int pasDist = Util.dist(Util.getOrigen(pas), Util.getDesti(pas));
            int minDistIncrease = Integer.MAX_VALUE;
            ComparticioTrajecte trajecteEscollit = null;

            for (Map.Entry<Usuario, ComparticioTrajecte> set : assignacioCoductors.entrySet())
            {
                Usuario cond = set.getKey();
                ComparticioTrajecte traj = set.getValue();

                // Intentem recullir i deixar al final
                Util.Pos lastPos = traj.trajecte.get(traj.trajecte.size() - 1).posicio;
                Util.Pos prevPos = traj.trajecte.get(traj.trajecte.size() - 2).posicio;

                int dist = traj.distancia - Util.dist(lastPos, prevPos) +
                        Util.dist(prevPos, Util.getOrigen(pas)) + pasDist;

                double time = 0.1 * (double)dist / 30.0;

                if (time <= 1.0)
                {
                    if (dist - traj.distancia < minDistIncrease)
                    {
                        minDistIncrease = dist - traj.distancia;
                        trajecteEscollit = traj;
                    }
                }
            }

            if (trajecteEscollit == null)
            {
                System.out.println("No hem pogut assignar el passatger " + pas);
                return;
            }

            ComparticioAccio recull = new ComparticioAccio(Accio.RECULL, pas, Util.getOrigen(pas));
            ComparticioAccio deixa = new ComparticioAccio(Accio.DEIXA, pas, Util.getDesti(pas));

            trajecteEscollit.distancia += minDistIncrease;
            trajecteEscollit.trajecte.add(trajecteEscollit.trajecte.size() - 1, recull);
            trajecteEscollit.trajecte.add(trajecteEscollit.trajecte.size() - 1, deixa);

            passatgers.removeFirst();
        }
    }

    public void print()
    {
        for (Map.Entry<Usuario, ComparticioTrajecte> set : assignacioCoductors.entrySet()) {
            Usuario cond = set.getKey();
            ComparticioTrajecte traj = set.getValue();

            System.out.println("Conductor " + cond.toString() + ", distancia " + traj.distancia);

            for (ComparticioAccio accio : traj.trajecte)
            {
                System.out.println(accio.accio + " passatger " + accio.usuari.toString() + " a la posició (" +
                        accio.posicio.x + ", " + accio.posicio.y + ")");
            }

            System.out.println();
        }
    }
}
