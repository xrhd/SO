package com.company.questao1;

public class ContaBancariaSemControle {

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
