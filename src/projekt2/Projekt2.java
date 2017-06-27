package projekt2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Semestralni prace PRJ2 rok 2016/2017.
 * @author Martina Becaverova 
 * @version 1.0
 * @since 2017
 */
public class Projekt2 {

    static Scanner sc; //scenner
    static int pocetVrcholu; //pocet vrcholov grafu
    static String[] vrcholy; // pole vrcholov grafu
    static int pocetHran;	//pocet hran
    static String[] hrany;	//pole hran
    static String vrchol;	//startovni vrchol z ktereho prohledavame graf

    /**
     * Metoda main. Provadi cteni a zapis do suboru, kdyz byl zadan vstupni soubor, 
     * jestli ne pracuje s konzolou, ceka na zadani vstupnich udaju z klavesnice
     *
     * @param args Vstupni parametry
     */
    public static void main(String[] args) {
        sc = null;
        if (args.length != 0) {
            try {
                sc = new Scanner(new File(args[0]));
            } catch (FileNotFoundException e) {
                System.err.println("Soubor se vstupy nenalezen");
                e.printStackTrace();
            }
        } else {
            sc = new Scanner(System.in);
        }

        read();
        Graf mdfs = new Graf(pocetVrcholu, vrcholy, pocetHran, hrany);
        if (args.length == 0) {
            System.out.println("Zadej vstupni vrchol");
            String start = sc.next();
            if (mdfs.isEuler(start)) {
                System.out.println("Cesta");
                LinkedList<String> euler = mdfs.eulerCircle(start);
                if (!euler.getFirst().equals(euler.getLast())) {
                    System.out.println("Graf neni eulerovsky");
                } else {
                    System.out.println(euler);
                }
            } else {
                System.out.println("Graf neni eulerovsky");
            }

        } else {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter("vysledky.txt"));
                //bw.write(euler);
                if (mdfs.isEuler(mdfs.grafVertex[0].name)) {
                    bw.write("Cesta");
                    LinkedList<String> euler = mdfs.eulerCircle(mdfs.grafVertex[0].name);
                    if (!euler.getFirst().equals(euler.getLast())) {
                        bw.write("Graf neni eulerovsky");
                    } else {
                        bw.write(euler.toString());
                    }
                } else {
                    bw.write("Graf neni eulerovsky");
                }
                bw.close();
            } catch (IOException e) {
                System.err.println("Chyba pri zapisu do souboru");
                e.printStackTrace();
            }
        }

        sc.close();
    }

    public static void read() {
        pocetVrcholu = Integer.parseInt(sc.nextLine());
        vrcholy = new String[pocetVrcholu];
        for (int i = 0; i < pocetVrcholu; i++) {
            vrcholy[i] = sc.nextLine();
        }
        pocetHran = Integer.parseInt(sc.nextLine());
        hrany = new String[pocetHran];
        for (int i = 0; i < pocetHran; i++) {
            hrany[i] = sc.nextLine();
        }

    }

}
