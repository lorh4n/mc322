import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class BatalhaTest {

    private Baralho baralhoSoDeDano(int qtd, int dano) {
        Baralho b = new Baralho();
        for (int i = 0; i < qtd; i++) {
            b.adicionarCarta(new CartaDano("Golpe", 1, "d", dano));
        }
        return b;
    }

    @Test
    public void batalhaPublisherInscreveEDesinscreve() {
        Heroi h = new Heroi("H", 10);
        List<Inimigo> inis = new ArrayList<>();
        inis.add(new Rato(5, 1));
        Batalha b = new Batalha(h, inis, new Baralho());
        Veneno v = new Veneno(h, 2, b);
        b.inscrever(v);
        assertTrue(b.getInimigos().contains(inis.get(0)));
        b.notificar(TipoEvento.FIM_TURNO_JOGADOR);
        // Veneno deve ter se auto-desinscrito após expirar (acumulos 2 -> 1, não expira ainda)
        b.notificar(TipoEvento.FIM_TURNO_JOGADOR);
        assertEquals(7, h.getVida()); // 2 + 1 dano
    }

    @Test
    public void resolverAutomaticoHeroiVenceRatoFraco() {
        Heroi h = new Heroi("Herói", 50);
        List<Inimigo> inis = new ArrayList<>();
        inis.add(new Rato(3, 1));
        Batalha b = new Batalha(h, inis, baralhoSoDeDano(10, 5));
        boolean venceu = b.resolverAutomatico(20);
        assertTrue(venceu);
        assertTrue(h.estaVivo());
        assertFalse(b.algumInimigoVivo());
    }

    @Test
    public void resolverAutomaticoHeroiPerdeContraRatoForte() {
        Heroi h = new Heroi("Herói", 3);
        List<Inimigo> inis = new ArrayList<>();
        inis.add(new Rato(100, 5));
        Batalha b = new Batalha(h, inis, baralhoSoDeDano(2, 1));
        boolean venceu = b.resolverAutomatico(20);
        assertFalse(venceu);
        assertFalse(h.estaVivo());
    }

    @Test
    public void estadoHeroiEBaralhoPersistemEntreBatalhas() {
        Heroi h = new Heroi("Herói", 40);
        Baralho baralho = baralhoSoDeDano(8, 10);

        List<Inimigo> inis1 = new ArrayList<>();
        inis1.add(new Rato(5, 2));
        new Batalha(h, inis1, baralho).resolverAutomatico(10);
        int vidaApos1 = h.getVida();
        assertTrue(vidaApos1 > 0);

        // Nova batalha com mesmo herói/baralho
        List<Inimigo> inis2 = new ArrayList<>();
        inis2.add(new Rato(5, 2));
        new Batalha(h, inis2, baralho).resolverAutomatico(10);
        assertTrue(h.getVida() > 0);
        assertEquals(0, h.getEscudo(), "Escudo é resetado entre batalhas");
        assertTrue(h.getEfeitos().isEmpty(), "Efeitos são limpos entre batalhas");
    }

    @Test
    public void recompensaAutomaticaConcedeOuroAposVitoria() {
        Heroi h = new Heroi("Herói", 50);
        List<Inimigo> inis = new ArrayList<>();
        inis.add(new Rato(3, 1));
        Batalha b = new Batalha(h, inis, baralhoSoDeDano(10, 5));
        assertTrue(b.resolverAutomatico(20));
        assertEquals(20, h.getOuro());
    }

    @Test
    public void reliquiaObservaInicioDaBatalha() {
        Heroi h = new Heroi("Herói", 50);
        h.adicionarReliquia(new ReliquiaEscudoInicial(h));
        List<Inimigo> inis = new ArrayList<>();
        inis.add(new Rato(3, 1));
        Batalha b = new Batalha(h, inis, baralhoSoDeDano(10, 5));
        assertTrue(b.resolverAutomatico(20));
        assertTrue(h.getEscudo() >= 4);
    }

    @Test
    public void algumInimigoVivoRetornaFalseQuandoTodosMortos() {
        Heroi h = new Heroi("H", 10);
        List<Inimigo> inis = new ArrayList<>();
        Rato r = new Rato(1, 1);
        inis.add(r);
        Batalha b = new Batalha(h, inis, new Baralho());
        assertTrue(b.algumInimigoVivo());
        r.receberDano(5);
        assertFalse(b.algumInimigoVivo());
    }
}
