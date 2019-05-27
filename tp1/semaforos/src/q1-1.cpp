#include <iostream>
#include <vector>
#include <semaphore.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>

#define NCONTAS 2

using namespace std;

int conta[NCONTAS] = {0};
sem_t semaforo;

typedef struct transactionData {
	long id;
	long index;
	long valor;
} tData;

void * deposito (void *t) {
	tData * data = (tData *) t;
	int a = conta[data->index];

	cout << "Thread Escritora " << data->id << " - Valor na conta "
		<< data->index << " antes do deposito: " << conta[data->index] << endl;

	sleep(1);
    
	a += data->valor;
	conta[data->index] = a;
    
	cout << "Thread Escritora " << data->id << " - Valor na conta "
		<< data->index << " depois do deposito: " << conta[data->index] << endl;
}

void * saque (void *t) {
	tData * data = (tData *) t;
	int a = conta[data->index];

	cout << "Thread Escritora " << data->id << " - Valor na conta "
		<< data->index << " antes do saque: " << conta[data->index] << endl;

	sleep(1);

	a -= data->valor;
	conta[data->index] = a;

	cout << "Thread Escritora " << data->id << " - Valor na conta "
		<< data->index << " depois do saque: " << conta[data->index] << endl;
}

void * consulta (void *t) {
	tData * data = (tData *) t;

	cout << "Thread Leitora " << data->id << " - Valor na conta " << data->index
		<< ": " << conta[data->index] << endl;
}

int main () {
	int leitoras, num_deposito, num_saque;
	
	cout << "Insira a quantidade de deposito, saque e leituras: ";
	cin >> num_deposito >> num_saque >> leitoras;
	
	pthread_t threads[num_deposito + num_saque + leitoras];
	tData d[num_deposito + num_saque + leitoras];
	
	for (int i = 0; i < num_deposito; ++i) {
		d[i].id = i;

		cout << endl << "Insira o numero da conta para deposito: ";
		cin >> d[i].index;
	
		cout << endl << "Insira o valor do deposito: ";
		cin >> d[i].valor;
	
		pthread_create(&threads[i], NULL, deposito, (void *) &d[i]);
	}
	
	for (int i = num_deposito; i < num_deposito + num_saque; ++i) {
		d[i].id = i;

		cout << endl << "Insira o numero da conta para saque: ";
		cin >> d[i].index;
	
		cout << endl << "Insira o valor do saque: ";
		cin >> d[i].valor;
	
		pthread_create(&threads[i], NULL, saque, (void *) &d[i]);
	}
	
	for (int i = num_deposito + num_saque; i < num_deposito + num_saque + leitoras; ++i) {
		d[i].id = i;

		cout << endl << "Insira o numero da conta para consulta: ";
		cin >> d[i].index;

		pthread_create(&threads[i], NULL, consulta, (void *) &d[i]);
	}
	
	for (int i = 0; i < num_deposito + num_saque + leitoras; ++i)
		pthread_join(threads[i], NULL);
	
	for (int i = 0; i < NCONTAS; ++i) 
		cout << endl << "Valor final na conta " << i << ": " << conta[i];
}