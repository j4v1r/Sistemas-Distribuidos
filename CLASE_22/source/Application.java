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

import java.util.ArrayList;
import java.util.List;

public class Application {
    private static final String WORKER_ADDRESS_1 = "http://localhost:8081/task";
    private static final String WORKER_ADDRESS_2 = "http://localhost:8082/task";

    public static void main(String[] args) {
        List<String> listaTareas = new ArrayList<>();

        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,a");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,b");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,c");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,d");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,e");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,f");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,g");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,h");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,i");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,j");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,k");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,l");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,m");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,n");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,o");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,p");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,q");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,r");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,s");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,t");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,u");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,v");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,w");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,x");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,y");
        listaTareas.add("934ded44ae4c25f79b68e0e4461f3d908ef5f3c9e312c0e98647f41dd4800087,z");

        System.out.println("Las tareas a resolver son las siguientes:");
        for (int i = 0; i < listaTareas.size(); i++) {
            System.out.println("Tarea " + i + ":" + listaTareas.get(i));
        }

        Aggregator aggregator = new Aggregator();
        List<String> workers = List.of(WORKER_ADDRESS_1, WORKER_ADDRESS_2);
        
        aggregator.sendTasksToWorkers(workers, listaTareas);
    }
}