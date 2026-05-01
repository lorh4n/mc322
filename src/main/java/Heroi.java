import java.util.ArrayList;
import java.util.List;

/**
 * Representa o personagem controlado pelo jogador no sistema de combate.
 * <p>
 * Esta classe herda de {@link Entidade}, o que significa que ela já possui 
 * nativamente todo o comportamento de receber dano, ganhar escudos e sofrer
 * efeitos de status. 
 * </p>
 */
public class Heroi extends Entidade {

    private int ouro;
    private final List<Reliquia> reliquias = new ArrayList<>();

    /**
     * Construtor padrão para inicializar o Herói.
     * <p>
     * Repassa as informações básicas para a classe mãe ({@link Entidade}) 
     * para configurar os atributos de combate.
     * </p>
     * @param nome O nome de exibição do herói.
     * @param vida Os pontos de vida iniciais (que também definirão o HP máximo).
     */
    public Heroi(String nome, int vida){
        super(nome, vida);
        this.ouro = 0;
    }

    public int getOuro() {
        return ouro;
    }

    public void adicionarOuro(int quantidade) {
        this.ouro += quantidade;
    }

    public boolean removerOuro(int quantidade) {
        if (this.ouro >= quantidade) {
            this.ouro -= quantidade;
            return true;
        }
        return false;
    }

    public List<Reliquia> getReliquias() {
        return reliquias;
    }

    public void adicionarReliquia(Reliquia reliquia) {
        if (reliquia != null) {
            this.reliquias.add(reliquia);
        }
    }
}
