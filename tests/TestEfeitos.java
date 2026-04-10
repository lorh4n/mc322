public class TestEfeitos {
    public static void run() {
        Assert.secao("Veneno - aplica dano no FIM_TURNO_JOGADOR");
        MockPublisher pub = new MockPublisher();
        Rato rato = new Rato(10, 4);
        Veneno v = new Veneno(rato, 3, pub);
        rato.aplicarEfeito(v, pub);
        Assert.assertTrue("Veneno inscrito no Publisher", pub.subscribers.contains(v));
        pub.notificar(TipoEvento.FIM_TURNO_JOGADOR);
        Assert.assertEquals("HP do Rato = 10 - 3 = 7", 7, rato.getVida());
        Assert.assertEquals("Acúmulos reduziram para 2", 2, v.getAcumulos());

        Assert.secao("Veneno - ignora outros eventos");
        int vidaAntes = rato.getVida();
        pub.notificar(TipoEvento.INICIO_TURNO_JOGADOR);
        pub.notificar(TipoEvento.INICIO_TURNO_INIMIGO);
        pub.notificar(TipoEvento.FIM_TURNO_INIMIGO);
        Assert.assertEquals("HP não muda com outros eventos", vidaAntes, rato.getVida());

        Assert.secao("Veneno - se remove ao expirar");
        pub.notificar(TipoEvento.FIM_TURNO_JOGADOR); // acumulos = 1
        Assert.assertTrue("Veneno ainda ativo", pub.subscribers.contains(v));
        pub.notificar(TipoEvento.FIM_TURNO_JOGADOR); // acumulos = 0 → remove
        Assert.assertFalse("Veneno removido do Publisher", pub.subscribers.contains(v));

        Assert.secao("Veneno - acúmulos somam se aplicado duas vezes");
        pub = new MockPublisher();
        rato = new Rato(20, 4);
        Veneno v1 = new Veneno(rato, 3, pub);
        rato.aplicarEfeito(v1, pub);
        Veneno v2 = new Veneno(rato, 2, pub);
        rato.aplicarEfeito(v2, pub); // deve somar, não criar novo
        Assert.assertEquals("Acúmulos somados = 5", 5, v1.getAcumulos());
        Assert.assertEquals("Publisher tem apenas 1 subscriber", 1, pub.subscribers.size());

        Assert.secao("Regeneracao - cura o herói no FIM_TURNO_JOGADOR");
        pub = new MockPublisher();
        Heroi hero = new Heroi("Herói", 20);
        hero.receberDano(10); // HP = 10
        Regeneracao r = new Regeneracao(hero, 3, pub);
        hero.aplicarEfeito(r, pub);
        pub.notificar(TipoEvento.FIM_TURNO_JOGADOR);
        Assert.assertEquals("HP = 10 + 3 = 13", 13, hero.getVida());
        Assert.assertEquals("Acúmulos = 2", 2, r.getAcumulos());

        Assert.secao("Regeneracao - não cura além do máximo");
        pub = new MockPublisher();
        hero = new Heroi("Herói", 20);
        hero.receberDano(2); // HP = 18
        r = new Regeneracao(hero, 10, pub);
        hero.aplicarEfeito(r, pub);
        pub.notificar(TipoEvento.FIM_TURNO_JOGADOR);
        Assert.assertEquals("HP capped em 20", 20, hero.getVida());

        Assert.secao("getString - formato correto");
        pub = new MockPublisher();
        rato = new Rato(10, 4);
        Veneno vStr = new Veneno(rato, 5, pub);
        Assert.assertEquals("Veneno.getString()", "Veneno (5)", vStr.getString());
    }
}
