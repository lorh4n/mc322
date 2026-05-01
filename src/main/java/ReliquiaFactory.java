import java.util.Random;

/**
 * Cria relíquias permanentes para recompensas e loja.
 */
public class ReliquiaFactory {

    private ReliquiaFactory() {}

    public static Reliquia criarAleatoria(Heroi heroi) {
        if (new Random().nextBoolean()) {
            return new ReliquiaEscudoInicial(heroi);
        }
        return new ReliquiaCuraPosBatalha(heroi);
    }
}
