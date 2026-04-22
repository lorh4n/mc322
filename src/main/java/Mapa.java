import java.util.ArrayList;
import java.util.List;

/**
 * Representa o mapa de progressão do jogo como uma árvore de nós.
 * <p>
 * O mapa é mantido entre batalhas: o herói inicia na raiz e progride escolhendo
 * um nó filho não visitado após cada vitória, até alcançar um nó final ou morrer.
 * </p>
 */
public class Mapa {

    private final NoMapa raiz;

    /**
     * Cria um mapa a partir da raiz.
     * @param raiz Nó inicial do mapa.
     */
    public Mapa(NoMapa raiz) {
        this.raiz = raiz;
    }

    /** @return Nó raiz do mapa. */
    public NoMapa getRaiz() { return raiz; }

    /**
     * Constrói um mapa padrão de exemplo com ramificação e convergência.
     * <pre>
     *            Início (raiz)
     *             /      \
     *         Floresta  Pântano
     *           / \      / \
     *      Caverna Ruínas  Torre
     *           \   |   |   /
     *           Chefe Final
     * </pre>
     * @return Novo mapa padrão.
     */
    public static Mapa gerarPadrao() {
        NoMapa chefe = new NoMapa("Chefe Final", 3,
            () -> {
                List<Inimigo> lst = new ArrayList<>();
                lst.add(new Rato(16, 5));
                lst.add(new Rato(16, 5));
                return lst;
            }, true);

        NoMapa caverna = new NoMapa("Caverna Sombria", 2,
            () -> {
                List<Inimigo> lst = new ArrayList<>();
                lst.add(new Rato(12, 4));
                lst.add(new Rato(8, 3));
                return lst;
            }, false);
        NoMapa ruinas = new NoMapa("Ruínas Antigas", 2,
            () -> {
                List<Inimigo> lst = new ArrayList<>();
                lst.add(new Rato(14, 3));
                return lst;
            }, false);
        NoMapa torre = new NoMapa("Torre Esquecida", 2,
            () -> {
                List<Inimigo> lst = new ArrayList<>();
                lst.add(new Rato(10, 4));
                lst.add(new Rato(10, 4));
                return lst;
            }, false);

        caverna.adicionarFilho(chefe);
        ruinas.adicionarFilho(chefe);
        torre.adicionarFilho(chefe);

        NoMapa floresta = new NoMapa("Floresta Sussurrante", 1,
            () -> {
                List<Inimigo> lst = new ArrayList<>();
                lst.add(new Rato(8, 3));
                lst.add(new Rato(6, 2));
                return lst;
            }, false);
        NoMapa pantano = new NoMapa("Pântano Tóxico", 1,
            () -> {
                List<Inimigo> lst = new ArrayList<>();
                lst.add(new Rato(10, 3));
                return lst;
            }, false);

        floresta.adicionarFilho(caverna);
        floresta.adicionarFilho(ruinas);
        pantano.adicionarFilho(ruinas);
        pantano.adicionarFilho(torre);

        NoMapa raiz = new NoMapa("Início da Jornada", 0, null, false);
        raiz.adicionarFilho(floresta);
        raiz.adicionarFilho(pantano);

        return new Mapa(raiz);
    }
}
