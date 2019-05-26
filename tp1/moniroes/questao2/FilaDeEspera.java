package com.company.questao2;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Classe que essencialmente funciona como um buffer sincronizado para os clientes que chegam na barbearia
 */
public class FilaDeEspera {

    private final Queue<Cliente> fila;

    private static int numTotalClientes;

    /**
     * Tamanho maximo da fila de espera
     */
    private int maxSize;

    public FilaDeEspera(int maxSize){
        this.maxSize = maxSize;
        this.fila = new LinkedList<>();
    }

    public synchronized boolean enfileiraCliente(Cliente c,long threadId){
            if (this.fila.size() < this.maxSize) {
                this.fila.add(c);
                System.out.println("Thread Cliente: " + threadId + " enfileirando...");
                //System.out.println("maxSize: " + this.maxSize + "size: " + this.fila.size());
                notifyAll();
                return true;
            } else {
                return false;
            }
    }

    public synchronized Cliente getNextCliente(long threadId){
        System.out.println(FilaDeEspera.getNumTotalClientes());
            if (FilaDeEspera.getNumTotalClientes() == 0) {
                //faz os barbeiros que estao dormindo finalizarem
                notifyAll();
                return null;
            }
            try {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FilaDeEspera.decrNumTotalClientes();
                return this.fila.remove();
            } catch (NoSuchElementException e) {
                //System.out.println("total = "  + Barbeiro.getNumTotalClientes());

                System.out.println("Thread de Barbeiro: " + threadId + " Dormindo...");
                try {
                    wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.out.println("Thread de Barbeiro: " + threadId + " Acordando...");
                return null;
            }
    }

    public synchronized static void setNumTotalClientes(int numTotalClientes) {
        FilaDeEspera.numTotalClientes = numTotalClientes;
    }

    public synchronized static int getNumTotalClientes() {
        return numTotalClientes;
    }

    public synchronized static void decrNumTotalClientes(){
        FilaDeEspera.numTotalClientes--;
    }


    public int getSize(){
        return this.fila.size();
    }

}
