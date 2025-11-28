// implementa tabela hash com encadeamento separado e com funcoes de divisao, multiplicacao e dobramento
public class TabelaHash {
    public static final int FUNC_DIVISAO = 1;
    public static final int FUNC_MULTIPLICACAO = 2;
    public static final int FUNC_DOBRAMENTO = 3;
    public static final double A_MULTIPLICACAO = 0.6180339887;
    
    private No[] tabela;
    private int tamanho;
    private int funcaoHash;
    private int colisoesTabela;
    private int colisoesLista;
    private int[] primeirosHashes;
    private int contadorHashes;
    
    public TabelaHash(int tamanho, int funcaoHash) {
        this.tamanho = tamanho;
        this.funcaoHash = funcaoHash;
        this.tabela = new No[tamanho];
        this.colisoesTabela = 0;
        this.colisoesLista = 0;
        this.primeirosHashes = new int[10];
        this.contadorHashes = 0;
        
        int i = 0;
        while (i < tamanho) {
            this.tabela[i] = null;
            i = i + 1;
        }
    }
    
    private int calcularHash(int chave) {
        int hash = 0;
        
        if (funcaoHash == FUNC_DIVISAO) {
            hash = chave % tamanho;
            if (hash < 0) {
                hash = hash + tamanho;
            }
        }
        
        if (funcaoHash == FUNC_MULTIPLICACAO) {
            double produto = chave * A_MULTIPLICACAO;
            double fracao = produto - (int)produto;
            if (fracao < 0) {
                fracao = fracao + 1.0;
            }
            hash = (int)(tamanho * fracao);
        }
        
        if (funcaoHash == FUNC_DOBRAMENTO) {
            int soma = 0;
            int k = chave;
            if (k < 0) {
                k = -k;
            }
            while (k > 0) {
                int bloco = k % 1000;
                soma = soma + bloco;
                k = k / 1000;
            }
            
            hash = soma % tamanho;
            if (hash < 0) {
                hash = hash + tamanho;
            }
        }
        
        return hash;
    }
    
    public void inserir(Registro registro) {
        int hash = calcularHash(registro.codigo);
        
        if (contadorHashes < 10) {
            primeirosHashes[contadorHashes] = hash;
            contadorHashes = contadorHashes + 1;
        }
        
        No novoNo = new No(registro);
        
        if (tabela[hash] == null) {
            tabela[hash] = novoNo;
        } else {
            colisoesTabela = colisoesTabela + 1;
            
            No atual = tabela[hash];
            int posicao = 0;
            
            while (atual.proximo != null) {
                atual = atual.proximo;
                posicao = posicao + 1;
            }
            
            atual.proximo = novoNo;
            colisoesLista = colisoesLista + posicao + 1;
        }
    }
    
    public int buscar(int codigo) {
        int hash = calcularHash(codigo);
        int comparacoes = 0;
        
        No atual = tabela[hash];
        
        while (atual != null) {
            comparacoes = comparacoes + 1;
            
            if (atual.registro.codigo == codigo) {
                return comparacoes;
            }
            
            atual = atual.proximo;
        }
        
        return comparacoes;
    }
    
    public int obterColisoesTabela() {
        return colisoesTabela;
    }
    
    public int obterColisoesLista() {
        return colisoesLista;
    }
    
    public int obterChecksum() {
        int soma = 0;
        int i = 0;
        
        while (i < contadorHashes) {
            soma = soma + primeirosHashes[i];
            i = i + 1;
        }
        
        return soma % 1000003;
    }
}
