//representa um no da lista encadeada simples
public class No {
    public Registro registro;
    public No proximo;
    
    public No(Registro registro) {
        this.registro = registro;
        this.proximo = null;
    }
}
