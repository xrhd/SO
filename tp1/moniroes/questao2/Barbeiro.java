package com.company.questao2;

import com.company.questao1.ContaBancariaSemControle;
import com.company.questao1.ContaBancariaSemPreferencia;
import com.company.questao1.ContaBancariaSincronized;


/*
Ideia geral: Os clientes chegam  e sao enfileirados em uma fila sincronizada. Enquanto existirem barbeiros livres, mandar um barbeiro
desinfileirar um clilente e o atender. Se a fila estiver cheia os clientes sao descartados.
 */

public class Barbeiro implements Runnable {



    private long threadId;
    private static int numBarbeiros;

    //variavel que determina se um dado barbeiro está ocupado;
    private boolean isBusy;

    private FilaDeEspera filaDeEspera;

    public Barbeiro(FilaDeEspera filaDeEspera){
        this.filaDeEspera = filaDeEspera;
    }


    public void atendeCliente(){
        Cliente clt;
        /*
        o motivo da filaDeEspera lidar com isso e porque ela e synchronized. Caso eu quisesse lidar com o wait() na classe Barbeiro
        esse metodo iria ter que ser synchronized.
        */
        clt = this.filaDeEspera.getNextCliente(threadId);

        //se um cliente foi retirado com sucesso da fila, noticiar que o tal cliente vai ser atendido
        if(clt != null){
            System.out.println("Thread Cliente: " + clt.getThreadId() + " atendida pelo barbeiro: " + threadId);
            clt.notifyAtendimento(threadId);
        }

    }



    @Override
    public void run(){
        this.threadId = Thread.currentThread().getId();
        System.out.println("Thread de Barbeiro: " + this.threadId + " Inicializando...");
        numBarbeiros++;

        /*
        enquanto todos os clientes nao forem atendidos, tentar atender. Obs: esse numero nao corresponde ao numero de clientes esperando o atendimento nas filas.
        Isso ocorre para simular uma entrada dinâmica de clientes.
        */
        while(FilaDeEspera.getNumTotalClientes() > 0){
            atendeCliente();
        }


        numBarbeiros--;
        System.out.println("Thread de Barbeiro: " + this.threadId + " Finalizando...");
    }
}
