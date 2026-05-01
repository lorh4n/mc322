/**
 * Representa um artefato permanente do herói.
 * Relíquias usam o mesmo canal Observer dos efeitos para reagir a eventos de batalha.
 */
public interface Reliquia extends Subscriber {

    String getNome();

    String getDescricao();

    /**
     * Prepara a relíquia para uma nova batalha e a registra no publicador atual.
     * @param batalha Batalha que publicará os eventos.
     */
    void prepararParaBatalha(Batalha batalha);
}
