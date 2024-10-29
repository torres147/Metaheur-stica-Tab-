
import java.io.File;
import java.util.Random;


public class Main {
    public static void main(String[] args) {

        Configurador conf = new Configurador(args[0]);
        Tiempos temporizador = new Tiempos();

        for(int i = 0; i < conf.getSemillas().size(); i++){
            Random semilla_actual = new Random(conf.getSemillas().get(i));
            for(int j = 0; j < conf.getArchivo().size(); j++){
                GestionaLog log = new GestionaLog(conf);
                log.cambioEjecucion(conf.getSemillas().get(i), conf.getArchivo().get(j));
                CargaDatos algoritmo = new CargaDatos(conf.getArchivo().get(j));


                log.registraLog("\n*************************** ALGORITMO GREEDY ALEATORIO ***************************************\n");
                log.registraLog("\n********************************************************************************************\n");
                AlgGreedyAleatorio_Clase02_Grupo01 greedy = new AlgGreedyAleatorio_Clase02_Grupo01(conf, algoritmo, semilla_actual, log);
                temporizador.comienza();
                greedy.ejecutar();
                temporizador.acaba();
                log.registraLog("\n Tiempo de ejecucion de Greedy Aleatorio: " + temporizador.getTotal() + "\n");
                log.registraLog("\n---Camino al final de Greedy:---\n");
                log.muestraCamino(greedy.getCamino_final());


                AlgLDM_Clase02_Grupo01 ldm = new AlgLDM_Clase02_Grupo01(greedy.getCamino_final(), greedy.getDistancia_total(), conf, algoritmo, semilla_actual, log);
                temporizador.comienza();
                ldm.ejecutar();
                temporizador.acaba();
                log.registraLog("\nTiempo de ejecucion de LDM: " + temporizador.getTotal() + "\n");
                log.registraLog("\n---Camino al final de LDM (" + ldm.getDistancia()  +")---\n");
                log.muestraCamino(ldm.getSolucion());



                AlgTabu_Clase02_Grupo01 tabu = new AlgTabu_Clase02_Grupo01(ldm.getSolucion(), ldm.getDistancia(), conf, algoritmo, semilla_actual, log);
                temporizador.comienza();
                tabu.ejecutar();
                temporizador.acaba();
                log.registraLog("\nTiempo de ejecucion de Tabu: " + temporizador.getTotal() + "\n");
                log.registraLog("\n---Camino al final de Tabu (" + tabu.getDistancia_global()  +")---\n");
                log.muestraCamino(tabu.s_global);

                log.escribeFichero(conf.getLogs() + File.separator + "_" + conf.getArchivo().get(j) + "[" + conf.getSemillas().get(i) + "].txt");
            }
        }


    }
}