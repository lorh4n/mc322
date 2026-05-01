import java.util.Random;

/**
 * Centraliza a criação das cartas usadas em recompensas, loja e baralho inicial.
 */
public class CartaFactory {

    private static final String[] NOMES_DANO =
        {"Golpe", "Apunhalada", "Explosão Sangrenta", "Corte Rápido", "Fúria"};
    private static final String[] NOMES_ESCUDO =
        {"Defesa", "Barreira", "Escudo de Ferro", "Esquiva", "Muralha"};
    private static final String[] NOMES_EFEITO_VENENO =
        {"Frasco Venenoso", "Nuvem Tóxica", "Toque Letal"};
    private static final String[] NOMES_EFEITO_CURA =
        {"Poção de Vida", "Bandagem Mágica", "Regeneração"};
    private static final String[] NOMES_ATAQUE_VENENOSO =
        {"Lâmina Envenenada", "Picada Mortal", "Golpe Tóxico"};
    private static final String[] NOMES_GOLPE_DUPLO =
        {"Rajada Dupla", "Duplo Corte", "Golpe Duplo"};
    private static final String[] NOMES_DRENO =
        {"Drenar Vida", "Mordida Vampírica", "Sifão"};
    private static final String[] NOMES_EXTIRPAR =
        {"Extirpar", "Purgar Veneno", "Drenar Toxina"};
    private static final String[] NOMES_ESCUDO_REGENERO =
        {"Bastião Vital", "Escudo Regenerativo", "Fortaleza Viva"};

    private CartaFactory() {}

    public static Carta criarAleatoria() {
        return criarAleatoria(new Random());
    }

    public static Carta criarAleatoria(Random gerador) {
        int custo = gerador.nextInt(3) + 1;
        int valor = gerador.nextInt(6) + 2;
        int tipoDeCarta = gerador.nextInt(8);

        if (tipoDeCarta == 0) {
            String nome = escolher(NOMES_DANO, gerador);
            return new CartaDano(nome, custo, "Causa " + valor + " de dano ao inimigo", valor);
        } else if (tipoDeCarta == 1) {
            String nome = escolher(NOMES_ESCUDO, gerador);
            return new CartaEscudo(nome, custo, "Ganha " + valor + " de Escudo", valor);
        } else if (tipoDeCarta == 2) {
            int acumulos = (valor / 2) + 1;
            if (gerador.nextBoolean()) {
                String nome = escolher(NOMES_EFEITO_VENENO, gerador);
                return new CartaEfeito(nome, custo, "Aplica " + acumulos + " de Veneno", acumulos, "Veneno");
            }
            String nome = escolher(NOMES_EFEITO_CURA, gerador);
            return new CartaEfeito(nome, custo, "Ganha " + acumulos + " de Regeneração", acumulos, "Regeneracao");
        } else if (tipoDeCarta == 3) {
            String nome = escolher(NOMES_ATAQUE_VENENOSO, gerador);
            int acumulos = (valor / 2) + 1;
            return new CartaAtaqueVenenoso(nome, custo,
                "Causa " + valor + " de dano e aplica " + acumulos + " de Veneno", valor, acumulos);
        } else if (tipoDeCarta == 4) {
            String nome = escolher(NOMES_GOLPE_DUPLO, gerador);
            int danoPorGolpe = Math.max(2, valor / 2);
            return new CartaGolpeDuplo(nome, custo,
                "Golpeia 2x causando " + danoPorGolpe + " de dano cada", danoPorGolpe);
        } else if (tipoDeCarta == 5) {
            String nome = escolher(NOMES_DRENO, gerador);
            int cura = Math.max(1, valor / 2);
            return new CartaDreno(nome, custo,
                "Causa " + valor + " de dano e cura " + cura + " de HP", valor, cura);
        } else if (tipoDeCarta == 6) {
            String nome = escolher(NOMES_EXTIRPAR, gerador);
            return new CartaExtirpar(nome, 1, "Remove Veneno do alvo e converte em dano");
        }

        String nome = escolher(NOMES_ESCUDO_REGENERO, gerador);
        int acumulos = (valor / 2) + 1;
        return new CartaEscudoRegenero(nome, custo,
            "Ganha " + valor + " de Escudo e " + acumulos + " de Regeneração", valor, acumulos);
    }

    private static String escolher(String[] opcoes, Random gerador) {
        return opcoes[gerador.nextInt(opcoes.length)];
    }
}
