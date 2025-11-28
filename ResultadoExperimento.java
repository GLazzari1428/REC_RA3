// gera as linhas do CSV com os resultados
public class ResultadoExperimento {
    public int m;
    public int n;
    public String funcao;
    public long seed;
    public long tempoInsercaoMs;
    public int colisoesTabela;
    public int colisoesLista;
    public long tempoBuscaHitsMs;
    public long tempoBuscaMissesMs;
    public long comparacoesHits;
    public long comparacoesMisses;
    public int checksum;
    
    // converte o resultado para formato CSV
    public String paraCSV() {
        String linha = "";
        linha = linha + m;
        linha = linha + ",";
        linha = linha + n;
        linha = linha + ",";
        linha = linha + funcao;
        linha = linha + ",";
        linha = linha + seed;
        linha = linha + ",";
        linha = linha + tempoInsercaoMs;
        linha = linha + ",";
        linha = linha + colisoesTabela;
        linha = linha + ",";
        linha = linha + colisoesLista;
        linha = linha + ",";
        linha = linha + tempoBuscaHitsMs;
        linha = linha + ",";
        linha = linha + tempoBuscaMissesMs;
        linha = linha + ",";
        linha = linha + comparacoesHits;
        linha = linha + ",";
        linha = linha + comparacoesMisses;
        linha = linha + ",";
        linha = linha + checksum;
        
        return linha;
    }
    
    // imprime o resultado no console
    public void imprimir() {
        System.out.println("Checksum: " + checksum);
        System.out.println("Tempo Insercao: " + tempoInsercaoMs + " ms");
        System.out.println("Colisoes Tabela: " + colisoesTabela);
        System.out.println("Colisoes Lista: " + colisoesLista);
        System.out.println("Tempo Busca Hits: " + tempoBuscaHitsMs + " ms");
        System.out.println("Tempo Busca Misses: " + tempoBuscaMissesMs + " ms");
        System.out.println("Comparacoes Hits (media): " + comparacoesHits);
        System.out.println("Comparacoes Misses (media): " + comparacoesMisses);
        System.out.println();
    }
}
