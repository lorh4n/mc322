/**
 * Representa a classe base abstrata para todos os efeitos de status (buffs ou debuffs) do jogo.
 * <p>
 * Esta classe implementa a interface {@link Subscriber}, o que significa que ela utiliza
 * o padrão de projeto Observer. Isso permite que os efeitos "escutem" e reajam a 
 * eventos específicos do sistema de combate (como o início ou o fim de um turno).
 * </p>
 */
public abstract class Efeito implements Subscriber {
    protected String nome;
    protected Entidade dono;
    protected int acumulos; 

    /**
     * Construtor padrão para a criação de um Efeito.
     * * @param nome O nome do efeito (ex: "Veneno", "Regeneração").
     * @param dono A entidade que está sofrendo ou sendo beneficiada por este efeito.
     * @param acumulos A intensidade ou duração inicial do efeito (ex: quantos turnos dura ou o valor do impacto).
     */
    public Efeito(String nome, Entidade dono, int acumulos) {
        this.nome = nome;
        this.dono = dono;
        this.acumulos = acumulos;
    }

    /**
     * Obtém o nome do efeito.
     * * @return O nome do efeito.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Obtém a quantidade atual de acúmulos deste efeito.
     * * @return O número de acúmulos.
     */
    public int getAcumulos() {
        return acumulos;
    }

    /**
     * Adiciona novos acúmulos ao efeito já existente.
     * <p>
     * Este método é essencial para mecânicas de "stack". Por exemplo, se uma entidade
     * já possui 3 de Veneno e recebe mais 2, este método é chamado para atualizar o total para 5.
     * </p>
     * * @param valor A quantidade de acúmulos a ser adicionada.
     */
    public void adicionarAcumulos(int valor) {
        this.acumulos += valor;
    }

    /**
     * Retorna uma representação em texto do efeito, ideal para ser exibida no console ou interface.
     * * @return Uma string formatada contendo o nome e a quantidade de acúmulos (ex: "Veneno (3)").
     */
    public String getString() {
        return this.nome + " (" + this.acumulos + ")";
    }

    /**
     * Método acionado pelo {@link Publisher} quando um evento é disparado no jogo.
     * <p>
     * Como é um método abstrato, cada classe filha (cada efeito específico) deve ditar 
     * como reage aos eventos. Um efeito de Veneno, por exemplo, pode reagir ao evento 
     * de "FIM_DE_TURNO" para causar dano ao {@code dono} e depois reduzir seus próprios acúmulos.
     * </p>
     * * @param evento O tipo de evento que acabou de acontecer no jogo (ex: início ou fim do turno).
     */
    @Override
    public abstract void serNotificado(TipoEvento evento);
}