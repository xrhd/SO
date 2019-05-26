package com.company.questao2;


import java.util.ArrayList;
import java.util.Scanner;

/*
Entrada de exemplo:

2 cadeiras, 2 barbeiros e 8 clientes

2
2
4


Obs: O programa precisa de uma condiçao de parada. No caso a condicao usada foi todos os clientes serem atendidos.
Sem isso alguma outra estrategia precisaria ser usada (até o usuario dar uma input especifica para parar)
 */


public class BarbeariaMain {

    public static void main(String[] args) {


        Scanner inp = new Scanner(System.in);

        int tamFilaEspera = inp.nextInt();
        int numBarbeiros = inp.nextInt();
        int numClientes = inp.nextInt();
        //seta o numero total de clientes
        FilaDeEspera.setNumTotalClientes(numClientes);


        FilaDeEspera filaDeEspera = new FilaDeEspera(tamFilaEspera);

        ArrayList<Barbeiro> barbeiros = new ArrayList<>();
        for (int i = 0; i < numBarbeiros; i++) {
            Barbeiro barbeiro = new Barbeiro(filaDeEspera);
            barbeiros.add(barbeiro);
            Thread thread = new Thread(barbeiro);
            thread.start();
        }


        ArrayList<Cliente> clientes = new ArrayList<>();
        for (int i = 0; i < numClientes; i++) {
            Cliente clt = new Cliente(filaDeEspera);
            clientes.add(clt);
            Thread thread = new Thread(clt);
            thread.start();
        }


    }
}
