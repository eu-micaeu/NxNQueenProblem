package com.mycompany.nxnqueensparallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class NxNQueensParallel {
    private ConcurrentLinkedQueue<int[]> solucoes; // Lista thread-safe para armazenar todas as soluções encontradas
    private int tamanho; // Tamanho do tabuleiro (NxN)
    private ForkJoinPool pool; // Pool de threads para execução paralela

    public NxNQueensParallel(int tamanho) {
        this.tamanho = tamanho;
        this.solucoes = new ConcurrentLinkedQueue<>();
        this.pool = new ForkJoinPool();
    }

    public void resolver() {
        pool.invoke(new PosicionarRainhasTask(0, new int[tamanho], 0)); // Inicia a solução
    }

    // Classe interna para representar a tarefa de posicionamento de rainhas
    private class PosicionarRainhasTask extends RecursiveTask<Void> {
        private final int linha;
        private final int[] rainhas;
        private final int profundidade;

        public PosicionarRainhasTask(int linha, int[] rainhas, int profundidade) {
            this.linha = linha;
            this.rainhas = rainhas.clone(); // Usa uma cópia do array unidimensional
            this.profundidade = profundidade;
        }

        @Override
        protected Void compute() {
            if (linha == tamanho) {
                solucoes.add(rainhas.clone()); // Adiciona a solução encontrada
                return null;
            }

            List<PosicionarRainhasTask> tarefas = new ArrayList<>();

            for (int coluna = 0; coluna < tamanho; coluna++) {
                if (ehSeguro(linha, coluna)) {
                    rainhas[linha] = coluna; // Posiciona a rainha
                    if (profundidade < 2) { // Ajuste a profundidade para controlar o paralelismo
                        PosicionarRainhasTask tarefa = new PosicionarRainhasTask(linha + 1, rainhas, profundidade + 1);
                        tarefas.add(tarefa);
                    } else {
                        new PosicionarRainhasTask(linha + 1, rainhas, profundidade + 1).compute();
                    }
                }
            }

            invokeAll(tarefas);
            return null;
        }

        private boolean ehSeguro(int linha, int coluna) {
            for (int i = 0; i < linha; i++) {
                int outrasColunas = rainhas[i];
                if (outrasColunas == coluna || Math.abs(outrasColunas - coluna) == linha - i) {
                    return false;
                }
            }
            return true;
        }
    }

    public List<int[]> getSolucoes() {
        return new ArrayList<>(solucoes); // Converte a lista thread-safe em uma lista normal
    }

    public static void main(String[] args) {
        int tamanho = 15;
        NxNQueensParallel nRainhas = new NxNQueensParallel(tamanho);

        nRainhas.resolver();

        List<int[]> solucoes = nRainhas.getSolucoes();
        System.out.println("Numero de solucoes: " + solucoes.size());
    }
}
