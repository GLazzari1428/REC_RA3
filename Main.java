import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

// Classe Main, gera o arquivo CSV com os resultados
public class Main {
    public static final int[] TAMANHOS_TABELA = {1009, 10007, 100003};
    public static final int[] TAMANHOS_DATASET = {1000, 10000, 100000};
    public static final long[] SEEDS = {137, 271828, 314159};
    public static final int[] FUNCOES = {
        TabelaHash.FUNC_DIVISAO,
        TabelaHash.FUNC_MULTIPLICACAO,
        TabelaHash.FUNC_DOBRAMENTO
    };
    
    public static void main(String[] args) {
        System.out.println("=== Experimentos de Tabela Hash ===");
        System.out.println();
        
        ResultadoExperimento[] resultados = executarTodosExperimentos();
        
        salvarCSV(resultados);
        
        System.out.println("Experimentos concluidos. Arquivo resultados.csv gerado.");
    }
    
    // executa todos os experimentos combinando parametros
    private static ResultadoExperimento[] executarTodosExperimentos() {
        int totalExperimentos = TAMANHOS_TABELA.length * 
                               TAMANHOS_DATASET.length * 
                               FUNCOES.length * 
                               SEEDS.length;
        
        ResultadoExperimento[] resultados = new ResultadoExperimento[totalExperimentos];
        int indice = 0;
        
        int im = 0;
        while (im < TAMANHOS_TABELA.length) {
            int m = TAMANHOS_TABELA[im];
            
            int in = 0;
            while (in < TAMANHOS_DATASET.length) {
                int n = TAMANHOS_DATASET[in];
                
                int ifunc = 0;
                while (ifunc < FUNCOES.length) {
                    int funcao = FUNCOES[ifunc];
                    
                    int iseed = 0;
                    while (iseed < SEEDS.length) {
                        long seed = SEEDS[iseed];
                        
                        System.out.println("Executando: m=" + m + 
                                         ", n=" + n + 
                                         ", seed=" + seed);
                        
                        Experimento exp = new Experimento(m, n, funcao, seed);
                        ResultadoExperimento resultado = exp.executar();
                        resultado.imprimir();
                        
                        resultados[indice] = resultado;
                        indice = indice + 1;
                        
                        iseed = iseed + 1;
                    }
                    
                    ifunc = ifunc + 1;
                }
                
                in = in + 1;
            }
            
            im = im + 1;
        }
        
        return resultados;
    }
    
    // salva os resultados em arquivo CSV
    private static void salvarCSV(ResultadoExperimento[] resultados) {
        try {
            FileWriter fw = new FileWriter("resultados.csv");
            PrintWriter pw = new PrintWriter(fw);
            
            pw.println("m,n,func,seed,ins_ms,coll_tbl,coll_lst,find_ms_hits,find_ms_misses,cmp_hits,cmp_misses,checksum");
            
            int i = 0;
            while (i < resultados.length) {
                pw.println(resultados[i].paraCSV());
                i = i + 1;
            }
            
            pw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar CSV");
        }
    }
}
