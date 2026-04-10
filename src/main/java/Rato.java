/**
 * Representa um inimigo específico do tipo "Rato" no jogo.
 * <p>
 * Herda de {@link Inimigo} e possui um padrão de ataque fixo e alternado:
 * ele sempre ataca no primeiro turno e aplica um debuff de {@link Veneno} no turno seguinte,
 * repetindo esse ciclo infinitamente.
 * </p>
 */
public class Rato extends Inimigo {

    /** Código ANSI para colorir a saída de texto do Rato de amarelo no terminal. */
    public static final String CorOutput = "\u001B[33m";
    /** Código ANSI para resetar a cor do terminal para o padrão. */
    public static final String Reset = "\u001B[0m";

    /** * Índice que controla a máquina de estados do inimigo para decidir qual será a sua próxima ação.
     * Alterna entre 0 (Ataque físico) e 1 (Aplicar Veneno).
     */
    private int acaoIndex = 0;

    /**
     * Construtor para criar um novo Rato.
     * O nome é fixado automaticamente como "Rato" para a classe mãe.
     * * @param vida Os pontos de vida iniciais (e máximos) do Rato.
     * @param dano O valor base do ataque físico do Rato.
     */
    public Rato(int vida, int dano) {
        super("Rato", vida, dano);
    }

    /**
     * Executa a ação do Rato no turno atual com base no seu padrão de ataque ({@code acaoIndex}).
     * <p>
     * Se o índice for 0, o Rato realiza um ataque físico básico.
     * Se o índice for 1, o Rato aplica 2 acúmulos de Veneno ao Herói.
     * Após a ação, o índice é atualizado para o próximo estado.
     * </p>
     * * @param alvo O {@link Heroi} que sofrerá o ataque ou o debuff.
     * @param publisher O gerenciador de eventos do jogo, usado para registrar o efeito de Veneno.
     */
    @Override
    public void executarAcao(Heroi alvo, Publisher publisher) {
        if (acaoIndex == 0) {
            atacar(alvo);
        } else {
            Efeito veneno = new Veneno(alvo, 2, publisher);
            alvo.aplicarEfeito(veneno, publisher);
            System.out.printf("║ > %-33s ║%n", getNome() + " aplicou 2 de Veneno!");
        }
        // Alterna o índice entre 0 e 1 usando o operador de módulo (resto da divisão)
        acaoIndex = (acaoIndex + 1) % 2;
    }

    /**
     * Anuncia o que o Rato fará no seu próximo turno, permitindo que o jogador se prepare.
     * * @return Uma string avisando se o Rato pretende causar dano físico ou aplicar Veneno.
     */
    @Override
    public String anunciarIntencao() {
        if (acaoIndex == 0) {
            return "Pretende causar " + this.dano + " de dano";
        } else {
            return "Pretende aplicar 2 de Veneno";
        }
    }
}