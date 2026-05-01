import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Representa um nó do mapa de progressão do jogo.
 */
public class NoMapa {

    private final String nome;
    private final int profundidade;
    private final Evento evento;
    private final List<NoMapa> filhos = new ArrayList<>();
    private boolean visitado = false;

    /**
     * Cria um novo nó do mapa.
     * @param nome Nome descritivo do nó.
     * @param profundidade Distância da raiz.
     * @param evento O evento que ocorrerá neste nó.
     */
    public NoMapa(String nome, int profundidade, Evento evento) {
        this.nome = nome;
        this.profundidade = profundidade;
        this.evento = evento;
    }

    public void adicionarFilho(NoMapa filho) {
        if (filho.profundidade <= this.profundidade) {
            throw new IllegalArgumentException(
                "Filho deve ter profundidade maior que o pai: "
                + this.nome + "(" + this.profundidade + ") -> "
                + filho.nome + "(" + filho.profundidade + ")");
        }
        filhos.add(filho);
    }

    public String getNome() { return nome; }
    public int getProfundidade() { return profundidade; }
    public List<NoMapa> getFilhos() { return filhos; }
    public boolean isVisitado() { return visitado; }
    public void marcarVisitado() { this.visitado = true; }

    public Evento getEvento() {
        return evento;
    }
}
