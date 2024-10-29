import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Configurador {
    ArrayList<String> archivo;
    int k;
    ArrayList<Integer> semillas;
    double num_iteracionesLDM;
    double tam_entornoLDM;
    double disminucionLDM;
    double cada_cuantoLDM;
    double num_iteracionesTABU;
    double tam_entornoTABU;
    double disminucionTABU;
    double cada_cuantoTABU;
    double parametro_estancamiento;
    double tenencia_tabu;
    double impacto_de_largo_plazo;
    double oscilacion_estrategica;
    String logs;


    public Configurador(String ruta) {
        archivo = new ArrayList<>();
        semillas = new ArrayList<>();
        String linea = null;
        FileReader f = null;
        try{
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);

            try {
                linea = b.readLine();
            } catch (IOException ex) {
                Logger.getLogger(CargaDatos.class.getName()).log(Level.SEVERE, null, ex);
            }

            while(!linea.equals("EOF")){



                switch (linea){
                    case "Archivos":

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        while (!linea.equals("Fin_Archivos")){

                            String[] split = linea.trim().split(":");
                            archivo.add(split[0]);
                            try {
                                linea = b.readLine();
                            } catch (IOException ex) {
                                Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }

                    case "Parametros":

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        String[] split = linea.trim().split(" ");
                        k = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        num_iteracionesLDM = Integer.parseInt(split[1]);


                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        tam_entornoLDM = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        disminucionLDM = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        cada_cuantoLDM = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        num_iteracionesTABU = Integer.parseInt(split[1]);


                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        tam_entornoTABU = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        disminucionTABU = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        cada_cuantoTABU = Integer.parseInt(split[1]);


                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        parametro_estancamiento = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        tenencia_tabu = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        impacto_de_largo_plazo = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        oscilacion_estrategica= Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        logs= split[1];


                    case "Semillas":

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        while (!linea.equals("Fin_Semillas")){
                            semillas.add(Integer.parseInt(linea));
                            try {
                                linea = b.readLine();
                            } catch (IOException ex) {
                                Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                }


                try {
                    linea = b.readLine();
                } catch (IOException ex) {
                    Logger.getLogger(CargaDatos.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }catch (IOException e){
            System.out.println(e);
        }
    }

    public ArrayList<String> getArchivo() {
        return archivo;
    }


    public ArrayList<Integer> getSemillas() {
        return semillas;
    }

    public int getK() {
        return k;
    }

    public double getParametro_estancamiento() {
        return parametro_estancamiento;
    }

    public double getTenencia_tabu() {
        return tenencia_tabu;
    }

    public double getImpacto_de_largo_plazo() {
        return impacto_de_largo_plazo;
    }

    public double getNum_iteracionesLDM() {
        return num_iteracionesLDM;
    }

    public double getTam_entornoLDM() {
        return tam_entornoLDM;
    }

    public double getDisminucionLDM() {
        return disminucionLDM;
    }

    public double getCada_cuantoLDM() {
        return cada_cuantoLDM;
    }

    public double getNum_iteracionesTABU() {
        return num_iteracionesTABU;
    }

    public double getTam_entornoTABU() {
        return tam_entornoTABU;
    }

    public double getDisminucionTABU() {
        return disminucionTABU;
    }

    public double getCada_cuantoTABU() {
        return cada_cuantoTABU;
    }

    public double getOscilacion_estrategica() {
        return oscilacion_estrategica;
    }

    public String getLogs() {
        return logs;
    }
}


