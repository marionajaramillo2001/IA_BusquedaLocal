package comparticio;

import IA.Comparticion.Usuario;

public class ComparticioAccio {
    public Accio accio;
    public Usuario usuari;
    public Util.Pos posicio;

    public ComparticioAccio(Accio accio, Usuario usuari, Util.Pos pos)
    {
        this.accio = accio;
        this.usuari = usuari;
        this.posicio = pos;
    }
}
