
package comparticio;

import IA.Comparticion.Usuarios;

public class Comparticio {

    public static void main(String[] args){
        Usuarios usuaris = new Usuarios(100, 50, 435);
        ComparticioEstat estatInicial = new ComparticioEstat(usuaris);
        estatInicial.print();
    }
}
