package projekt2;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Trida pro reprezentaci grafu
 */
public class Graf {

    /**
     * Pole vrcholu grafu.
     */
    Vrchol[] grafVertex;
    /**
     * Matice sousednosti.
     */
    int[][] matice;
    boolean oriented = false;

    /**
     * Konstruktor vytvari pole vrcholu a pole hran.
     *	
     * @param pocetVrcholu pocet vrcholu
     * @param vrcholy pole vrcholu
     * @param pocetHran pocet hran medzi vrcholy
     * @param hrana popis hran mezi vrcholy
     */
    public Graf(int pocetVrcholu, String[] vrcholy, int pocetHran, String[] hrana) {
        grafVertex = new Vrchol[pocetVrcholu];
        matice = new int[pocetVrcholu][pocetVrcholu];

        for (int i = 0; i < pocetVrcholu; i++) {
            addVertex(vrcholy[i], i);
        }
        for (int i = 0; i < pocetHran; i++) {
            addEdge(hrana[i]);
        }
    }

    /**
     * Prida vrchol do pole vrcholu.
     *
     * @param nazev nazev vrcholu
     * @param i Index vrcholu v poli
     */
    public void addVertex(String nazev, int i) {
        grafVertex[i] = new Vrchol(nazev);
    }

    /**
     * Vytvari hrany mezi vrcholmi.
     *
     * @param hrana retezec popisujici hranu
     */
    public void addEdge(String hrana) {
        String[] h = null;
        if (hrana.contains("-")) {
            h = hrana.split("-");
            matice[index(h[0])][index(h[1])] = 1;
            matice[index(h[1])][index(h[0])] = 1;
        }
        if (hrana.contains(">")) {
            h = hrana.split(">");
            matice[index(h[0])][index(h[1])] = 1;
            oriented = true;
        }
        if (hrana.contains("<")) {
            h = hrana.split("<");
            matice[index(h[1])][index(h[0])] = 1;
            oriented = true;
        }
    }

    /**
     * Najde index vrcholu v poli vrcholu.
     *
     * @param c nazev vrcholu
     * @return Index vrcholu
     */
    public int index(String c) { 
        int i = 0; //index vrcholu
        for (Vrchol v : grafVertex) { //prohledava vsechny vrcholy v poly grafVertex
            if (v.name.equals(c)) {   
                return i;   //nazev vrcholu se rovna vstupnimu nazvu vrcholu, nasli jsme hledany index i
            }
            i++; //jinak index i zvedneme o 1
        }
        return -1;
    }

    /**
     * Metoda pro prochazeni grafu, ktera zisti jestli je graf souvisly.
     *
     * @param start startnovni vrchol
     * @return seznam objevenych vrcholu
     */
    public LinkedList<String> wayGrafSouvislost(String start) { //metoda na prohledavani grafu do hloubky MDFS 
        LinkedList<String> souvislost = new LinkedList<>();
        Queue<String> fronta = new LinkedList<>();
        souvislost.add(start);
        fronta.add(start);
        int i = 0;
        while (!(fronta.isEmpty())) {
            Vrchol v1 = null;//pomocny vrchol

            String c = fronta.poll();
            for (Vrchol v : grafVertex) {
                if (v.name.equals(c)) {
                    i = index(v.name);
                }
            }
            String p = null;
            for (int j = matice.length - 1; j >= 0; j--) {//pruchod matici 
                if (matice[i][j] == 1) {
                    p = grafVertex[j].name;
                    for (Vrchol v : grafVertex) {
                        if (v.name.equals(p)) {
                            v1 = v;
                        }
                    }
                    if (v1.finded == false) {
                        v1.finded = true;
                        fronta.add(p);
                    }
                }
            }
            if (!(c.equals(start)))//prvni vrchol je vypsan 
            {
                souvislost.add(c);
            }
        }
        return souvislost;
    }

    /**
     * Metoda pro zisteni sousedu vrcholu.
     *
     * @return vrati true ak uz vrchol nema souseda
     */
    boolean edges(int index) {
        int sum = 0; //pocet sousedu vrcholu na pozici index v poly grafVertex
        for (int i = 0; i <= matice.length - 1; i++) {
            sum += matice[index][i]; 
        }
        return (sum != 0);
    }

