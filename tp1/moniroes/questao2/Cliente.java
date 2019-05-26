package com.company.questao2;

import com.company.questao1.ContaBancariaSemControle;
import com.company.questao1.ContaBancariaSemPreferencia;
import com.company.questao1.ContaBancariaSincronized;

/* ERRO
Thread Cliente: 15 Inicializando...
Thread de Barbeiro: 14 Inicializando...
Thread de Barbeiro: 13 Inicializando...
Thread Cliente: 16 Inicializando...
Thread Cliente: 15 enfileirando...
2
Thread Cliente: 16 Descartada por falta de espaço...
1
Thread Cliente: 15 atendida pelo barbeiro: 14
Thread Cliente: 15 Finalizando...
Thread de Barbeiro: 13 Dormindo...
0
Thread de Barbeiro: 14 Finalizando...
Thread de Barbeiro: 13 Acordando...
Thread de Barbeiro: 13 Finalizando...


CERTO:
Thread Cliente: 15 Inicializando...
Thread de Barbeiro: 14 Inicializando...
Thread Cliente: 16 Inicializando...
Thread de Barbeiro: 13 Inicializando...
Thread Cliente: 15 enfileirando...
2
Thread Cliente: 16 enfileirando...
1
Thread Cliente: 15 atendida pelo barbeiro: 13
Thread Cliente: 15 Finalizando...
0
Thread Cliente: 16 atendida pelo barbeiro: 14
Thread Cliente: 16 Finalizando...
Thread de Barbeiro: 13 Finalizando...
Thread de Barbeiro: 14 Finalizando...

 */


public class Cliente implements Runnable {


    private long threadId;
    private FilaDeEspera filaDeEspera;
    private boolean foiAtendido = false;

    public Cliente(FilaDeEspera filaDeEspera){
        this.filaDeEspera = filaDeEspera;
    }


    public synchronized void notifyAtendimento(long threadId){
        foiAtendido = true;

        //libera as threads que estao esperando para finalizarem
        notifyAll();
    }


    public void enfileiraCliente(){
        //se entrar no if e porque o this nao foi enfileirado devido a fila estar cheia
        if(!this.filaDeEspera.enfileiraCliente(this,threadId)){
            System.out.println("Thread Cliente: " + this.threadId + " Descartada por falta de espaço...");
            foiAtendido = true;
            //acorda as threads barbeiro que estiverem dormindo
        }
    }

    @Override
    public synchronized void run(){
        threadId = Thread.currentThread().getId();
        System.out.println("Thread Cliente: " + this.threadId + " Inicializando...");
        //coloca o cliente na fila
        enfileiraCliente();
        //se nao foi atendido,
        while(!foiAtendido){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Thread Cliente: " + this.threadId + " Finalizando...");
    }

    public long getThreadId(){
        return threadId;
    }
}
