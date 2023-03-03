package comparticio;
import IA.Comparticion.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ComparticioEstat {
    Usuarios usuaris;
    HashMap<Usuario, ArrayList<ComparticioAccio>> assignacioCoductors;

    public ComparticioEstat(Usuarios users)
    {
        usuaris = users;
        assignacioCoductors = new HashMap<Usuario, ArrayList<ComparticioAccio>>();
        generaSolucioInicial();
    }

    public ComparticioEstat(ComparticioEstat copia)
    {
        usuaris = (Usuarios)copia.usuaris.clone();
        assignacioCoductors = (HashMap<Usuario, ArrayList<ComparticioAccio>>)copia.assignacioCoductors.clone();
    }

    private void generaSolucioInicial()
    {
        ArrayList<Usuario> conductors = new ArrayList<>();
        ArrayList<Usuario> passatgers = new ArrayList<>();

        for (Usuario u : usuaris)
        {
            if (u.isConductor()) conductors.add(u);
            else passatgers.add(u);
        }
    }
}
