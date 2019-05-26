package com.company.questao1;

public class ContaBancariaThread implements Runnable {

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

    public ContaBancariaThread(char modoOperacao,int valorDeposito, ContaBancariaSincronized contaBancariaSincronized){
        this.valorDeposito = valorDeposito;
        this.modoOperacao = modoOperacao;
        this.contaBancariaSincronized = contaBancariaSincronized;
        this.contaBancariaSemControle = null;
        contaBancariaSemPreferencia = null;
    }

    public ContaBancariaThread(char modoOperacao,int valorDeposito, ContaBancariaSemPreferencia contaBancariaSemPreferencia){
        this.valorDeposito = valorDeposito;
        this.modoOperacao = modoOperacao;
        this.contaBancariaSemPreferencia = contaBancariaSemPreferencia;
        this.contaBancariaSemControle = null;
        contaBancariaSincronized = null;
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
        }else if(this.itemQuestao==2){
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
        }else if(this.itemQuestao == 3){
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
