package comparticio;
import IA.Comparticion.*;
import aima.search.framework.Successor;

import java.util.*;
import java.util.stream.Collectors;

public class State {
    Usuarios usuaris;
    HashMap<Usuario, Path> assignacioCoductors;

    public State(Usuarios users)
    {
        usuaris = users;
        assignacioCoductors = new HashMap<>();
        generaSolucioInicial1();
    }

    public State(State copy)
    {
        usuaris = copy.usuaris;
        assignacioCoductors = (HashMap<Usuario, Path>)copy.assignacioCoductors.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new Path(e.getValue())));
    }

    private void generaSolucioInicial1()
    {
        // Genera solucio inicial on assigna cada passatger sense cotxe a un conductor
        LinkedList<Usuario> passatgers = new LinkedList<>();

        for (Usuario u : usuaris)
        {
            if (u.isConductor()) {
                Path ct = new Path();
                ct.trajecte.add(new Action(Action.DriverAction.RECULL, u,
                        Util.getOrigen(u), 0));
                ct.trajecte.add(new Action(Action.DriverAction.DEIXA, u,
                        Util.getDesti(u), 0));
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
            Path trajecteEscollit = null;

            for (Map.Entry<Usuario, Path> set : assignacioCoductors.entrySet())
            {
                Usuario cond = set.getKey();
                Path traj = set.getValue();

                // Intentem recullir i deixar al final
                Util.Pos lastPos =
                        traj.trajecte.get(traj.trajecte.size() - 1).position;
                Util.Pos prevPos =
                        traj.trajecte.get(traj.trajecte.size() - 2).position;

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

            Action recull = new Action(Action.DriverAction.RECULL, pas,
                    Util.getOrigen(pas), 1);
            Action deixa = new Action(Action.DriverAction.DEIXA, pas,
                    Util.getDesti(pas), 0);

            trajecteEscollit.distancia += minDistIncrease;
            trajecteEscollit.trajecte.add(trajecteEscollit.trajecte.size() - 1, recull);
            trajecteEscollit.trajecte.add(trajecteEscollit.trajecte.size() - 1, deixa);

            passatgers.removeFirst();
        }
    }

    public int getTotalDistance() {
        int dist = 0;

        for (Map.Entry<Usuario, Path> set : assignacioCoductors.entrySet()) {
            Path traj = set.getValue();
            dist += traj.distancia;
        }

        return dist;
    }

    public int getDrivers() {
        return assignacioCoductors.size();
    }

    public void swapPassengers(ArrayList<Successor> states)
    {
        for (Map.Entry<Usuario, Path> set : assignacioCoductors.entrySet()) {
            Usuario driver = (Usuario)set.getKey();
            Path traj = set.getValue();
            for (int i = 1; i < traj.trajecte.size() - 2; ++i)
            {
                if (traj.canSwap(i))
                {
                    Action a1 = traj.trajecte.get(i);
                    Action a2 = traj.trajecte.get(i + 1);
                    String act = "Swapping " + a1.toString() + " with " +
                            a2.toString() + " in driver " + driver.toString();
                    traj.swap(i);
                    states.add(new Successor(act, new State(this)));
                    traj.swap(i);
                }
            }
        }
    }

    public void print()
    {
        for (Map.Entry<Usuario, Path> set : assignacioCoductors.entrySet()) {
            Usuario cond = set.getKey();
            Path traj = set.getValue();

            System.out.println("Conductor " + cond.toString() + ", distancia " + traj.distancia);

            for (Action accio : traj.trajecte)
            {
                System.out.println(accio.action + " passatger " + accio.user.toString() + " a la posició (" +
                        accio.position.x + ", " + accio.position.y + ")");
            }

            System.out.println();
        }
    }
}
