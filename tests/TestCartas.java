public class TestCartas {
    public static void run() {
        MockPublisher pub = new MockPublisher();
        Heroi hero = new Heroi("Herói", 15);
        Rato rato = new Rato(10, 4);

        Assert.secao("CartaDano - causa dano no alvo");
        CartaDano cd = new CartaDano("Golpe", 1, "Causa 5 de dano", 5);
        cd.usar(hero, rato, pub);
        Assert.assertEquals("HP do inimigo = 10 - 5 = 5", 5, rato.getVida());
        Assert.assertEquals("HP do herói intacto", 15, hero.getVida());
        Assert.assertTrue("precisaAlvo() = true", cd.precisaAlvo());

        Assert.secao("CartaEscudo - aplica escudo no usuário");
        CartaEscudo ce = new CartaEscudo("Defesa", 1, "Ganha 5 de escudo", 5);
        ce.usar(hero, rato, pub);
        Assert.assertEquals("Herói tem 5 de escudo", 5, hero.getEscudo());
        Assert.assertEquals("HP do inimigo não muda", 5, rato.getVida());
        Assert.assertFalse("precisaAlvo() = false", ce.precisaAlvo());

        Assert.secao("CartaEfeito Veneno - aplica no alvo");
        pub = new MockPublisher();
        CartaEfeito cfv = new CartaEfeito("Frasco", 1, "Aplica 3 de Veneno", 3, "Veneno");
        cfv.usar(hero, rato, pub);
        Assert.assertEquals("Publisher tem 1 subscriber (Veneno)", 1, pub.subscribers.size());
        Assert.assertTrue("precisaAlvo() = true para Veneno", cfv.precisaAlvo());

        Assert.secao("CartaEfeito Regeneracao - aplica no usuário");
        pub = new MockPublisher();
        CartaEfeito cfr = new CartaEfeito("Regen", 1, "Ganha 2 de Regeneração", 2, "Regeneracao");
        cfr.usar(hero, rato, pub);
        Assert.assertEquals("Publisher tem 1 subscriber (Regeneracao)", 1, pub.subscribers.size());
        Assert.assertFalse("precisaAlvo() = false para Regeneracao", cfr.precisaAlvo());

        Assert.secao("CartaEfeito Veneno - dano ativado pelo Publisher");
        pub = new MockPublisher();
        rato = new Rato(10, 4);
        CartaEfeito venCard = new CartaEfeito("Nuvem", 1, "Aplica 2 de Veneno", 2, "Veneno");
        venCard.usar(hero, rato, pub);
        pub.notificar(TipoEvento.FIM_TURNO_JOGADOR);
        Assert.assertEquals("HP do Rato = 10 - 2 = 8 após FIM_TURNO", 8, rato.getVida());

        Assert.secao("CartaEscudo - alvo null não quebra");
        hero = new Heroi("Herói", 15);
        CartaEscudo ceNull = new CartaEscudo("Muralha", 1, "Ganha 4 de escudo", 4);
        ceNull.usar(hero, null, pub); // alvo null é válido para escudo
        Assert.assertEquals("Escudo aplicado com alvo null", 4, hero.getEscudo());
    }
}
