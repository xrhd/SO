// package com.company;
// import ContaBancariaSemControle;
// import ContaBancariaSemPreferencia;
// import ContaBancariaSincronized;
// import ContaBancariaThread;

import java.util.ArrayList;
import java.util.Scanner;

class Q1 {

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
        Scanner inp = new Scanner(System.in);

        // setup input
        int numLeitores, numDeposito, numSaque;
        System.out.println("Insira a quantidade de deposito, saque e leituras: ");
        numDeposito = inp.nextInt();
        numSaque = inp.nextInt();
        numLeitores = inp.nextInt();

        // setup
        int numEscritores;
        numEscritores = numDeposito + numSaque;
        ContaBancariaSemControle contaBancariaSemControle = new ContaBancariaSemControle();
        ContaBancariaSemPreferencia contaBancariaSemPreferencia = new ContaBancariaSemPreferencia();
        ContaBancariaSincronized contaBancariaSincronized3 = new ContaBancariaSincronized();


        // leitura
        final int[] vetEntradas = new int[numEscritores];
        System.out.println("Insira o vetor de entrada");
        for (int i = 0 ; i < numEscritores; i++){
            vetEntradas[i] = inp.nextInt();
        }


        // main
        item3(vetEntradas,contaBancariaSincronized3,numLeitores);
    }
}

// package com.company.questao1;
class ContaBancariaThread implements Runnable {

    //qual funcao executar
    private final ContaBancariaSincronized contaBancariaSincronized;
    private final ContaBancariaSemControle contaBancariaSemControle;
    private final ContaBancariaSemPreferencia contaBancariaSemPreferencia;


    private char modoOperacao;
    private int valorDeposito;
    private int itemQuestao;


    public ContaBancariaThread(char modoOperacao,int valorDeposito ,ContaBancariaSemControle contaBancariaSemControle){
        this.valorDeposito = valorDeposito;
        this.modoOperacao = modoOperacao;
        this.contaBancariaSemControle = contaBancariaSemControle;
        contaBancariaSincronized = null;
        contaBancariaSemPreferencia = null;
    }

    public ContaBancariaThread(char modoOperacao,int valorDeposito, ContaBancariaSemPreferencia contaBancariaSemPreferencia){
        this.valorDeposito = valorDeposito;
        this.modoOperacao = modoOperacao;
        this.contaBancariaSemPreferencia = contaBancariaSemPreferencia;
        this.contaBancariaSemControle = null;
        contaBancariaSincronized = null;
    }

    public ContaBancariaThread(char modoOperacao,int valorDeposito, ContaBancariaSincronized contaBancariaSincronized){
        this.valorDeposito = valorDeposito;
        this.modoOperacao = modoOperacao;
        this.contaBancariaSincronized = contaBancariaSincronized;
        this.contaBancariaSemControle = null;
        contaBancariaSemPreferencia = null;
    }

    private void switchModo(){
        if(this.contaBancariaSincronized != null){
            this.itemQuestao = 2;
        }else if(this.contaBancariaSemControle != null){
            this.itemQuestao = 1;
        }else if(this.contaBancariaSemPreferencia != null){
            this.itemQuestao = 3;
        }
    }

