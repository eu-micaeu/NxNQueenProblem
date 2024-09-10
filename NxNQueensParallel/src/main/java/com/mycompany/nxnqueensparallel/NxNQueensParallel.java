package com.mycompany.nxnqueensparallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class NxNQueensParallel {

    // Cria um pool de threads ForkJoinPool para executar tarefas em paralelo
    private static final ForkJoinPool pool = new ForkJoinPool();

    public static void main(String[] args) {
        
        int n = 12; // Tamanho do tabuleiro NxN. Pode ser alterado para outro valor de N
        
        // Submete a tarefa inicial ao pool para encontrar todas as soluções
        List<int[]> solutions = pool.invoke(new NQueensTask(new int[0], n));
        
        // Imprime o número total de soluções encontradas
        System.out.println("Total de solucoes: " + solutions.size());
        
    }

    // Tarefa recursiva para resolver o problema das N-rainhas
    private static class NQueensTask extends RecursiveTask<List<int[]>> {
        
        private final int[] rainhas; // Configuração atual das rainhas no tabuleiro
        
        private final int n; // Tamanho do tabuleiro NxN

        // Construtor da tarefa
        NQueensTask(int[] rainhas, int n) {
            
            this.rainhas = rainhas;
            
            this.n = n;
            
        }

        @Override
        protected List<int[]> compute() {
           
            List<int[]> solucoes = new ArrayList<>(); // Lista para armazenar as soluções encontradas

            // Se o número de rainhas é igual ao tamanho do tabuleiro, adiciona a solução
            if (rainhas.length == n) {
                solucoes.add(rainhas);
            } else {
                List<NQueensTask> subtarefas = new ArrayList<>(); // Lista de subtarefas a serem executadas

                // Tenta colocar uma rainha em cada coluna da próxima linha
                for (int i = 0; i < n; i++) {
                    // Verifica se é válido colocar uma rainha na coluna 'i'
                    if (ehValido(rainhas, i)) {
                        // Cria uma nova configuração de rainhas com a nova rainha adicionada
                        int[] novasRainhas = new int[rainhas.length + 1];
                        System.arraycopy(rainhas, 0, novasRainhas, 0, rainhas.length);
                        novasRainhas[rainhas.length] = i;
                        // Adiciona uma nova subtarefa para a configuração atualizada
                        subtarefas.add(new NQueensTask(novasRainhas, n));
                    }
                }
                // Executa todas as subtarefas e agrega os resultados das soluções
                invokeAll(subtarefas).forEach(tarefa -> solucoes.addAll(tarefa.join()));
            }
            return solucoes; // Retorna a lista de soluções encontradas
        }

        // Verifica se é válido colocar uma rainha na coluna 'coluna' da linha atual
        private boolean ehValido(int[] rainhas, int coluna) {
            int linha = rainhas.length; // A linha onde a rainha será colocada
            // Verifica conflitos com as rainhas já colocadas
            for (int i = 0; i < linha; i++) {
                // Verifica se há uma rainha na mesma coluna ou na mesma diagonal
                if (rainhas[i] == coluna || Math.abs(rainhas[i] - coluna) == linha - i) {
                    return false; // Conflito encontrado
                }
            }
            return true; // Nenhum conflito encontrado
        }
    }
}
