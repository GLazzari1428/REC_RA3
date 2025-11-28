// executa os experimentos e coleta metricas
public class Experimento {
    public static final int REPETICOES = 5;
    public static final int PERCENTUAL_HITS = 50;
    public static final int TAMANHO_LOTE_BUSCA = 1000;
    
    private int m;
    private int n;
    private int funcao;
    private long seed;
    private TabelaHash tabela;
    private int[] dados;
    
    // construtor: inicializa o experimento com parametros especificados
    public Experimento(int m, int n, int funcao, long seed) {
        this.m = m;
        this.n = n;
        this.funcao = funcao;
        this.seed = seed;
    }
    
    // imprime a etiqueta da funcao de hashing
    private void imprimirEtiqueta() {
        if (funcao == TabelaHash.FUNC_DIVISAO) {
            System.out.println("H_DIV m=" + m + " seed=" + seed);
        }
        if (funcao == TabelaHash.FUNC_MULTIPLICACAO) {
            System.out.println("H_MUL m=" + m + " seed=" + seed);
        }
        if (funcao == TabelaHash.FUNC_DOBRAMENTO) {
            System.out.println("H_FOLD m=" + m + " seed=" + seed);
        }
    }
    
    // executa o experimento completo e retorna os resultados
    public ResultadoExperimento executar() {
        imprimirEtiqueta();
        
        GeradorDados gerador = new GeradorDados(seed);
        dados = gerador.gerarDados(n);
        
        long tempoInsercao = medirInsercao();
        
        ResultadoBusca resultadoBusca = medirBusca();
        
        ResultadoExperimento resultado = new ResultadoExperimento();
        resultado.m = m;
        resultado.n = n;
        resultado.funcao = obterNomeFuncao();
        resultado.seed = seed;
        resultado.tempoInsercaoMs = tempoInsercao;
        resultado.colisoesTabela = tabela.obterColisoesTabela();
        resultado.colisoesLista = tabela.obterColisoesLista();
        resultado.tempoBuscaHitsMs = resultadoBusca.tempoHits;
        resultado.tempoBuscaMissesMs = resultadoBusca.tempoMisses;
        resultado.comparacoesHits = resultadoBusca.comparacoesHits;
        resultado.comparacoesMisses = resultadoBusca.comparacoesMisses;
        resultado.checksum = tabela.obterChecksum();
        
        return resultado;
    }
    
    // mede o tempo de insercao de todos os elementos
    private long medirInsercao() {
        long somaTempos = 0;
        int r = 0;
        
        while (r < REPETICOES) {
            tabela = new TabelaHash(m, funcao);
            
            long inicio = System.currentTimeMillis();
            
            int i = 0;
            while (i < n) {
                Registro registro = new Registro(dados[i]);
                tabela.inserir(registro);
                i = i + 1;
            }
            
            long fim = System.currentTimeMillis();
            somaTempos = somaTempos + (fim - inicio);
            
            r = r + 1;
        }
        
        return somaTempos / REPETICOES;
    }
    
    // mede tempos e comparacoes de busca separando hits e misses
    private ResultadoBusca medirBusca() {
        int tamanhoHits = TAMANHO_LOTE_BUSCA * PERCENTUAL_HITS / 100;
        int tamanhoMisses = TAMANHO_LOTE_BUSCA - tamanhoHits;
        
        int[] chavesHits = gerarChavesHits(tamanhoHits);
        int[] chavesMisses = gerarChavesMisses(tamanhoMisses);
        
        long somaTempoHits = 0;
        long somaTempoMisses = 0;
        long somaComparacoesHits = 0;
        long somaComparacoesMisses = 0;
        
        int r = 0;
        while (r < REPETICOES) {
            long inicio = System.currentTimeMillis();
            long comparacoes = 0;
            
            int i = 0;
            while (i < tamanhoHits) {
                comparacoes = comparacoes + tabela.buscar(chavesHits[i]);
                i = i + 1;
            }
            
            long fim = System.currentTimeMillis();
            somaTempoHits = somaTempoHits + (fim - inicio);
            somaComparacoesHits = somaComparacoesHits + comparacoes;
            
            inicio = System.currentTimeMillis();
            comparacoes = 0;
            
            i = 0;
            while (i < tamanhoMisses) {
                comparacoes = comparacoes + tabela.buscar(chavesMisses[i]);
                i = i + 1;
            }
            
            fim = System.currentTimeMillis();
            somaTempoMisses = somaTempoMisses + (fim - inicio);
            somaComparacoesMisses = somaComparacoesMisses + comparacoes;
            
            r = r + 1;
        }
        
        ResultadoBusca resultado = new ResultadoBusca();
        resultado.tempoHits = somaTempoHits / REPETICOES;
        resultado.tempoMisses = somaTempoMisses / REPETICOES;
        resultado.comparacoesHits = somaComparacoesHits / REPETICOES / tamanhoHits;
        resultado.comparacoesMisses = somaComparacoesMisses / REPETICOES / tamanhoMisses;
        
        return resultado;
    }
    
    // gera chaves presentes na tabela para buscas bem-sucedidas
    private int[] gerarChavesHits(int quantidade) {
        int[] chaves = new int[quantidade];
        
        int i = 0;
        while (i < quantidade) {
            int indice = i % n;
            chaves[i] = dados[indice];
            i = i + 1;
        }
        
        embaralhar(chaves);
        return chaves;
    }
    
    // gera chaves ausentes na tabela para buscas malsucedidas
    private int[] gerarChavesMisses(int quantidade) {
        int[] chaves = new int[quantidade];
        GeradorDados gerador = new GeradorDados(seed + 999999);
        
        int i = 0;
        while (i < quantidade) {
            int[] temp = gerador.gerarDados(1);
            int chave = temp[0];
            
            if (chave != 0) {
                chave = chave + 1000000000;
                if (chave > 999999999) {
                    chave = 100000000;
                }
            }
            
            chaves[i] = chave;
            i = i + 1;
        }
        
        return chaves;
    }
    
    // embaralha um array de inteiros
    private void embaralhar(int[] array) {
        GeradorDados gerador = new GeradorDados(seed + 12345);
        int tamanho = array.length;
        
        int i = tamanho - 1;
        while (i > 0) {
            int[] temp = gerador.gerarDados(1);
            int j = temp[0] % (i + 1);
            if (j < 0) {
                j = -j;
            }
            
            int aux = array[i];
            array[i] = array[j];
            array[j] = aux;
            
            i = i - 1;
        }
    }
    
    // retorna o nome da funcao de hashing
    private String obterNomeFuncao() {
        if (funcao == TabelaHash.FUNC_DIVISAO) {
            return "H_DIV";
        }
        if (funcao == TabelaHash.FUNC_MULTIPLICACAO) {
            return "H_MUL";
        }
        if (funcao == TabelaHash.FUNC_DOBRAMENTO) {
            return "H_FOLD";
        }
        return "";
    }
    
    private class ResultadoBusca {
        public long tempoHits;
        public long tempoMisses;
        public long comparacoesHits;
        public long comparacoesMisses;
    }
}
