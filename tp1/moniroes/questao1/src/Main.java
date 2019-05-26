package com.company.questao1;

import ContaBancariaSemControle;
import ContaBancariaSemPreferencia;
import ContaBancariaSincronized;
import ContaBancariaThread;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    /*

Entradas de exemplo.
2
10
10 10 10 10

2
2
10 10
     */

    //https://stackoverflow.com/questions/14858075/set-the-develop-branch-as-the-default-for-a-pull-request
    //https://stackoverflow.com/questions/24215032/cant-update-no-tracked-branch
    //https://stackoverflow.com/questions/11073841/why-doesnt-this-synchronized-method-work-as-expected

    public static void item1(int[] vetEntradas, ContaBancariaSemControle contaBancariaSemControle, int numLeitores){
        ContaBancariaThread contaBancariaThread;
        /**
         * Criar *numEscritores* threads de escrita. Cada uma ira depositar um valor que está guardado em vetEntradas.
         */
        for (int i:vetEntradas) {
            contaBancariaThread = new ContaBancariaThread('d',i,contaBancariaSemControle);
            //passar objeto runnable pra classe.
            Thread thread = new Thread(contaBancariaThread);
            thread.start();
        }

        /**
         * Criar *numLeitores* threads de leitura. Cada uma ira ler o valor da contaBancariaSemControle.
         */
        for (int i = 0;i < numLeitores; i++) {
            contaBancariaThread = new ContaBancariaThread('g',i,contaBancariaSemControle);
            Thread thread = new Thread(contaBancariaThread);
            thread.start();
        }
    }

    public static void item2(int[] vetEntradas, ContaBancariaSemPreferencia contaBancariaSemPreferencia, int numLeitores){
        /**
         * Criar *numEscritores* threads de escrita. Cada uma ira depositar um valor que está guardado em vetEntradas.
         */
        //contaBancariaSincronized.setCanRun(vetEntradas.length);
        for (int i:vetEntradas) {
            ContaBancariaThread contaBancariaThread = new ContaBancariaThread('d',i,contaBancariaSemPreferencia);
            //passar objeto runnable pra classe.
            Thread thread = new Thread(contaBancariaThread);
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }

        /**
         * Criar *numLeitores* threads de leitura. Cada uma ira ler o valor da contaBancariaSemControle.
         */
        for (int i = 0;i < numLeitores; i++) {
            ContaBancariaThread contaBancariaThread = new ContaBancariaThread('g',i,contaBancariaSemPreferencia);
            Thread thread = new Thread(contaBancariaThread);
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }
    }

    public static void item3(int[] vetEntradas, ContaBancariaSincronized contaBancariaSincronized, int numLeitores){


        ArrayList<Thread> threads = new ArrayList<>();


        /**
         * Criar *numEscritores* threads de escrita. Cada uma ira depositar um valor que está guardado em vetEntradas.
         */
        contaBancariaSincronized.setCanRun(vetEntradas.length);
        for (int i:vetEntradas) {
            ContaBancariaThread contaBancariaThread = new ContaBancariaThread('d',i,contaBancariaSincronized);
            //passar objeto runnable pra classe.
            Thread thread = new Thread(contaBancariaThread);
            threads.add(thread);
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }

        /**
         * Criar *numLeitores* threads de leitura. Cada uma ira ler o valor da ContaBancariaSincronized.
         */
        for (int i = 0;i < numLeitores; i++) {
            ContaBancariaThread contaBancariaThread = new ContaBancariaThread('g',i,contaBancariaSincronized);
            Thread thread = new Thread(contaBancariaThread);
            thread.setPriority(Thread.MIN_PRIORITY);
            threads.add(thread);
            thread.start();
        }



        for (int i = 0; i < 4; i++) {

            for (Thread t:threads) {
                if(t.getState() == Thread.State.BLOCKED){
                    System.out.println("Thread " + t.getId() + " estado: " + t.getState());
                }
                /*if(!(t.isAlive())){
                    System.out.println("Thread " + t.getId() + " estado: finalizado" );
                }*/
            }

            //gambi pra poder achar as threads bloqueadas
            try {
                Thread.sleep(1000);
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }



    public static void main(String[] args) {

        /**
         * Leitura
         */
        Scanner inp = new Scanner(System.in);

        final int numEscritores;
        final int numLeitores;

        ContaBancariaSemControle contaBancariaSemControle = new ContaBancariaSemControle();


        ContaBancariaSemPreferencia contaBancariaSemPreferencia = new ContaBancariaSemPreferencia();
        ContaBancariaSincronized contaBancariaSincronized3 = new ContaBancariaSincronized();

        System.out.println("Insira o numero de escritores");
        numEscritores = inp.nextInt();

        System.out.println("Insira o numero de Leitores");
        numLeitores = inp.nextInt();

        final int[] vetEntradas = new int[numEscritores];

        System.out.println("Insira o vetor de entrada");

        for (int i = 0 ; i < numEscritores; i++){
            vetEntradas[i] = inp.nextInt();
        }


        System.out.println("\nItem 1:");

        item1(vetEntradas,contaBancariaSemControle,numLeitores);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Valor Final = " + contaBancariaSemControle.getSaldo());

        System.out.println("\nItem 2:");

        item2(vetEntradas,contaBancariaSemPreferencia,numLeitores);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Valor Final = " + contaBancariaSemPreferencia.getSaldoNoThread());

        System.out.println("\nItem 3:");

        item3(vetEntradas,contaBancariaSincronized3,numLeitores);

        inp.close();

        System.out.println(contaBancariaSincronized3.getSaldoNoThread());

    }
}
