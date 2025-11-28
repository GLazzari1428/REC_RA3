// gera numeros inteiros de 9 digitos no intervalo especificado
public class GeradorDados {
    private static final int MIN_VALOR = 100000000;
    private static final int MAX_VALOR = 999999999;
    private static final int RANGE = MAX_VALOR - MIN_VALOR + 1;
    
    private long seed;
    
    public GeradorDados(long seed) {
        this.seed = seed;
    }

    private int proximoInt() {
        seed = (seed * 1103515245L + 12345L) & 0x7fffffffL;
        return (int)(seed % RANGE) + MIN_VALOR;
    }

    public int[] gerarDados(int n) {
        int[] dados = new int[n];
        int i = 0;
        
        while (i < n) {
            dados[i] = proximoInt();
            i = i + 1;
        }
        
        return dados;
    }
}
