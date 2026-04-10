public class TestEntidade {
    public static void run() {
        Assert.secao("Entidade - receberDano sem escudo");
        Heroi h = new Heroi("Teste", 10);
        h.receberDano(4);
        Assert.assertEquals("HP reduz corretamente", 6, h.getVida());

        Assert.secao("Entidade - receberDano com escudo absorvendo tudo");
        h = new Heroi("Teste", 10);
        h.ganharEscudo(7);
        h.receberDano(4);
        Assert.assertEquals("HP intacto", 10, h.getVida());
        Assert.assertEquals("Escudo restante = 3", 3, h.getEscudo());

        Assert.secao("Entidade - receberDano com escudo absorvendo parcialmente");
        h = new Heroi("Teste", 10);
        h.ganharEscudo(2);
        h.receberDano(5);
        Assert.assertEquals("HP = 10 - (5-2) = 7", 7, h.getVida());
        Assert.assertEquals("Escudo zerado", 0, h.getEscudo());

        Assert.secao("Entidade - perderVida ignora escudo");
        h = new Heroi("Teste", 10);
        h.ganharEscudo(5);
        h.perderVida(3);
        Assert.assertEquals("HP reduz mesmo com escudo", 7, h.getVida());
        Assert.assertEquals("Escudo intacto", 5, h.getEscudo());

        Assert.secao("Entidade - curar não ultrapassa vida máxima");
        h = new Heroi("Teste", 10);
        h.receberDano(3);
        h.curar(99);
        Assert.assertEquals("HP capped no máximo", 10, h.getVida());

        Assert.secao("Entidade - estaVivo");
        h = new Heroi("Teste", 10);
        Assert.assertTrue("vivo com HP > 0", h.estaVivo());
        h.perderVida(10);
        Assert.assertFalse("morto com HP = 0", h.estaVivo());

        Assert.secao("Entidade - dano não leva HP abaixo de 0");
        h = new Heroi("Teste", 5);
        h.receberDano(100);
        Assert.assertEquals("HP mínimo é 0", 0, h.getVida());
    }
}