    @Override
    public void run(){
        switchModo();
        if(this.itemQuestao == 1) {
            switch (modoOperacao) {
                case ('d'):
                        System.out.println("Thread: " + Thread.currentThread().getId() + " Inicializando escritor... ");
                        System.out.println("Thread: " + Thread.currentThread().getId() + " Escrevendo: " + this.valorDeposito);
                        this.contaBancariaSemControle.depositaSaldo(this.valorDeposito);
                        //deposito
                    break;
                case ('t'):
                   // this.contaBancariaSemControle.transferConta();
                    //transferencia
                    break;
                case ('g'):
                    System.out.println("Thread: " + Thread.currentThread().getId() + " Inicializando leitor... ");
                    System.out.println("Thread: " + Thread.currentThread().getId() + " Lendo: " + this.contaBancariaSemControle.getSaldo());

                    //getter
                    break;
                case ('s'):

                    this.contaBancariaSemControle.setSaldo(this.valorDeposito);
                    //setter
                    break;
            }
        }
        else if(this.itemQuestao==2){
            switch (modoOperacao) {
                case ('d'):
                        System.out.println("Thread: " + Thread.currentThread().getId() + " Inicializando escritor... ");
                        System.out.println("Thread: " + Thread.currentThread().getId() + " Escrevendo: " + this.valorDeposito);
                        this.contaBancariaSincronized.depositaSaldo(this.valorDeposito);
                    //deposito
                    break;
                case ('t'):
                    // this.contaBancariaSemControle.transferConta();
                    //transferencia
                    break;
                case ('g'):
                    System.out.println("Thread: " + Thread.currentThread().getId() + " Inicializando leitor... ");
                    System.out.println("Thread: " + Thread.currentThread().getId() + " Lendo: " + this.contaBancariaSincronized.getSaldo());
                    //getter
                    break;
                case ('s'):
                    this.contaBancariaSincronized.setSaldo(this.valorDeposito);
                    //setter
                    break;
            }
        }
        else if(this.itemQuestao == 3){
            switch (modoOperacao) {
                case ('d'):
                    System.out.println("Thread: " + Thread.currentThread().getId() + " Inicializando escritor... ");
                    System.out.println("Thread: " + Thread.currentThread().getId() + " Escrevendo: " + this.valorDeposito);
                    this.contaBancariaSemPreferencia.depositaSaldo(this.valorDeposito);
                    //deposito
                    break;
                case ('t'):
                    // this.contaBancariaSemControle.transferConta();
                    //transferencia
                    break;
                case ('g'):
                    System.out.println("Thread: " + Thread.currentThread().getId() + " Inicializando leitor... ");
                    System.out.println("Thread: " + Thread.currentThread().getId() + " Lendo: " + this.contaBancariaSemPreferencia.getSaldo());
                    //getter
                    break;
                case ('s'):
                    this.contaBancariaSemPreferencia.setSaldo(this.valorDeposito);
                    //setter
                    break;
            }
        }
        System.out.println("Thread: " + Thread.currentThread().getId() + " Finalizando...");
    }
}


// package com.company.questao1;

class ContaBancariaSemControle {

    private int saldo;


    public void depositaSaldo (int valor){
        try {
            Thread.sleep(210);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.saldo += valor;
    }


    public void transferConta(ContaBancariaSemControle c1, int transferencia){
        c1.depositaSaldo(transferencia);
        this.saldo -= transferencia;
    }

    public int  getSaldo() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
}

class ContaBancariaSemPreferencia {

    private int saldo;

    /**
     * canRun e uma variavel que representa quantos escritores existem.
     */


    //O SLEEP DEVE ESTAR AQUI PARA EVITAR O PROBLEMA DO LOCK
    public synchronized void depositaSaldo (int valor){
        int res = this.saldo + valor;
        //para provocar leitura suja
        try {
            Thread.sleep(210);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.saldo = res;
    }


    public synchronized void transferConta(ContaBancariaSemPreferencia c1, int transferencia){
        c1.depositaSaldo(transferencia);
        this.saldo -= transferencia;
    }

    //o synchronized aqui nao permite dois leitores ao mesmo tempo. Para permitir isso e preciso usar outra estrutura do java.
    public synchronized int getSaldo() {

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return saldo;
    }

    public int getSaldoNoThread(){
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

}


// package com.company.questao1;

class ContaBancariaSincronized {

    private int saldo;

    /**
     * canRun e uma variavel que representa quantos escritores existem.
     */
    private int canRun = 0;

    //O SLEEP DEVE ESTAR AQUI PARA EVITAR O PROBLEMA DO LOCK
    public synchronized void depositaSaldo (int valor){
        int res = this.saldo + valor;
        try {
            Thread.sleep(210);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.saldo = res;
// import ContaBancariaSemControle;
// import ContaBancariaSemPreferencia;
// import ContaBancariaSincronized;
// import ContaBancariaThread;
        //rodou um dos escritores
        this.canRun--;
        notifyAll();
    }


    public synchronized void transferConta(ContaBancariaSincronized c1, int transferencia){
        c1.depositaSaldo(transferencia);
        this.saldo -= transferencia;
    }

    //o synchronized aqui nao permite dois leitores ao mesmo tempo. Para permitir isso e preciso usar outra estrutura do java (filas sincronizadas).
    public synchronized int getSaldo() {
        while(canRun != 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return saldo;
    }

    public int getSaldoNoThread(){
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }


    /**
     * O usuario da classe deve definir quantas threads ecritoras vão existir. É possível tornar esse método mais user-friendly
     * fazendo com que cada vez que um método de escrita for ser executado, essa variável seja incrementada.
     * @param n
     */
    public void setCanRun(int n){
        this.canRun = n;
    }
}
