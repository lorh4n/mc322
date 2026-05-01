import java.util.ArrayList;
import java.util.List;

/**
 * Representa o mapa de progressão do jogo como uma árvore de nós.
 */
public class Mapa {

    private final NoMapa raiz;

    public Mapa(NoMapa raiz) {
        this.raiz = raiz;
    }

    public NoMapa getRaiz() { return raiz; }

    /**
     * Constrói um mapa padrão de exemplo.
     * @param heroi Referência do herói para as batalhas.
     * @param baralho Referência do baralho para as batalhas.
     * @return Novo mapa padrão.
     */
    public static Mapa gerarPadrao(Heroi heroi, Baralho baralho) {
        NoMapa chefe = new NoMapa("Chefe Final", 4,
            new Batalha(heroi, baralho, () -> {
                List<Inimigo> lst = new ArrayList<>();
                lst.add(new Rato(16, 5));
                lst.add(new Rato(16, 5));
                return lst;
            }));

        NoMapa caverna = new NoMapa("Caverna Sombria", 2,
            new Batalha(heroi, baralho, () -> {
                List<Inimigo> lst = new ArrayList<>();
                lst.add(new Rato(12, 4));
                lst.add(new Rato(8, 3));
                return lst;
            }));
        NoMapa ruinas = new NoMapa("Ruínas Antigas", 2,
            new Batalha(heroi, baralho, () -> {
                List<Inimigo> lst = new ArrayList<>();
                lst.add(new Rato(14, 3));
                return lst;
            }));
        NoMapa loja = new NoMapa("Loja do Mercador", 3, new Loja(baralho));
        NoMapa altar = new NoMapa("Altar Esquecido", 3, new Escolha(baralho));
        NoMapa torre = new NoMapa("Torre Esquecida", 3,
            new Batalha(heroi, baralho, () -> {
                List<Inimigo> lst = new ArrayList<>();
                lst.add(new Rato(10, 4));
                lst.add(new Rato(10, 4));
                return lst;
            }));

        caverna.adicionarFilho(loja);
        caverna.adicionarFilho(altar);
        ruinas.adicionarFilho(altar);
        torre.adicionarFilho(chefe);
        loja.adicionarFilho(chefe);
        altar.adicionarFilho(chefe);

        NoMapa floresta = new NoMapa("Floresta Sussurrante", 1,
            new Batalha(heroi, baralho, () -> {
                List<Inimigo> lst = new ArrayList<>();
                lst.add(new Rato(8, 3));
                lst.add(new Rato(6, 2));
                return lst;
            }));
        NoMapa pantano = new NoMapa("Pântano Tóxico", 1,
            new Batalha(heroi, baralho, () -> {
                List<Inimigo> lst = new ArrayList<>();
                lst.add(new Rato(10, 3));
                return lst;
            }));

        floresta.adicionarFilho(caverna);
        floresta.adicionarFilho(ruinas);
        pantano.adicionarFilho(ruinas);
        pantano.adicionarFilho(torre);

        NoMapa raiz = new NoMapa("Início da Jornada", 0, null);
        raiz.adicionarFilho(floresta);
        raiz.adicionarFilho(pantano);

        return new Mapa(raiz);
    }
}