    /**
     * Metoda pro nalezeni eulerovskeho tahu.
     *
     * @param start vstupni vrchol odkud se prohledava
     * @return spojovy seznam eulerovske kruznice
     */
    public LinkedList<String> eulerCircle(String start) {
        
        LinkedList<String> euler = new LinkedList<String>();     //pomocni seznam
        LinkedList<String> eulerPom = new LinkedList<String>();  //vysledni eulerovska kruznice
        euler.push(start);      //vlozi do pomocniho seznamu vrchol odkud zacina
        int i = 0;              //index vrcholu v poly       
        int k = 0;              
        while (!euler.isEmpty()) {//Opakuje dokud seznam "euler" neni prazdny 
            
            String c = euler.pop();  //Vyndame vrchol na prvni pozici do pomocne promenne c.
            euler.push(c);           //Vrchol vlozime zpatky do seznamu.
            for (Vrchol v : grafVertex) {  //Pro vsechny vrcholy v grafu overime,
                if (v.name.equals(c)) {    //jestli se nazev vrcholu rovna pomocni promenne c,
                    i = index(v.name);     //kdyz najde shodu, do pomocne promenne i priradi index vrcholu z pola vrcholu
                }  //jinak nedela nic.
            }
            if (edges(i)) { //Zjisti jesli vrchol ma sousedov
                for (int j = 0; j <= matice.length - 1; j++) {            
                    if (matice[i][j] == 1) {  //prohledavanim najde souseda
                        k = j;      //priradi index souseda do promenne k
                        break;
                    }
                }
                euler.push(grafVertex[k].name);    //Vrchol vlozi do pomocniho seznamu
                matice[i][k] = 0;                  //Vymaze hranu
                matice[k][i] = 0;                  //v obou smerech 
            } else { //Vrchol jiz nema zadne dalsi sousedy vlozi ho do seznamu
                if (oriented) {
                    eulerPom.addFirst(euler.pop()); //vklada vrchol na zacatek seznamu(vytahuje vrchol z pomocniho seznamu "euler")
                } else {
                    eulerPom.add(euler.pop());     //vklada vrchol na konec seznamu(eulerPom.addLast(euler.pop))
                }
            }
        }
        return eulerPom;        //vraci vysledni eulerovsky seznam
    }

    /**
     * Metoda, ktera zisti jestli je graf eulerovsky.
     *
     * @return true jestli je graf eulerovsky
     */
    boolean isEuler(String start) {
        int souvislost = wayGrafSouvislost(start).size(); //velkost seznamu pri prohledavani grafu do hloubky
        if (souvislost != grafVertex.length) { //zjisti jestli se velkost seznamu rovna poctu vrcholu v poly
            System.out.println("Graf neni souvislej.");//nerovna => graf neni souvislej => eulerovsky
            return false;
        } else { 
            if (!oriented) { //jestli je graf souvisly neorientovany
                for (int i = 0; i <= grafVertex.length - 1; i++) { //prochazi vsechny vrcholy grafu
                    if (getTopDegreeInput(i) % 2 != 0) { //vsechny vrcholy musi byt sudyho stupne
                        System.out.println("Nasel jsem vrchol, ktery neni sudeho stupne.");
                        return false; // nektery z vrcholu neni sudeho stupne => graf neni eulerovsky
                    }
                }
            } else { //pro souvisly orientovany graf
                if (!degreesOfVertex()) { //zjisti jestli se pocet vstupnich hran rovna poctu vystupnich pro vsechny vrcholy v poly grafVertex
                    System.out.println("Vstupni stupen vrcholu se nerovna vystupnimu");
                    return false; // vstupni stupen se nerovna vystupnimu => graf neni eulerovsky
                }
            }
        }
        return true; //graf je eulerovsky
    }

    /**
     * Metoda pro zisteni vystupnich hran vrcholu.
     *
     * @return pocet vystupnich hran
     */
    int getTopDegreeOutput(int top) {
        int result = 0;
        for (int i = 0; i <= grafVertex.length - 1; i++) {
            result += matice[top][i];
        }
        return result;
    }

    /**
     * Metoda pro zisteni vstupnich hran vrcholu.
     *
     * @return pocet vstupnich hran
     */
    int getTopDegreeInput(int top) {
        int result = 0;
        for (int i = 0; i <= grafVertex.length - 1; i++) {
            result += matice[i][top];
        }
        return result;
    }

    /**
     * Metoda zjisti jestli se pocet vstupnich a vystupnich hran vrcholu rovna.
     *
     * @return true jestli je pocet vstupnich a vystupnich hran rovni
     */
    boolean degreesOfVertex() {
        for (int i = 0; i <= grafVertex.length - 1; i++) {
            if (getTopDegreeInput(i) != getTopDegreeOutput(i)) { //zjisti jestli se pocet vstupnich hran rovna poctu vystupnich hran
                return false;
            }
        }
        return true;
    }
}
