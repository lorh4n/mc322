import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Representa um nó do mapa de progressão do jogo.
 * <p>
 * Cada nó possui uma profundidade (distância da raiz), uma fábrica de inimigos
 * que gera a batalha correspondente ao nó e uma lista de filhos para onde o
 * jogador pode progredir. A regra de "sempre para frente" é implementada através
 * do campo {@code profundidade}: filhos devem ter profundidade maior que o pai.
 * </p>
 */
public class NoMapa {

    private final String nome;
    private final int profundidade;
    private final Supplier<List<Inimigo>> fabricaInimigos;
    private final List<NoMapa> filhos = new ArrayList<>();
    private boolean visitado = false;
    private final boolean ehFinal;

    /**
     * Cria um novo nó do mapa.
     * @param nome Nome descritivo do nó (para exibição no terminal).
     * @param profundidade Distância da raiz. Raiz tem profundidade 0.
     * @param fabricaInimigos Fábrica que gera a lista de inimigos para a batalha
     *        deste nó. Usa-se fábrica (não lista direta) para permitir instâncias
     *        novas a cada acesso e manter a raiz "sem batalha" quando {@code null}.
     * @param ehFinal {@code true} se este nó encerra o jogo em vitória quando vencido.
     */
    public NoMapa(String nome, int profundidade, Supplier<List<Inimigo>> fabricaInimigos, boolean ehFinal) {
        this.nome = nome;
        this.profundidade = profundidade;
        this.fabricaInimigos = fabricaInimigos;
        this.ehFinal = ehFinal;
    }

    /**
     * Adiciona um filho ao nó. Só adiciona se a profundidade do filho for maior
     * que a do pai, garantindo que o jogador sempre avance (nunca volte).
     * @param filho Nó filho.
     */
    public void adicionarFilho(NoMapa filho) {
        if (filho.profundidade <= this.profundidade) {
            throw new IllegalArgumentException(
                "Filho deve ter profundidade maior que o pai: "
                + this.nome + "(" + this.profundidade + ") -> "
                + filho.nome + "(" + filho.profundidade + ")");
        }
        filhos.add(filho);
    }

    /** @return Nome do nó. */
    public String getNome() { return nome; }

    /** @return Profundidade do nó na árvore (0 para raiz). */
    public int getProfundidade() { return profundidade; }

    /** @return Lista de filhos (nós adjacentes acessíveis a partir deste). */
    public List<NoMapa> getFilhos() { return filhos; }

    /** @return {@code true} se o nó já foi visitado pelo jogador. */
    public boolean isVisitado() { return visitado; }

    /** Marca o nó como visitado. */
    public void marcarVisitado() { this.visitado = true; }

    /** @return {@code true} se este é um nó final (vitória do jogo). */
    public boolean isEhFinal() { return ehFinal; }

    /**
     * Gera uma nova instância da lista de inimigos para a batalha deste nó.
     * @return Nova lista de inimigos, ou {@code null} se o nó não tem batalha (raiz).
     */
    public List<Inimigo> criarInimigos() {
        return fabricaInimigos == null ? null : fabricaInimigos.get();
    }

    /** @return {@code true} se este nó possui uma batalha associada. */
    public boolean temBatalha() {
        return fabricaInimigos != null;
    }
}
