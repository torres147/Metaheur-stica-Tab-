import java.util.Random;
import java.util.Vector;

public class AlgLDM_Clase02_Grupo01 {

    private Vector<Integer> solucion;
    private Double distancia;
    private Double[][] m_adyacencias;
    private Double numero_iteraciones;
    private Double tamanio_entorno;
    private Double disminucion;
    private Double cada_cuanto;
    private Integer num_datos;
    Random semilla;
    Configurador carga;
    CargaDatos datos;
    GestionaLog log;


    public AlgLDM_Clase02_Grupo01(Vector _solucion_inicial, double _distancia, Configurador configurador, CargaDatos datos, Random seed, GestionaLog log) {

        solucion = _solucion_inicial;
        distancia = _distancia;
        num_datos = _solucion_inicial.size();
        semilla = seed;
        carga = configurador;
        this.datos = datos;
        this.log = log;


        m_adyacencias = new Double[num_datos][num_datos];
        for(int i = 0; i < num_datos; i++){
            for(int j = 0; j < num_datos; j++){
                m_adyacencias[i][j] = datos.getDistancias()[i][j];
            }
        }

        numero_iteraciones = configurador.getNum_iteracionesLDM();
        tamanio_entorno = configurador.getTam_entornoLDM();
        disminucion = configurador.getDisminucionLDM();
        cada_cuanto = configurador.getCada_cuantoLDM();

    }

    public Vector<Integer> getSolucion() {
        return solucion;
    }

    public double getDistancia() {
        return distancia;
    }

    public Double[][] getM_adyacencias() {
        return m_adyacencias;
    }

    public double getNumero_iteraciones() {
        return numero_iteraciones;
    }

    public double getTamanio_entorno() {
        return tamanio_entorno;
    }

    public double getCada_cuanto() {
        return cada_cuanto;
    }

    public double getDisminucion() {
        return disminucion;
    }

    public int getNum_datos() {
        return num_datos;
    }

    void ejecutar() {
        log.registraLog("\n****************************************************************************\n");
        log.registraLog("\n************************** ALGORITMO BUSQUEDA LDM **************************\n");
        log.registraLog("\n****************************************************************************\n");
        double entorno = numero_iteraciones*(tamanio_entorno/100);
        int mejor_a = -1;
        int mejor_b = -1;
        double mejor_distancia = distancia;
        int a;
        int b;

        for(int i = 1; i < numero_iteraciones+1; i++){

            for(int j = 0; j < entorno; j++){
                do{
                    a = semilla.nextInt(num_datos);
                    b = semilla.nextInt(num_datos);
                }while(a == b);
                double vecino = Funciones_Auxiliares.dos_opt(a,b,solucion,distancia,m_adyacencias,num_datos, i);
                if(vecino < mejor_distancia) {
                    mejor_a = a;
                    mejor_b = b;
                    mejor_distancia = vecino;
                }
            }
            //Actualizar la solución con la mejor (A, B)
            if(distancia > mejor_distancia) {
                int ciudad_aux = solucion.get(mejor_a);
                solucion.set(mejor_a, solucion.get(mejor_b));
                solucion.set(mejor_b, ciudad_aux);
                distancia = mejor_distancia;
            }else{
                break;
            }

            if(i % (numero_iteraciones*(cada_cuanto/100)) == 0){
                entorno = entorno*((100-disminucion)/100);
            }


        }


    }

}
