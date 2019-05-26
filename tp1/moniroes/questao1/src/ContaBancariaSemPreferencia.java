package com.company.questao1;


/**
 * Classe contaBancaria que ainda é synchronized, mas não possui os mecanismos de controle wait() e notify() que a classe synchronized tem.
 * Isso faz com que a leitura suja possa ocorrer.
 */
public class ContaBancariaSemPreferencia {

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
