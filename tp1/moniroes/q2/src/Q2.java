/*rhd && asx*/
import java.util.ArrayList;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Q2 {

    static ArrayList<Barbeiro> barbeiros = new ArrayList<>();
    static ArrayList<Cliente> clientes = new ArrayList<>();

    public static void main(String[] args) {

        
        Scanner inp = new Scanner(System.in);
        System.out.println("[INFORME]: Barbeiros Vagas Clientes: ");
        int numBarbeiros = inp.nextInt();
        int tamFilaEspera = inp.nextInt();
        int numClientes = inp.nextInt(); 
        System.out.println();

        //  setup
        Salao.setNumTotalClientes(numClientes);
        Salao salao = new Salao(tamFilaEspera);
        ArrayList<Barbeiro> barbeiros = new ArrayList<>();

        // create threads
        Thread[] threads = new Thread[numBarbeiros + numClientes];

        for (int i = 0; i < numBarbeiros; i++) {
            Barbeiro barbeiro = new Barbeiro(salao);
            barbeiros.add(barbeiro);
            threads[i] = new Thread(barbeiro);
        }
        ArrayList<Cliente> clientes = new ArrayList<>();
        for (int i = numBarbeiros; i < numBarbeiros+numClientes; i++) {
            Cliente clt = new Cliente(salao);
            clientes.add(clt);
            threads[i] = new Thread(clt);
        }

        // init threads
        for (int i = 0; i<numBarbeiros+numClientes; i++) {
            threads[i].start();
        }
    }
}


class Salao {

    private final Queue<Cliente> fila;
    private static int numTotalClientes;
    private int maxSize;

    public Salao(int maxSize){
        this.maxSize = maxSize;
        this.fila = new LinkedList<>();
    }

    public synchronized boolean enfileiraCliente(Cliente c,long threadId){
        if (this.fila.size() < this.maxSize) {
            Barbeiro.updateAll();
            try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.fila.add(c);
                System.out.println("\t[Esperando    ] Thread Cliente " + threadId);
                //libera a fila
                notifyAll();


                return true;
            } else {
                if(Barbeiro.numBarbeiros > 0){
                    Barbeiro.updateAll();
                    //tranca a fila de espera que consequentemente tranca o cliente
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
    }

    public synchronized Cliente getNextCliente(long threadId){
        System.out.println("\n[N Clientes]: "+Salao.getNumTotalClientes()+"\n");
            if (Salao.getNumTotalClientes() == 0) {
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
                Cliente clt = this.fila.remove();
                if(clt != null){
                    Salao.decrNumTotalClientes();
                    System.out.println("\t[Atendimento] Thread Barbeiro: " + threadId + " atendendo cliente: " + clt.getThreadId());
                }
                notifyAll();
                return clt;
            } catch (NoSuchElementException e) {
                //System.out.println("total = "  + Barbeiro.getNumTotalClientes());

                System.out.println("\t[Dormindo     ] Thread Barbeiro " + threadId);
                try {
                    wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.out.println("\t[Acordando    ] Thread Barbeiro " + threadId);
                return null;
            }
    }

    public synchronized static void setNumTotalClientes(int numTotalClientes) {
        Salao.numTotalClientes = numTotalClientes;
    }

    public synchronized static int getNumTotalClientes() {
        return numTotalClientes;
    }

    public synchronized static void decrNumTotalClientes(){
        Salao.numTotalClientes--;
    }


    public int getSize(){
        return this.fila.size();
    }

}

class Barbeiro implements Runnable {

    private long threadId;
    public static int numBarbeiros;
    private boolean isBusy;
    private Salao filaDeEspera;

    public Barbeiro(Salao filaDeEspera){
        this.filaDeEspera = filaDeEspera;
    }


    public static void updateAll(){
        for (Barbeiro b:Q2.barbeiros) {
            synchronized (b){
                b.notifyAll();
            }
        }
    }

    public void atendeCliente(){
        Cliente clt;
        clt = this.filaDeEspera.getNextCliente(threadId);
        if(clt != null){
            System.out.println("\t[Atendido   ] Thread Cliente: " + clt.getThreadId() + " atendida pelo barbeiro: " + threadId);
            clt.notifyAtendimento(threadId);
        }
    }

    @Override
    public void run(){
        this.threadId = Thread.currentThread().getId();
        System.out.println("\t[Inicializando] Thread Barbeiro " + this.threadId);

        while(Salao.getNumTotalClientes() > 0){
            Barbeiro.numBarbeiros--;
            atendeCliente();
            Barbeiro.numBarbeiros++;
        }

        System.out.println("\t[Finalizando  ] Thread Barbeiro " + this.threadId);
    }
}


class Cliente implements Runnable {

    private long threadId;
    private Salao filaDeEspera;
    private boolean foiAtendido = false;

    public Cliente(Salao filaDeEspera){
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
            System.out.println("\t[Sem Vagas    ] Thread Cliente: " + this.threadId + " Descartada por falta de espaço...");
            Salao.decrNumTotalClientes();
            foiAtendido = true;
            //acorda as threads barbeiro que estiverem dormindo
        }
    }

    @Override
    public synchronized void run(){
        threadId = Thread.currentThread().getId();
        System.out.println("\t[Inicializando] Thread Cliente " + this.threadId );

        enfileiraCliente();
        while(!foiAtendido){
            try { wait(); }
            catch (InterruptedException e) {e.printStackTrace();}
        }

        System.out.println("\t[Finalizando  ] Thread Cliente " + this.threadId);
    }

    public long getThreadId(){
        return threadId;
    }
}