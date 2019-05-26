package com.company.questao1;

public class ContaBancariaSincronized {

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
