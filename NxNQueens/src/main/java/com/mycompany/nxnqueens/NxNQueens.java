package com.mycompany.nxnqueens;

import java.util.ArrayList;
import java.util.List;

public class NxNQueens {
    private int[][] tabuleiro; // Tabuleiro NxN representado por uma matriz de inteiros
    private List<int[]> solucoes; // Lista para armazenar todas as soluções encontradas
    private int tamanho; // Tamanho do tabuleiro (NxN)

    // Construtor que inicializa o tabuleiro e a lista de soluções com o tamanho dado
    public NxNQueens(int tamanho) {
        this.tamanho = tamanho;
        this.tabuleiro = new int[tamanho][tamanho];
        this.solucoes = new ArrayList<>();
    }

    // Método que inicia a solução do problema das N rainhas
    public void resolver() {
        posicionarRainhas(0); // Começa a tentar posicionar rainhas a partir da primeira linha
    }

    // Método recursivo para posicionar rainhas
    private void posicionarRainhas(int linha) {
        // Caso base: se todas as rainhas foram posicionadas (linha == tamanho), adiciona a solução
        if (linha == tamanho) {
            adicionarSolucao();
            return;
        }

        // Tenta posicionar uma rainha em cada coluna da linha 'linha'
        for (int coluna = 0; coluna < tamanho; coluna++) {
            if (tabuleiro[linha][coluna] == 0) { // Verifica se a posição é segura para colocar a rainha
                colocarRainha(linha, coluna); // Coloca a rainha na posição (linha, coluna)
                posicionarRainhas(linha + 1); // Tenta posicionar a próxima rainha na próxima linha
                removerRainha(linha, coluna); // Remove a rainha para tentar a próxima posição (backtracking)
            }
        }
    }

    // Método para posicionar uma rainha na posição (linha, coluna) e atualizar o tabuleiro
    private void colocarRainha(int linha, int coluna) {
        tabuleiro[linha][coluna] = -1; // Coloca uma rainha na posição (linha, coluna)

        // Atualiza as posições na mesma linha, coluna e diagonais
        for (int i = 0; i < tamanho; i++) {
            if (i != coluna) {
                tabuleiro[linha][i]++; // Atualiza a linha
            }
        }

        for (int i = 0; i < tamanho; i++) {
            if (i != linha) {
                tabuleiro[i][coluna]++; // Atualiza a coluna
            }
        }

        // Atualiza as diagonais
        for (int i = 1; linha + i < tamanho && coluna + i < tamanho; i++) {
            tabuleiro[linha + i][coluna + i]++;
        }
        for (int i = 1; linha + i < tamanho && coluna - i >= 0; i++) {
            tabuleiro[linha + i][coluna - i]++;
        }
        for (int i = 1; linha - i >= 0 && coluna + i < tamanho; i++) {
            tabuleiro[linha - i][coluna + i]++;
        }
        for (int i = 1; linha - i >= 0 && coluna - i >= 0; i++) {
            tabuleiro[linha - i][coluna - i]++;
        }
    }

    // Método para remover uma rainha da posição (linha, coluna) e restaurar o tabuleiro
    private void removerRainha(int linha, int coluna) {
        tabuleiro[linha][coluna] = 0; // Remove a rainha da posição (linha, coluna)

        // Reverte as atualizações feitas na linha, coluna e diagonais
        for (int i = 0; i < tamanho; i++) {
            if (i != coluna) {
                tabuleiro[linha][i]--;
            }
        }

        for (int i = 0; i < tamanho; i++) {
            if (i != linha) {
                tabuleiro[i][coluna]--;
            }
        }

        for (int i = 1; linha + i < tamanho && coluna + i < tamanho; i++) {
            tabuleiro[linha + i][coluna + i]--;
        }
        for (int i = 1; linha + i < tamanho && coluna - i >= 0; i++) {
            tabuleiro[linha + i][coluna - i]--;
        }
        for (int i = 1; linha - i >= 0 && coluna + i < tamanho; i++) {
            tabuleiro[linha - i][coluna + i]--;
        }
        for (int i = 1; linha - i >= 0 && coluna - i >= 0; i++) {
            tabuleiro[linha - i][coluna - i]--;
        }
    }

    // Método para adicionar a configuração atual do tabuleiro como uma solução válida
    private void adicionarSolucao() {
        int[] solucao = new int[tamanho];
        for (int linha = 0; linha < tamanho; linha++) {
            for (int coluna = 0; coluna < tamanho; coluna++) {
                if (tabuleiro[linha][coluna] == -1) { // Se há uma rainha na posição (linha, coluna)
                    solucao[linha] = coluna; // Armazena a coluna onde a rainha foi posicionada
                    break;
                }
            }
        }
        solucoes.add(solucao); // Adiciona a solução à lista de soluções
    }

    // Método para retornar todas as soluções encontradas
    public List<int[]> getSolucoes() {
        return solucoes;
    }

    // Método principal para executar o programa
    public static void main(String[] args) {
        int tamanho = 15; // Tamanho do tabuleiro (NxN)
        NxNQueens nRainhas = new NxNQueens(tamanho); // Cria uma instância do problema NxN Queens

        nRainhas.resolver(); // Resolve o problema

        List<int[]> solucoes = nRainhas.getSolucoes(); // Obtém todas as soluções
        System.out.println("Numero de solucoes: " + solucoes.size()); // Exibe o número de soluções encontradas
    }
}
