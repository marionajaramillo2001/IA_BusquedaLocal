package comparticio;

import IA.Comparticion.Usuario;

public class Util {

    public static class Pos
    {
        public int x;
        public int y;

        public Pos(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString()
        {
            return "(" + x + ", " + y + ")";
        }
    }

    public static int dist(int x1, int y1, int x2, int y2)
    {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public static int dist(Pos p1, Pos p2)
    {
        return dist(p1.x, p1.y, p2.x, p2.y);
    }

    public static Pos getOrigen(Usuario u)
    {
        return new Pos(u.getCoordOrigenX(), u.getCoordOrigenY());
    }

    public static Pos getDesti(Usuario u)
    {
        return new Pos(u.getCoordDestinoX(), u.getCoordDestinoY());
    }
}
