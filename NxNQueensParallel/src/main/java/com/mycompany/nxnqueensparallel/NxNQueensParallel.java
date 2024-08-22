package com.mycompany.nxnqueensparallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NxNQueensParallel {
    private int[][] tabuleiro; // Tabuleiro NxN representado por uma matriz de inteiros
    private List<int[]> solucoes; // Lista para armazenar todas as soluções encontradas
    private int tamanho; // Tamanho do tabuleiro (NxN)
    private ExecutorService executor; // Executor para gerenciar as threads

    // Construtor que inicializa o tabuleiro e a lista de soluções com o tamanho dado
    public NxNQueensParallel(int tamanho) {
        this.tamanho = tamanho;
        this.tabuleiro = new int[tamanho][tamanho];
        this.solucoes = new ArrayList<>();
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    // Método que inicia a solução do problema das N rainhas em paralelo
    public void resolver() throws InterruptedException {
        for (int coluna = 0; coluna < tamanho; coluna++) {
            int finalColuna = coluna;
            executor.submit(() -> {
                int[][] novoTabuleiro = new int[tamanho][tamanho];
                copiarTabuleiro(tabuleiro, novoTabuleiro);
                if (novoTabuleiro[0][finalColuna] == 0) {
                    colocarRainha(0, finalColuna, novoTabuleiro);
                    posicionarRainhas(1, novoTabuleiro);
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
    }

    // Método recursivo para posicionar rainhas
    private void posicionarRainhas(int linha, int[][] tabuleiroAtual) {
        if (linha == tamanho) {
            adicionarSolucao(tabuleiroAtual);
            return;
        }

        for (int coluna = 0; coluna < tamanho; coluna++) {
            if (tabuleiroAtual[linha][coluna] == 0) {
                colocarRainha(linha, coluna, tabuleiroAtual);
                posicionarRainhas(linha + 1, tabuleiroAtual);
                removerRainha(linha, coluna, tabuleiroAtual);
            }
        }
    }

    // Método para posicionar uma rainha na posição (linha, coluna) e atualizar o tabuleiro
    private void colocarRainha(int linha, int coluna, int[][] tabuleiroAtual) {
        tabuleiroAtual[linha][coluna] = -1;

        for (int i = 0; i < tamanho; i++) {
            if (i != coluna) {
                tabuleiroAtual[linha][i]++;
            }
        }

        for (int i = 0; i < tamanho; i++) {
            if (i != linha) {
                tabuleiroAtual[i][coluna]++;
            }
        }

        for (int i = 1; linha + i < tamanho && coluna + i < tamanho; i++) {
            tabuleiroAtual[linha + i][coluna + i]++;
        }
        for (int i = 1; linha + i < tamanho && coluna - i >= 0; i++) {
            tabuleiroAtual[linha + i][coluna - i]++;
        }
        for (int i = 1; linha - i >= 0 && coluna + i < tamanho; i++) {
            tabuleiroAtual[linha - i][coluna + i]++;
        }
        for (int i = 1; linha - i >= 0 && coluna - i >= 0; i++) {
            tabuleiroAtual[linha - i][coluna - i]++;
        }
    }

    // Método para remover uma rainha da posição (linha, coluna) e restaurar o tabuleiro
    private void removerRainha(int linha, int coluna, int[][] tabuleiroAtual) {
        tabuleiroAtual[linha][coluna] = 0;

        for (int i = 0; i < tamanho; i++) {
            if (i != coluna) {
                tabuleiroAtual[linha][i]--;
            }
        }

        for (int i = 0; i < tamanho; i++) {
            if (i != linha) {
                tabuleiroAtual[i][coluna]--;
            }
        }

        for (int i = 1; linha + i < tamanho && coluna + i < tamanho; i++) {
            tabuleiroAtual[linha + i][coluna + i]--;
        }
        for (int i = 1; linha + i < tamanho && coluna - i >= 0; i++) {
            tabuleiroAtual[linha + i][coluna - i]--;
        }
        for (int i = 1; linha - i >= 0 && coluna + i < tamanho; i++) {
            tabuleiroAtual[linha - i][coluna + i]--;
        }
        for (int i = 1; linha - i >= 0 && coluna - i >= 0; i++) {
            tabuleiroAtual[linha - i][coluna - i]--;
        }
    }

    // Método para adicionar a configuração atual do tabuleiro como uma solução válida
    private synchronized void adicionarSolucao(int[][] tabuleiroAtual) {
        int[] solucao = new int[tamanho];
        for (int linha = 0; linha < tamanho; linha++) {
            for (int coluna = 0; coluna < tamanho; coluna++) {
                if (tabuleiroAtual[linha][coluna] == -1) {
                    solucao[linha] = coluna;
                    break;
                }
            }
        }
        solucoes.add(solucao);
    }

    // Método para copiar o tabuleiro
    private void copiarTabuleiro(int[][] origem, int[][] destino) {
        for (int i = 0; i < tamanho; i++) {
            System.arraycopy(origem[i], 0, destino[i], 0, tamanho);
        }
    }

    // Método para retornar todas as soluções encontradas
    public List<int[]> getSolucoes() {
        return solucoes;
    }

    // Método principal para executar o programa
    public static void main(String[] args) throws InterruptedException {
        int tamanho = 15; // Tamanho do tabuleiro (NxN)
        NxNQueensParallel nRainhasParalelo = new NxNQueensParallel(tamanho);

        // Início da medição do tempo
        long inicio = System.nanoTime();

        nRainhasParalelo.resolver();

        // Fim da medição do tempo
        long fim = System.nanoTime();
        long duracao = (fim - inicio) / 1_000_000; // Calcula a duração em milissegundos

        List<int[]> solucoes = nRainhasParalelo.getSolucoes();
        System.out.println("Número de soluções: " + solucoes.size());
        System.out.println("Tempo de execução: " + duracao + " ms");

        for (int[] solucao : solucoes) {
            for (int i = 0; i < solucao.length; i++) {
                for (int j = 0; j < solucao.length; j++) {
                    if (solucao[i] == j) {
                        System.out.print("Q ");
                    } else {
                        System.out.print(". ");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
