import java.util.*;


public class AlgTabu_Clase02_Grupo01 {
    Vector<Integer> s_actual;
    Vector<Integer> s_global;
    Vector<Integer> s_mejor_actual;
    private Double distancia_actual;
    private Double distancia_global;
    private Double distancia_mejor_actual;
    private Double m_distancia_actual;
    private Double[][] m_adyacencias;
    private Double numero_iteraciones;
    private Integer num_datos;
    Random semilla;
    private Integer[][] memoria_dinamica;
    private Double tamanio_entorno;
    private Double cada_cuanto;
    private Double entorno;
    private Double disminucion;
    private Double param_estancamiento;
    private Double tenencia_tabu;
    private Double impacto_de_largo_plazo;
    private Integer k;
    private Double oscilacion_estrategica;
    CargaDatos datos;
    Configurador config;
    int pivote_tabu = 0;
    boolean tabu_lleno = false;
    GestionaLog log;

    public AlgTabu_Clase02_Grupo01(Vector<Integer> inicio, double dist, Configurador configurador, CargaDatos datos, Random seed, GestionaLog log) {
        s_actual = inicio;
        s_global = inicio;
        s_mejor_actual = inicio;
        num_datos = inicio.size();
        semilla = seed;
        distancia_actual = dist;
        distancia_global = dist;
        distancia_mejor_actual = dist;
        m_distancia_actual = Double.MAX_VALUE;
        m_adyacencias = datos.getDistancias();
        numero_iteraciones = configurador.getNum_iteracionesTABU();
        tamanio_entorno = numero_iteraciones*((configurador.getTam_entornoTABU())/100);
        cada_cuanto = configurador.getCada_cuantoTABU();
        disminucion = configurador.getDisminucionTABU();
        param_estancamiento = numero_iteraciones*(configurador.getParametro_estancamiento()/100);
        tenencia_tabu = num_datos*((configurador.getTenencia_tabu())/100);
        impacto_de_largo_plazo = num_datos*(configurador.getImpacto_de_largo_plazo()/100);
        k = configurador.getK();
        oscilacion_estrategica = configurador.getOscilacion_estrategica();
        config = configurador;
        this.datos = datos;
        this.log = log;

        for(int i = 0; i < num_datos; i++){
            for(int j = 0; j < num_datos; j++){
                m_adyacencias[i][j] = datos.getDistancias()[i][j];
            }
        }

        memoria_dinamica = new Integer[num_datos][num_datos];
        for(int i = 0; i < num_datos; i++){
            for(int j = 0; j < num_datos; j++){
                memoria_dinamica[i][j] = 0;
            }
        }
    }

