package comparticio;

import IA.Comparticion.Usuario;


public class Action {
    public enum DriverAction {
        RECULL, DEIXA
    }
    public DriverAction action;
    public Usuario user;
    public Util.Pos position;
    public int npassengers;

    public Action(DriverAction action, Usuario user, Util.Pos pos, int npassengers)
    {
        this.action = action;
        this.user = user;
        this.position = pos;
        this.npassengers = npassengers;
    }
}
