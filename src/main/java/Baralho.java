import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Gerencia o baralho de cartas do jogador durante o combate.
 * <p>
 * O baralho é dividido em três zonas principais, assim como em jogos de cartas clássicos:
 * <ul>
 * <li><b>Pilha de Compra:</b> Cartas disponíveis para serem sacadas.</li>
 * <li><b>Mão do Jogador:</b> Cartas que podem ser jogadas no turno atual.</li>
 * <li><b>Pilha de Descarte:</b> Cartas já utilizadas ou descartadas, que serão recicladas 
 * quando a pilha de compra esvaziar.</li>
 * </ul>
 * </p>
 */
public class Baralho {

    private ArrayList<Carta> pilhaCompra;
    private ArrayList<Carta> maoJogador;
    private ArrayList<Carta> pilhaDescarte;

    /**
     * Construtor padrão que inicializa as três zonas do baralho como listas vazias.
     */
    public Baralho() {
        pilhaCompra = new ArrayList<>();
        maoJogador = new ArrayList<>();
        pilhaDescarte = new ArrayList<>();
    }

    /**
     * Adiciona uma nova carta diretamente à pilha de compra.
     * @param carta A carta a ser adicionada.
     */
    public void adicionarCarta(Carta carta) {
        pilhaCompra.add(carta);
    }

    /**
     * Embaralha as cartas que estão atualmente na pilha de compra.
     */
    public void embaralhar() {
        Collections.shuffle(pilhaCompra);
    }