    private void intensificar(Vector<Pair<Integer, Integer>> tabu_ciudades, Vector<Pair<Integer, Integer>> tabu_posiciones){
        AlgGreedyAleatorio_Clase02_Grupo01 nuevo = new AlgGreedyAleatorio_Clase02_Grupo01(config, this.datos, semilla, log);
        nuevo.ejecutar();
        Pair<Integer, Integer> numeros = new Pair<>(0,0);
        s_actual = nuevo.getCamino_final();
        distancia_actual = nuevo.getDistancia_total();
        int coordenada_mayor = 0;
        int coordenada_menor = 0;

        PriorityQueue<Pair<Pair<Integer, Integer>, Integer>> cola = new PriorityQueue<>((num_datos*num_datos)/2);

        for(int j = num_datos-1; j > 0; j--){
            for(int k = j-1; k >= 0; k--){
                Pair<Integer, Integer> ciudades = new Pair<>(k,j);
                Pair<Pair<Integer, Integer>, Integer> valor = new Pair<>(ciudades,memoria_dinamica[j][k]);
                cola.add(valor);
            }
        }
        int buscando_origen;
        int buscando_destino;
        boolean origen_encontrado = false;
        boolean destino_encontrado = false;
        int l;

        for(int p = 1; p < num_datos; p++){
            for(int j = p; j < num_datos; j++ ){
                memoria_dinamica[p][j] = 0;
            }
        }
        this.pivote_tabu =0;
        this.tabu_lleno = false;

        for(int j = 0; j < impacto_de_largo_plazo; j++) {
            buscando_origen = cola.peek().getFirst().getFirst();
            buscando_destino = cola.poll().getFirst().getSecond();
            l = 0;
            origen_encontrado = false;
            destino_encontrado = false;
            while (!origen_encontrado || !destino_encontrado) {
                if (s_actual.get(l) == buscando_origen && !origen_encontrado) {
                    buscando_origen = l;
                    origen_encontrado = true;
                }
                if (s_actual.get(l) == buscando_destino && !destino_encontrado ) {
                    buscando_destino = l;
                    destino_encontrado = true;
                }
                l++;
            }
            Funciones_Auxiliares.mayorYMenor(numeros, buscando_destino, buscando_origen);
            coordenada_mayor = numeros.getFirst();
            coordenada_menor = numeros.getSecond();
            Funciones_Auxiliares.mayorYMenor(numeros, s_actual.get(buscando_destino), s_actual.get(buscando_origen));
            Integer elem_mayor = numeros.getFirst();
            Integer elem_menor = numeros.getSecond();
            if((memoria_dinamica[coordenada_menor][coordenada_mayor] == 0 || memoria_dinamica[coordenada_menor][coordenada_mayor] == 1)
                    && (memoria_dinamica[elem_menor][elem_mayor] == 0 || memoria_dinamica[elem_menor][elem_mayor] == 2))  {
                distancia_actual = Funciones_Auxiliares.dos_opt(coordenada_menor + 1, coordenada_mayor, s_actual, distancia_actual, m_adyacencias, num_datos, -1);

                if (!Objects.equals(s_actual.get(coordenada_menor + 1), s_actual.get(coordenada_mayor))) {
                    Integer ciudad_aux = s_actual.get(coordenada_menor + 1);
                    s_actual.set(coordenada_menor + 1, s_actual.get(coordenada_mayor));
                    s_actual.set(coordenada_mayor, ciudad_aux);
                }

                Funciones_Auxiliares.mayorYMenor(numeros, s_actual.get(coordenada_menor + 1), s_actual.get(coordenada_mayor));
                coordenada_mayor = numeros.getFirst();
                coordenada_menor = numeros.getSecond();
                //ACTUALIZACION DE LA PARTE DE LAS CIUDADES
                if(this.tabu_lleno){
                    if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 1){
                        memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 0;
                    }else if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 3){
                        memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 2;
                    }

                    tabu_ciudades.set(pivote_tabu, new Pair<>(coordenada_mayor, coordenada_menor));
                    if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 0){
                        memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 1;
                    }else if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 2){
                        memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 3;
                    }
                }else{
                    tabu_ciudades.add(pivote_tabu, new Pair<>(coordenada_mayor, coordenada_menor));
                    if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 0){
                        memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 1;
                    }else if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 2){
                        memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 3;
                    }
                }

                //ACTUALIZACION DE LA PARTE DE LAS POSICIONES DEL VECTOR
                if(this.tabu_lleno){
                    if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 2){
                        memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 0;
                    }else if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 3){
                        memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 1;
                    }

                    tabu_posiciones.set(pivote_tabu, new Pair<>(coordenada_mayor, coordenada_menor));
                    if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 0){
                        memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 2;
                    }else if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 1){
                        memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 3;
                    }

                }else{
                    tabu_posiciones.add(pivote_tabu, new Pair<>(coordenada_mayor, coordenada_menor));
                    if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 0){
                        memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 2;
                    }else if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 1){
                        memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 3;
                    }
                }
                this.pivote_tabu++;

                if (pivote_tabu == tenencia_tabu.intValue()) {
                    this.pivote_tabu = 0;
                    this.tabu_lleno = true;
                }
            }
        }
    }

    private void diversificar(Vector<Pair<Integer, Integer>> tabu_ciudades,Vector<Pair<Integer, Integer>> tabu_posiciones){

        AlgGreedyAleatorio_Clase02_Grupo01 nuevo = new AlgGreedyAleatorio_Clase02_Grupo01(config, this.datos, semilla, log);
        nuevo.ejecutar();
        s_actual = nuevo.getCamino_final();
        Pair<Integer, Integer> numeros = new Pair<>(0,0);
        distancia_actual = nuevo.getDistancia_total();
        Integer coordenada_mayor = 0;
        Integer coordenada_menor = 0;

        PriorityQueue<Pair<Pair<Integer, Integer>, Integer>> cola =
                new PriorityQueue<>((a, b) -> b.getSecond().compareTo(a.getSecond()));

        for(int j = num_datos-1; j > 0; j--){
            for(int k = j-1; k >= 0; k--){
                Pair<Integer, Integer> ciudades = new Pair<>(k,j);
                Pair<Pair<Integer, Integer>, Integer> valor = new Pair<>(ciudades,memoria_dinamica[j][k]);
                cola.add(valor);
            }
        }

        Integer buscando_origen;
        Integer buscando_destino;
        Boolean origen_encontrado = false;
        Boolean destino_encontrado = false;
        Integer l;

        for(int p = 0; p < num_datos; p++){
            for(int j = p; j < num_datos; j++ ){
                memoria_dinamica[p][j] = 0;
            }
        }
        pivote_tabu =0;
        tabu_lleno = false;

        for(int j = 0; j < impacto_de_largo_plazo; j++) {
            buscando_origen = cola.peek().getFirst().getFirst();
            buscando_destino = cola.poll().getFirst().getSecond();
            l = 0;
            origen_encontrado = false;
            destino_encontrado = false;
            while (!origen_encontrado || !destino_encontrado) {
                if (Objects.equals(s_actual.get(l), buscando_origen) && !origen_encontrado) {
                    buscando_origen = l;
                    origen_encontrado = true;
                }
                if (Objects.equals(s_actual.get(l), buscando_destino) && !destino_encontrado) {
                    buscando_destino = l;
                    destino_encontrado = true;
                }
                l++;
            }

            Funciones_Auxiliares.mayorYMenor(numeros, buscando_destino, buscando_origen);
            coordenada_mayor = numeros.getFirst();
            coordenada_menor = numeros.getSecond();
            Funciones_Auxiliares.mayorYMenor(numeros, s_actual.get(buscando_destino), s_actual.get(buscando_origen));
            Integer elem_mayor = numeros.getFirst();
            Integer elem_menor = numeros.getSecond();

            if((memoria_dinamica[coordenada_menor][coordenada_mayor] == 0 || memoria_dinamica[coordenada_menor][coordenada_mayor] == 1)
                    && (memoria_dinamica[elem_menor][elem_mayor] == 0 || memoria_dinamica[elem_menor][elem_mayor] == 2)) {
                distancia_actual = Funciones_Auxiliares.dos_opt(coordenada_menor + 1, coordenada_mayor, s_actual, distancia_actual, m_adyacencias, num_datos, -1);
                if (!Objects.equals(s_actual.get(coordenada_menor + 1), s_actual.get(coordenada_mayor))) {
                    Integer ciudad_aux = s_actual.get(coordenada_menor + 1);
                    s_actual.set(coordenada_menor + 1, s_actual.get(coordenada_mayor));
                    s_actual.set(coordenada_mayor, ciudad_aux);
                }
                Funciones_Auxiliares.mayorYMenor(numeros, s_actual.get(coordenada_menor + 1), s_actual.get(coordenada_mayor));
                coordenada_mayor = numeros.getFirst();
                coordenada_menor = numeros.getSecond();

                //ACTUALIZACION DE LA PARTE DE LAS CIUDADES
                if(this.tabu_lleno){
                    if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 1){
                        memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 0;
                    }else if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 3){
                        memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 2;
                    }
                    tabu_ciudades.set(pivote_tabu, new Pair<>(coordenada_mayor, coordenada_menor));
                    if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 0){
                        memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 1;
                    }else if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 2){
                        memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 3;
                    }
                }else{
                    tabu_ciudades.add(pivote_tabu, new Pair<>(coordenada_mayor, coordenada_menor));
                    if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 0){
                        memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 1;
                    }else if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 2){
                        memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 3;
                    }
                }

                //ACTUALIZACION DE LA PARTE DE LAS POSICIONES DEL VECTOR
                if(this.tabu_lleno){
                    if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 2){
                        memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 0;
                    }else if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 3){
                        memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 1;
                    }
                    tabu_posiciones.set(pivote_tabu, new Pair<>(coordenada_mayor, coordenada_menor));
                    if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 0){
                        memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 2;
                    }else if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 1){
                        memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 3;
                    }
                }else{
                    tabu_posiciones.add(pivote_tabu, new Pair<>(coordenada_mayor, coordenada_menor));
                    if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 0){
                        memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 2;
                    }else if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 1){
                        memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 3;
                    }
                }
                this.pivote_tabu++;
                if (pivote_tabu == tenencia_tabu.intValue()) {
                    pivote_tabu = 0;
                    this.tabu_lleno = true;
                }
            }
        }
    }


    private void actualizaMemoria(Vector<Pair<Integer, Integer>> tabu_ciudades, Vector<Pair<Integer, Integer>> tabu_posiciones, Integer coordenada_mayor, Integer coordenada_menor){

        //ACTUALIZACION DE LA PARTE DE LAS CIUDADES
        if(this.tabu_lleno){
            if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 1){
                memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 0;
            }else if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 3){
                memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 2;
            }
            tabu_ciudades.set(pivote_tabu, new Pair<>(coordenada_mayor, coordenada_menor));
            if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 0){
                memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 1;
            }else if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 2){
                memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 3;
            }
        }else{
            tabu_ciudades.add(pivote_tabu, new Pair<>(coordenada_mayor, coordenada_menor));
            if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 0){
                memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 1;
            }else if(memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] == 2){
                memoria_dinamica[tabu_ciudades.get(pivote_tabu).getSecond()][tabu_ciudades.get(pivote_tabu).getFirst()] = 3;
            }
        }

        //ACTUALIZACION DE LA PARTE DE LAS POSICIONES DEL VECTOR
        if(this.tabu_lleno){
            if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 2){
                memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 0;
            }else if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 3){
                memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 1;
            }
            tabu_posiciones.set(pivote_tabu, new Pair<>(coordenada_mayor, coordenada_menor));
            if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 0){
                memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 2;
            }else if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 1){
                memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 3;
            }
        }else{
            tabu_posiciones.add(pivote_tabu, new Pair<>(coordenada_mayor, coordenada_menor));
            if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 0){
                memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 2;
            }else if(memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] == 1){
                memoria_dinamica[tabu_posiciones.get(pivote_tabu).getSecond()][tabu_posiciones.get(pivote_tabu).getFirst()] = 3;
            }
        }
        this.pivote_tabu++;

        if(pivote_tabu == tenencia_tabu.intValue()){
            this.pivote_tabu = 0;
            this.tabu_lleno = true;
        }

        Pair<Integer, Integer> numeros = new Pair<>(0,0);
        Integer ciudad_origen = 0;
        Integer ciudad_destino = 0;

        //Recorremos s_actual para actualizar la memoria a largo plazo
        for(int k = 0; k < num_datos-1; k++){
            ciudad_origen = s_actual.get(k);
            ciudad_destino = s_actual.get(k+1);
            Funciones_Auxiliares.mayorYMenor(numeros, ciudad_destino, ciudad_origen);
            coordenada_mayor = numeros.getFirst();
            coordenada_menor = numeros.getSecond();
            memoria_dinamica[coordenada_mayor][coordenada_menor]++;
        }
        ciudad_origen = s_actual.get(s_actual.size()-1);
        ciudad_destino = s_actual.get(0);
        Funciones_Auxiliares.mayorYMenor(numeros, ciudad_destino, ciudad_origen);
        coordenada_mayor = numeros.getFirst();
        coordenada_menor = numeros.getSecond();
        memoria_dinamica[coordenada_mayor][coordenada_menor]++;
    }

