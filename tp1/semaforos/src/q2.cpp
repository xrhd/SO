#include <iostream>
#include <vector>
#include <semaphore.h>
#include <pthread.h>
#include <unistd.h>

using namespace std;

sem_t mutex;
sem_t sem_clientes;
sem_t sem_barbeiros;

vector<int> buffer;

int maxClientes;
int numClientes;
int numThreadsClientes;
int numBarbeiros;

int in = 0;
int out = 0;
int fila = 0;

void * barbeiro (void *arg) {
    int i =  *((int*) arg);
    
    while (numClientes) {
        cout << "Barbeiro " << i << " esta esperando um cliente" << endl; 
        sem_wait(&sem_clientes);
        
        --numClientes;
        --buffer[out];
        
        out = (out + 1) % maxClientes;
        
        cout<< "Barbeiro " << i << " esta cortando cabelo de um cliente." << endl;
        
        sleep(5);
        
        cout<< "Barbeiro " << i << " cortou o cabelo de um cliente." << endl;
        sem_post(&sem_barbeiros);
    }
}

void * cliente (void *arg) {
    int i = *((int*)arg);
    
    if (!buffer[in]) {
        ++buffer[in];
        in = (in + 1) % maxClientes;
        
        cout<< "Cliente " << i << " esta na fila." << endl;
        
        sem_post(&sem_clientes);
        sem_wait(&sem_barbeiros);
        
        cout << "Cliente "<< i <<" foi embora, pois acabou de cortar o cabelo." << endl;
    } else {
        --numClientes;
        
        cout << "Cliente " << i << " foi embora, pois a barbearia estava lotada." << endl;
    }
}

int main () {
    cout << "Informe o numero de barbeiros, cadeiras de espera e clientes: ";
    cin >> numBarbeiros >> maxClientes >> numClientes;
    cout << endl;
    
    numThreadsClientes = numClientes;
    buffer.resize(maxClientes);
    
    sem_init(&mutex, 0, 1);
    sem_init(&sem_clientes, 0, 0);
    sem_init(&sem_barbeiros, 0, 0);
    
    int copia;
    pthread_t threads[numBarbeiros + numClientes];
    
    for (int i = 0; i < numBarbeiros; ++i) {
        pthread_create(&threads[i], NULL, barbeiro, &i);
        
        sleep(0.00001);
    }
    
    for (int i = numBarbeiros; i < numBarbeiros + numThreadsClientes; ++i){
        copia = i - numBarbeiros;
        
        pthread_create(&threads[i], NULL, cliente, &copia);
        
        sleep(0.00001);
    }
    
    for (int i = 0; i < numBarbeiros + numThreadsClientes; ++i)
        pthread_join(threads[i], NULL);
    
    sem_close(&mutex);
    sem_close(&sem_clientes);
    sem_close(&sem_barbeiros);
    
    return 0;
}