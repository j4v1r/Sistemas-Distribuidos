/*
 *  MIT License
 *
 *  Copyright (c) 2019 Michael Pogrebinsky - Distributed Systems & Cloud Computing with Java
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

import networking.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Aggregator {
    private WebClient webClient;

    public Aggregator() {
        this.webClient = new WebClient();
    }

    public List<String> sendTasksToWorkers(List<String> workersAddresses, List<String> tasks) {
        int numWorkers = workersAddresses.size();
        int totalTasks = tasks.size();

        CompletableFuture<String>[] futures = new CompletableFuture[numWorkers];
        String[] currentTasks = new String[numWorkers];
        String[] taskResults = new String[totalTasks];

        int nextTaskIndex = 0;

        // 1. Asignación inicial de tareas a los servidores disponibles
        for (int i = 0; i < numWorkers && nextTaskIndex < totalTasks; i++) {
            String worker = workersAddresses.get(i);
            String task = tasks.get(nextTaskIndex);

            currentTasks[i] = task;
            System.out.println("Al servidor " + worker + " se le asigna la tarea: " + task);
            futures[i] = webClient.sendTask(worker, task.getBytes());

            nextTaskIndex++;
        }

        // 2. Monitoreo mediante isDone() y reasignación dinámica al servidor desocupado
        int completedTasks = 0;
        while (completedTasks < totalTasks) {
            for (int i = 0; i < numWorkers; i++) {
                if (futures[i] != null && futures[i].isDone()) {
                    String worker = workersAddresses.get(i);
                    String completedTask = currentTasks[i];
                    String result = futures[i].join();

                    System.out.println("El servidor " + worker + " completó la tarea " + completedTask);

                    // Guardar el resultado en la posición original de la tarea
                    int taskIndex = tasks.indexOf(completedTask);
                    if (taskIndex != -1) {
                        taskResults[taskIndex] = result;
                    }

                    completedTasks++;
                    futures[i] = null; // Marcar slot como disponible

                    // Reasignar inmediatamente la siguiente tarea de la cola si existe
                    if (nextTaskIndex < totalTasks) {
                        String newTask = tasks.get(nextTaskIndex);
                        currentTasks[i] = newTask;

                        System.out.println("Al servidor " + worker + " se le asigna la tarea: " + newTask);
                        futures[i] = webClient.sendTask(worker, newTask.getBytes());

                        nextTaskIndex++;
                    }
                }
            }

            try {
                Thread.sleep(10); // Pausa breve para reducir uso excesivo de CPU en el bucle
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // 3. Imprimir el resumen con las apariciones respetando el orden original
        List<String> resultsList = new ArrayList<>();
        for (int i = 0; i < totalTasks; i++) {
            System.out.println("Para la tarea " + tasks.get(i) + " El numero de apariciones es " + taskResults[i]);
            resultsList.add(taskResults[i]);
        }

        return resultsList;
    }
}