void ejecutar(){

    log.registraLog("\n*****************************************************************************\n");
    log.registraLog("\n************************** ALGORITMO BUSQUEDA TABU **************************\n");
    log.registraLog("\n*****************************************************************************\n");
    Double entorno = tamanio_entorno;
    Vector<Pair<Integer, Integer>> tabu_ciudades = new Vector<>(tenencia_tabu.intValue());
    Vector<Pair<Integer, Integer>> tabu_posiciones = new Vector<>(tenencia_tabu.intValue());

    Integer coordenada_mayor = 0;
    Integer coordenada_menor = 0;
    Pair<Integer, Integer> numeros = new Pair<>(0,0);
     for(int i = 0; i < num_datos; i++){
         for(int j = i; j < num_datos; j++ ){
             memoria_dinamica[i][j] = 0;
         }
     }

    Double contador_estancamiento = 0.0;
    Integer mejor_a = -1;
    Integer mejor_b = -1;
    Integer a;
    Integer b;

    for(int i = 1; i < numero_iteraciones+1; i++){
            numeros.setFirst(0);
            numeros.setSecond(0);
            Double mejor_vecino = Double.MAX_VALUE;

        for(int j = 0; j < entorno; j++){
            do {
                a = semilla.nextInt(num_datos);
                b = semilla.nextInt(num_datos);
            }while (a.equals(b));
            double vecino = Funciones_Auxiliares.dos_opt(a,b,s_mejor_actual,distancia_mejor_actual,m_adyacencias,num_datos,i);
            Funciones_Auxiliares.mayorYMenor(numeros, a, b);
            coordenada_mayor = numeros.getFirst();
            coordenada_menor = numeros.getSecond();
            Funciones_Auxiliares.mayorYMenor(numeros, s_mejor_actual.get(a), s_mejor_actual.get(b));
            Integer elem_mayor = numeros.getFirst();
            Integer elem_menor = numeros.getSecond();

            //Actualizamos el mejor vecino solo si lo permite la memoria a corto plazo
            if(vecino < mejor_vecino && (memoria_dinamica[coordenada_menor][coordenada_mayor] == 0 || memoria_dinamica[coordenada_menor][coordenada_mayor] == 1)
                                    && (memoria_dinamica[elem_menor][elem_mayor] == 0 || memoria_dinamica[elem_menor][elem_mayor] == 2)){
                mejor_a = a;
                mejor_b = b;
                mejor_vecino = vecino;
            }
        }

        //System.out.println("mejor_actual evoluciona de " + distancia_mejor_actual + " a " + mejor_vecino);
        int ciudad_aux = s_mejor_actual.get(mejor_a);
        s_mejor_actual.set(mejor_a, s_mejor_actual.get(mejor_b));
        s_mejor_actual.set(mejor_b, ciudad_aux);
        distancia_mejor_actual = mejor_vecino;


        contador_estancamiento++;

        if(distancia_mejor_actual < distancia_actual){
            contador_estancamiento = 0.0;
            s_actual = s_mejor_actual;
            distancia_actual = distancia_mejor_actual;
            if(distancia_actual < distancia_global){
                log.registraLog("\n(" + i + ")Mejora de distancia global: " + distancia_global + "-->" + distancia_actual);
                log.registraLog("  [" + mejor_a + ", " + mejor_b + "]");
                distancia_global = distancia_actual;
                s_global = s_actual;
            }
        }

        Funciones_Auxiliares.mayorYMenor(numeros, mejor_a, mejor_b);
        coordenada_mayor = numeros.getFirst();
        coordenada_menor = numeros.getSecond();
        actualizaMemoria(tabu_ciudades, tabu_posiciones, coordenada_mayor, coordenada_menor);

        if(contador_estancamiento.equals(param_estancamiento)) {

            int continua_por = semilla.nextInt(100);
            if(continua_por < oscilacion_estrategica){
                log.registraLog("\n\n<<<<<<< INTENSIFICAMOS ("+ i +") >>>>>>>>>>>");
                intensificar(tabu_ciudades, tabu_posiciones);
                log.registraLog("Distancia despues de intensificar: " + distancia_actual +"\n");
                s_mejor_actual = s_actual;
                distancia_mejor_actual = distancia_actual;

            }else{
                log.registraLog("\n\n<<<<<<< DIVERSIFICAMOS ("+ i +") >>>>>>>>>>>");
                diversificar(tabu_ciudades, tabu_posiciones);
                log.registraLog("Distancia despues de diversificar: " + distancia_actual +"\n");
                s_mejor_actual = s_actual;
                distancia_mejor_actual = distancia_actual;
            }
            contador_estancamiento = 0.0;

        }

        if(i % (numero_iteraciones*(cada_cuanto/100)) == 0){
            entorno = entorno*((100-disminucion)/100);
        }
    }

}

    public Double getDistancia_global() {
        return distancia_global;
    }
}
