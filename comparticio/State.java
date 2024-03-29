package comparticio;
import IA.Comparticion.*;
import aima.search.framework.Successor;
import aima.util.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class State {
    Usuarios usuaris;
    HashMap<Usuario, Path> assignacioConductors;

    public State(Usuarios users, int ini)
    {
        usuaris = users;
        assignacioConductors = new HashMap<>();

        if (ini == 0) generaSolucioInicial1();
        else if (ini == 1) generaSolucioInicial2();
    }

    public State(State copy)
    {
        usuaris = copy.usuaris;
        assignacioConductors = (HashMap<Usuario, Path>)copy.assignacioConductors.entrySet().stream()
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
                // Action s'inicialitza amb npassengers a 0 perquè no són
                // passatgers, són conductors
                ct.trajecte.add(new Action(Action.DriverAction.RECULL, u, Util.getOrigen(u), 0));
                ct.trajecte.add(new Action(Action.DriverAction.DEIXA, u, Util.getDesti(u), -1));
                ct.calculaDistancia();
                assignacioConductors.put(u, ct); // cada conductor genera un
                // path (o assignació de passatgers a conductors associada al
                // path)
            }
            else passatgers.add(u);
        }

        while (!passatgers.isEmpty())
        {
            Usuario pas = passatgers.getFirst();
            int pasDist = Util.dist(Util.getOrigen(pas), Util.getDesti(pas));
            int minDistIncrease = Integer.MAX_VALUE;
            Path trajecteEscollit = null;

            double maxtime = 1.0;
            double increaseOutOftime = Double.MAX_VALUE;
            Path trajecteForaTemps = null;
            int distForaTemps = 0;

            for (Map.Entry<Usuario, Path> set : assignacioConductors.entrySet())
            {
                Usuario cond = set.getKey();
                Path traj = set.getValue();

                // Intentem recollir i deixar al final
                Util.Pos lastPos = traj.trajecte.get(traj.trajecte.size() - 1).position;
                Util.Pos prevPos = traj.trajecte.get(traj.trajecte.size() - 2).position;

                int dist = traj.distancia - Util.dist(lastPos, prevPos) +
                        Util.dist(prevPos, Util.getOrigen(pas)) + pasDist +
                        Util.dist(Util.getDesti(pas),lastPos);

                double time = 0.1 * (double)dist / 30.0;

                if (time <= 1.0)
                {
                    if (dist - traj.distancia < minDistIncrease)
                    {
                        minDistIncrease = dist - traj.distancia;
                        trajecteEscollit = traj;
                    }
                }
                else
                {
                    maxtime = Math.max(maxtime, time);
                    double distIncrease = maxtime - 1.0 + dist - traj.distancia;
                    if (distIncrease < increaseOutOftime)
                    {
                        increaseOutOftime = distIncrease;
                        distForaTemps = dist - traj.distancia;
                        trajecteForaTemps = traj;
                    }
                }
            }

            if (trajecteEscollit == null )
            {
                trajecteEscollit = trajecteForaTemps;
                minDistIncrease = distForaTemps;
                if (trajecteEscollit == null) {
                    System.out.println("No hem pogut assignar el passatger " + pas);
                    return;
                }
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

    private void generaSolucioInicial2()
    {
        // Genera solucio inicial on assigna cada passatger sense cotxe a un conductor
        LinkedList<Usuario> passatgers = new LinkedList<>();

        for (Usuario u : usuaris)
        {
            if (u.isConductor()) {
                Path ct = new Path();
                // Action s'inicialitza amb npassengers a 0 perquè no són
                // passatgers, són conductors
                ct.trajecte.add(new Action(Action.DriverAction.RECULL, u, Util.getOrigen(u), 0));
                ct.trajecte.add(new Action(Action.DriverAction.DEIXA, u, Util.getDesti(u), -1));
                ct.calculaDistancia();
                assignacioConductors.put(u, ct); // cada conductor genera un
                // path (o assignació de passatgers a conductors associada al
                // path)
            }
            else passatgers.add(u);
        }

        while (!passatgers.isEmpty())
        {
            Usuario pas = passatgers.getFirst();
            Util.Pos porigin = Util.getOrigen(pas);
            Util.Pos pdestiny = Util.getDesti(pas);
            int minDistIncrease = Integer.MAX_VALUE;
            Path trajecteEscollit = null;
            int recullPos = -1;
            int deixaPos = -1;

            double maxtime = 1.0;
            double increaseOutOftime = Double.MAX_VALUE;
            Path trajecteForaTemps = null;
            int distForaTemps = 0;
            int recullForaTemps = -1;
            int deixaForaTemps = -1;

            for (Map.Entry<Usuario, Path> set : assignacioConductors.entrySet())
            {
                Usuario cond = set.getKey();
                Path traj = set.getValue();

                // Probem tots els llocs possibles on recollir/deixar
                for (int i = 1; i < traj.trajecte.size(); ++i)
                {
                   Action sprev = traj.trajecte.get(i - 1);
                   Action snext = traj.trajecte.get(i);

                   for (int j = i; j < traj.trajecte.size(); ++j)
                   {
                       Action eprev = traj.trajecte.get(j - 1);
                       Action enext = traj.trajecte.get(j);
                       if (eprev.npassengers == 2) break;

                       int distInc = 0;

                       if (i == j)
                           distInc = Util.dist(porigin, pdestiny) + Util.dist(sprev.position, porigin)
                                   + Util.dist(snext.position, pdestiny) - Util.dist(sprev.position, snext.position);
                       else
                           distInc = Util.dist(sprev.position, porigin) + Util.dist(porigin, snext.position)
                                   - Util.dist(sprev.position, snext.position) + Util.dist(eprev.position, pdestiny)
                                   + Util.dist(pdestiny, enext.position) - Util.dist(eprev.position, enext.position);

                       int dist = traj.distancia + distInc;

                       double time = 0.1 * (double)dist / 30.0;

                       if (time <= 1.0)
                       {
                           if (dist - traj.distancia < minDistIncrease)
                           {
                               minDistIncrease = distInc;
                               trajecteEscollit = traj;
                               recullPos = i;
                               deixaPos = j;
                           }
                       }
                       else
                       {
                           maxtime = Math.max(maxtime, time);
                           double distIncrease = maxtime - 1.0 + dist - traj.distancia;
                           if (distIncrease < increaseOutOftime)
                           {
                               increaseOutOftime = distIncrease;
                               distForaTemps = dist - traj.distancia;
                               trajecteForaTemps = traj;
                               recullForaTemps = i;
                               deixaForaTemps = j;
                           }
                       }
                   }
                }
            }

            if (trajecteEscollit == null )
            {
                trajecteEscollit = trajecteForaTemps;
                minDistIncrease = distForaTemps;
                recullPos = recullForaTemps;
                deixaPos = deixaForaTemps;
                if (trajecteEscollit == null) {
                    System.out.println("No hem pogut assignar el passatger " + pas);
                    return;
                }
            }

            Action sprev = trajecteEscollit.trajecte.get(recullPos - 1);
            Action eprev = trajecteEscollit.trajecte.get(deixaPos - 1);

            Action recull = new Action(Action.DriverAction.RECULL, pas, Util.getOrigen(pas), sprev.npassengers + 1);
            Action deixa = new Action(Action.DriverAction.DEIXA, pas, Util.getDesti(pas), eprev.npassengers);

            for (int i = recullPos; i < deixaPos; ++i)
                ++trajecteEscollit.trajecte.get(i).npassengers;

            trajecteEscollit.distancia += minDistIncrease;
            trajecteEscollit.trajecte.add(deixaPos, deixa);
            trajecteEscollit.trajecte.add(recullPos, recull);

            passatgers.removeFirst();
        }
    }

    public int getTotalDistance() {
        int dist = 0;

        for (Map.Entry<Usuario, Path> set : assignacioConductors.entrySet()) {
            Path traj = set.getValue();
            dist += traj.distancia;
        }
        return dist;
    }

    public int getDrivers() {
        return assignacioConductors.size();
    }

    public void swapPassengers(ArrayList<Successor> states)
    {
        for (Map.Entry<Usuario, Path> set : assignacioConductors.entrySet()) {
            Usuario driver = (Usuario)set.getKey();
            Path traj = set.getValue();
            for (int i = 1; i < traj.trajecte.size() - 2; ++i)
            {
                if (traj.canSwap(i))
                {
                    Action a1 = traj.trajecte.get(i);
                    Action a2 = traj.trajecte.get(i + 1);
                    State newstate = new State(this);
                    newstate.assignacioConductors.get(driver).swap(i);
                    String act = "S";
                    states.add(new Successor(act, newstate));
                }
            }
        }
    }

    public void unassignAloneDriver(ArrayList<Successor> states) {
        for (Map.Entry<Usuario, Path> possibleAloneDriver : assignacioConductors.entrySet())
        {
            Usuario driver = (Usuario)possibleAloneDriver.getKey();
            Path trajPossibleAloneDriver = new Path(possibleAloneDriver.getValue());

            if (trajPossibleAloneDriver.trajecte.size() == 2) {
                int driverDist = Util.dist(Util.getOrigen(driver), Util.getDesti(driver));

                for (Map.Entry<Usuario, Path> set : assignacioConductors.entrySet()) {
                    Usuario nextDriver = set.getKey();
                    if (nextDriver != driver) {
                        Path traj = set.getValue();

                        // Intentem recollir i deixar al final
                        Util.Pos lastPos = traj.trajecte.get(traj.trajecte.size() - 1).position;
                        Util.Pos prevPos = traj.trajecte.get(traj.trajecte.size() - 2).position;

                        int dist = traj.distancia - Util.dist(lastPos, prevPos) +
                                Util.dist(prevPos, Util.getOrigen(driver)) + driverDist +
                                Util.dist(Util.getDesti(driver), lastPos);

                        Action recull = new Action(Action.DriverAction.RECULL, driver,
                                Util.getOrigen(driver), 1);
                        Action deixa = new Action(Action.DriverAction.DEIXA, driver,
                                Util.getDesti(driver), 0);

                        State newstate = new State(this);
                        Path newtrajecte = newstate.assignacioConductors.get(nextDriver);

                        newtrajecte.distancia = dist;
                        newtrajecte.trajecte.add(newtrajecte.trajecte.size() - 1, recull);
                        newtrajecte.trajecte.add(newtrajecte.trajecte.size() - 1, deixa);

                        newstate.assignacioConductors.remove(driver);

                        String act = "U";
                        states.add(new Successor(act, newstate));
                    }
                }
            }
        }
    }

    public void mourePassatgers(ArrayList<Successor> states) {
        for (Map.Entry<Usuario, Path> set : assignacioConductors.entrySet()) {
            Usuario conductor = set.getKey();
            Path ruta = set.getValue();
            HashMap<Usuario, Integer> usuarisRecullPos = new HashMap<>();
            HashMap<Usuario, Integer> usuarisDeixaPos = new HashMap<>();

            for (int i = 1; i <= ruta.trajecte.size() - 2; ++i) {
                Action act = ruta.trajecte.get(i);
                Usuario u = act.user;
                if (act.action == Action.DriverAction.RECULL) usuarisRecullPos.put(u, i);
                else if (act.action == Action.DriverAction.DEIXA) usuarisDeixaPos.put(u, i);
            }

            for (Map.Entry<Usuario, Path> set2 : assignacioConductors.entrySet()) {
                Usuario conductor2 = set2.getKey();
                Path ruta2 = set2.getValue();

                if (conductor2 != conductor) {
                    for(Map.Entry<Usuario, Integer> set3 : usuarisRecullPos.entrySet()) {

                        Usuario passatger = set3.getKey();
                        int posRecullTrajecteIni =  usuarisRecullPos.get(passatger);
                        int posDeixaTrajecteIni = usuarisDeixaPos.get(passatger);

                        int distPassatger = Util.dist(Util.getOrigen(passatger), Util.getDesti(passatger));

                        for (int i = 1; i <= ruta2.trajecte.size() - 2; ++i) {
                            if (ruta2.trajecte.get(i - 1).npassengers < 2) {
                                Util.Pos curPos = ruta2.trajecte.get(i).position;
                                Util.Pos prevPos = ruta2.trajecte.get(i - 1).position;

                                Action recull = new Action(
                                        Action.DriverAction.RECULL, passatger, Util.getOrigen(passatger), ruta2.trajecte.get(i - 1).npassengers + 1);
                                Action deixa = new Action(
                                        Action.DriverAction.DEIXA, passatger, Util.getDesti(passatger), ruta2.trajecte.get(i - 1).npassengers);

                                State newState = new State(this);
                                Path newRuta = newState.assignacioConductors.get(conductor);
                                Path newRuta2 = newState.assignacioConductors.get(conductor2);
                                newRuta2.add(i, deixa);
                                newRuta2.add(i, recull);

                                for (int j = posRecullTrajecteIni + 1; j < posDeixaTrajecteIni; ++j)
                                    --newRuta.trajecte.get(j).npassengers;

                                newRuta.remove(posDeixaTrajecteIni);
                                newRuta.remove(posRecullTrajecteIni);
                                String act = "M";

                                states.add(new Successor(act, newState));
                            }
                        }
                    }
                }
            }
        }
    }

    public void print()
    {
        for (Map.Entry<Usuario, Path> set : assignacioConductors.entrySet()) {
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
