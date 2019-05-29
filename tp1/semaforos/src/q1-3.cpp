//authors: rhd asx//

#include <iostream>
#include <vector>
#include <semaphore.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>

#define NCONTAS 2
#define SLEEP 2

using namespace std;

int conta[NCONTAS] = {0};
int nEscritoras;
sem_t semaforoE;
sem_t semaforoL;

typedef struct transactionData {
	long id;
	long index;
	long valor;
} tData;

void * deposito (void *t) {
	sem_wait(&semaforoE);
	// sleep(0.1);
	
	tData * data = (tData *) t;
	int a = conta[data->index];

	cout << "[ ]Thread Escritora " << data->id << " - Valor na conta "
		<< data->index << " antes do deposito: " << conta[data->index] << endl;

	sleep(SLEEP);
    
	a += data->valor;
	conta[data->index] = a;
    
	cout << "[+]Thread Escritora " << data->id << " - Valor na conta "
		<< data->index << " depois do deposito: " << conta[data->index] << endl;

	--nEscritoras;
	sem_post(&semaforoE);
}

void * saque (void *t) {
	sem_wait(&semaforoE);
	// sleep(0.1);
	
	tData * data = (tData *) t;
	int a = conta[data->index];

	cout << "[ ]Thread Escritora " << data->id << " - Valor na conta "
		<< data->index << " antes do saque: " << conta[data->index] << endl;

	sleep(SLEEP);

	a -= data->valor;
	conta[data->index] = a;

	cout << "[-]Thread Escritora " << data->id << " - Valor na conta "
		<< data->index << " depois do saque: " << conta[data->index] << endl;

	--nEscritoras;
	sem_post(&semaforoE);
}

void * consulta (void *t) {
	while (nEscritoras);
	sem_wait(&semaforoL);

	tData * data = (tData *) t;
	
	cout << "[?] Thread Leitora " << data->id << " - Valor na conta " << data->index
		<< ": " << conta[data->index] << endl;

	sem_post(&semaforoL);
}


// Driver //
int main () {
	sem_init(&semaforoE, 0, 1);
	sem_init(&semaforoL, 0, 1);
	
	int leitoras, num_deposito, num_saque;
	
	cout << "[INFORME]: quantidade de deposito, saque e leituras: ";
	cin >> num_deposito >> num_saque >> leitoras;

	nEscritoras = num_deposito + num_saque;
	pthread_t threads[nEscritoras + leitoras];
	tData d[nEscritoras + leitoras];
	
	// leitura

	for (int i = 0; i < num_deposito; ++i) {
		d[i].id = i;

		cout << endl << "Insira o numero da conta para deposito: ";
		cin >> d[i].index;
	
		cout << endl << "Insira o valor do deposito: ";
		cin >> d[i].valor;
	}

	for (int i = num_deposito; i < nEscritoras; ++i) {
		d[i].id = i;

		cout << endl << "Insira o numero da conta para saque: ";
		cin >> d[i].index;
	
		cout << endl << "Insira o valor do saque: ";
		cin >> d[i].valor;
	}
	
	for (int i = nEscritoras; i < nEscritoras + leitoras; ++i) {
		d[i].id = i;

		cout << endl << "Insira o numero da conta para consulta: ";
		cin >> d[i].index;
	}

	cout << endl << endl;

	// chamada de thrds
	int nThreds = num_deposito + num_saque + leitoras;

	for (int i = 0; i < num_deposito; ++i)
		pthread_create(&threads[i], NULL, deposito, (void *) &d[i]);

	for (int i = num_deposito; i < num_deposito+num_saque; ++i)
		pthread_create(&threads[i], NULL, saque, (void *) &d[i]);

	for (int i = num_deposito+num_saque; i < nThreds; ++i)
		pthread_create(&threads[i], NULL, consulta, (void *) &d[i]);

	// init threds
	for (int i = 0; i < nThreds; ++i)
		pthread_join(threads[i], NULL);
	
	for (int i = 0; i < NCONTAS; ++i) 
		cout << endl << "Valor final na conta " << i << ": " << conta[i] << endl;
        
	// sem_destroy
    // sem_close(&semaforoE);
    // sem_close(&semaforoL);
}