    /**
     * Puxa um número específico de cartas da pilha de compra para a mão do jogador.
     * <p>
     * Se a pilha de compra esvaziar durante o processo, a pilha de descarte é 
     * automaticamente reciclada e embaralhada para formar uma nova pilha de compra.
     * </p>
     * @param quantidade O número de cartas a serem compradas.
     */
    public void comprarCartas(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            if (pilhaCompra.isEmpty()) {
                reciclarDescarte();
            }

            if (!pilhaCompra.isEmpty()) {
                Carta carta = pilhaCompra.remove(0);
                maoJogador.add(carta);
                System.out.println(" > Você comprou: " + carta.getNome());
            }
        }
    }

    /**
     * Tenta jogar uma carta da mão do jogador.
     * <p>
     * O método verifica se a carta existe e se o jogador possui energia suficiente.
     * Se jogada com sucesso, a carta aplica seu efeito (método {@code usar}) e é enviada 
     * para a pilha de descarte.
     * </p>
     * @param index O índice da carta na mão do jogador.
     * @param alvo A entidade inimiga que sofrerá o efeito (se a carta precisar de alvo).
     * @param heroi A entidade do jogador que está jogando a carta.
     * @param energy A quantidade de energia/mana que o jogador possui atualmente.
     * @param publisher O gerenciador de eventos do jogo.
     * @return O custo de energia da carta (para ser subtraído do jogador), ou {@code -1} caso a jogada falhe.
     */
    public int usarCarta(int index, Entidade alvo, Entidade heroi, int energy, Publisher publisher) {

        if (index < 0 || index >= maoJogador.size()) {
            System.out.println("Carta inválida");
            return -1;
        }

        Carta carta = maoJogador.remove(index);

        if(carta.getCusto() > energy){
            // Devolve a carta para a mão se não tiver energia suficiente
            maoJogador.add(index, carta); 
            return -1;
        }

        carta.usar(heroi, alvo, publisher);

        pilhaDescarte.add(carta);

        return carta.getCusto();
    }

    /**
     * Transfere todas as cartas da pilha de descarte de volta para a pilha de compra
     * e as embaralha. Chamado automaticamente quando o jogador tenta comprar uma carta
     * e a pilha de compra está vazia.
     */
    private void reciclarDescarte() {
        if (pilhaDescarte.isEmpty()) {
            return;
        }

        pilhaCompra.addAll(pilhaDescarte);
        pilhaDescarte.clear();
        Collections.shuffle(pilhaCompra);

        System.out.println("Pilha de compra reciclada!");
    }

    /**
     * Exibe no terminal as cartas atualmente na mão do jogador, formatadas
     * com seus índices, custos e descrições.
     */
    public void mostrarMao() {

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║            CARTAS NA MÃO             ║");
        System.out.println("╠══════════════════════════════════════╣");

        if (maoJogador.isEmpty()) {
            System.out.println("║ Nenhuma carta disponível             ║");
        } else {
            for (int i = 0; i < maoJogador.size(); i++) {
                Carta carta = maoJogador.get(i);
                String line1 = String.format("%d : %s (Custo: %d)", i, carta.getNome(), carta.getCusto());
                String line2 = "    " + carta.getDescricao();

                System.out.printf("║ %-36s ║\n", line1);
                System.out.printf("║ %-36s ║\n", line2);
            }
        }

        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ -1 : Encerrar turno                  ║");
        System.out.println("║ -2 : Descartar uma carta             ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    /**
     * Descarta uma única carta da mão do jogador, enviando-a para a pilha de descarte.
     * @param index O índice da carta na mão.
     * @return {@code true} se a carta foi descartada com sucesso, {@code false} se o índice for inválido.
     */
    public boolean descartarCarta(int index) {
        if (index < 0 || index >= maoJogador.size()) {
            return false;
        }
        Carta carta = maoJogador.remove(index);
        pilhaDescarte.add(carta);
        System.out.println(" > Você descartou: " + carta.getNome());
        return true;
    }

    /**
     * Esvazia completamente a mão do jogador, movendo todas as cartas para a pilha de descarte.
     * Normalmente chamado no final do turno do jogador.
     */
    public void descartarMao() {
        pilhaDescarte.addAll(maoJogador);
        maoJogador.clear();
    }

    /**
     * @return O número de cartas restantes na pilha de compra.
     */
    public int tamanhoCompra() {
        return pilhaCompra.size();
    }

    /**
     * @return O número de cartas atualmente na pilha de descarte.
     */
    public int tamanhoDescarte() {
        return pilhaDescarte.size();
    }

    /**
     * @return O número de cartas atualmente na mão do jogador.
     */
    public int tamanhoMao() {
        return maoJogador.size();
    }

    /**
     * Obtém uma referência para uma carta específica na mão do jogador sem removê-la.
     * @param index O índice da carta.
     * @return A {@link Carta} solicitada ou {@code null} se o índice for inválido.
     */
    public Carta getCarta(int index) {
        if (index < 0 || index >= maoJogador.size()) return null;
        return maoJogador.get(index);
    }

    /**
     * Gera cartas aleatórias de diferentes tipos (Dano, Escudo, Veneno, Regeneração)
     * para preencher o baralho inicial do jogador.
     * @param quantidade O número de cartas a serem geradas e adicionadas ao baralho.
     */
    public void popularBaralho(int quantidade) {

        String[] nomesDano = {"Golpe", "Apunhalada", "Explosão Sangrenta", "Corte Rápido", "Fúria"};
        String[] nomesEscudo = {"Defesa", "Barreira", "Escudo de Ferro", "Esquiva", "Muralha"};
        // NOVO: Nomes para as cartas de efeito
        String[] nomesEfeitoVeneno = {"Frasco Venenoso", "Nuvem Tóxica", "Toque Letal"}; 
        String[] nomesEfeitoCura = {"Poção de Vida", "Bandagem Mágica", "Regeneração"};

        Random gerador = new Random();
        for (int i = 0; i < quantidade; i++) {
            int custo = gerador.nextInt(3) + 1;
            int valor = gerador.nextInt(6) + 2;
            
            int tipoDeCarta = gerador.nextInt(3); 

            if (tipoDeCarta == 0) {
                String nome = nomesDano[gerador.nextInt(nomesDano.length)];
                CartaDano carta = new CartaDano(nome, custo, "Causa " + valor + " de dano ao inimigo", valor);
                adicionarCarta(carta);

            } else if (tipoDeCarta == 1) {
                String nome = nomesEscudo[gerador.nextInt(nomesEscudo.length)];
                CartaEscudo carta = new CartaEscudo(nome, custo, "Ganha " + valor + " de Escudo", valor);
                adicionarCarta(carta);

            } else {
                boolean sorteiaEfeito = gerador.nextBoolean();
                
                if (sorteiaEfeito) {
                    String nome = nomesEfeitoVeneno[gerador.nextInt(nomesEfeitoVeneno.length)];
                    int acumulos = (valor / 2) + 1; 
                    CartaEfeito carta = new CartaEfeito(nome, custo, "Aplica " + acumulos + " de Veneno", acumulos, "Veneno");
                    adicionarCarta(carta);
                } else {
                    String nome = nomesEfeitoCura[gerador.nextInt(nomesEfeitoCura.length)];
                    int acumulos = (valor / 2) + 1;
                    CartaEfeito carta = new CartaEfeito(nome, custo, "Ganha " + acumulos + " de Regeneração", acumulos, "Regeneracao");
                    adicionarCarta(carta);
                }
            }
        }
    }

    /**
     * Exibe no terminal as últimas cartas que foram enviadas para a pilha de descarte.
     * Mostra até 5 cartas, do topo da pilha para baixo.
     */
    public void mostrarDescarte() {
        System.out.println("\n╔════════════════════════════╗");
        System.out.println("║     PILHA DE DESCARTE      ║");
        System.out.println("╠════════════════════════════╣");

        if (pilhaDescarte.isEmpty()) {
            System.out.println("║ (vazia)                    ║");
        } else {
            int limite = Math.min(5, pilhaDescarte.size());

            // Percorre a lista de trás para frente para mostrar o "topo" do descarte
            for (int i = pilhaDescarte.size() - limite; i < pilhaDescarte.size(); i++) {
                String nome = pilhaDescarte.get(i).getNome();
                System.out.printf("║ %-26s ║%n", nome);
            }

            if (pilhaDescarte.size() > 5) {
                System.out.printf("║ ... (%d cartas no total)   ║%n", pilhaDescarte.size());
            }
        }

        System.out.println("╚════════════════════════════╝");
    }
}