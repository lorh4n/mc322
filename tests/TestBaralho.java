public class TestBaralho {
    public static void run() {
        MockPublisher pub = new MockPublisher();
        Heroi hero = new Heroi("Herói", 15);
        Rato rato = new Rato(10, 4);

        Assert.secao("Baralho - popularBaralho");
        Baralho b = new Baralho();
        b.popularBaralho(10);
        Assert.assertEquals("10 cartas na pilha de compra", 10, b.tamanhoCompra());
        Assert.assertEquals("mão começa vazia", 0, b.tamanhoMao());
        Assert.assertEquals("descarte começa vazio", 0, b.tamanhoDescarte());

        Assert.secao("Baralho - comprarCartas");
        b.comprarCartas(3);
        Assert.assertEquals("mão tem 3 cartas", 3, b.tamanhoMao());
        Assert.assertEquals("pilha de compra tem 7", 7, b.tamanhoCompra());

        Assert.secao("Baralho - descartarCarta");
        boolean ok = b.descartarCarta(0);
        Assert.assertTrue("descartarCarta retorna true", ok);
        Assert.assertEquals("mão tem 2", 2, b.tamanhoMao());
        Assert.assertEquals("descarte tem 1", 1, b.tamanhoDescarte());
        Assert.assertFalse("índice inválido retorna false", b.descartarCarta(99));

        Assert.secao("Baralho - usarCarta com energia suficiente");
        b = new Baralho();
        b.adicionarCarta(new CartaDano("Golpe", 1, "Causa 5 de dano", 5));
        b.comprarCartas(1);
        int custo = b.usarCarta(0, rato, hero, 3, pub);
        Assert.assertEquals("retorna custo da carta", 1, custo);
        Assert.assertEquals("mão vazia após usar", 0, b.tamanhoMao());
        Assert.assertEquals("carta no descarte", 1, b.tamanhoDescarte());

        Assert.secao("Baralho - usarCarta sem energia suficiente");
        b = new Baralho();
        b.adicionarCarta(new CartaDano("Caro", 3, "Causa 5 de dano", 5));
        b.comprarCartas(1);
        int ret = b.usarCarta(0, rato, hero, 1, pub);
        Assert.assertEquals("retorna -1 sem energia", -1, ret);
        Assert.assertEquals("carta volta à mão", 1, b.tamanhoMao());

        Assert.secao("Baralho - getCarta não remove da mão");
        b = new Baralho();
        b.adicionarCarta(new CartaDano("Golpe", 1, "desc", 3));
        b.comprarCartas(1);
        Carta c = b.getCarta(0);
        Assert.assertTrue("getCarta retorna carta válida", c != null);
        Assert.assertEquals("mão ainda tem 1 carta", 1, b.tamanhoMao());

        Assert.secao("Baralho - reciclagem automática do descarte");
        b = new Baralho();
        b.popularBaralho(3);
        b.comprarCartas(3);         // esvazia pilha de compra
        b.descartarCarta(0);        // descarte tem 1
        b.descartarCarta(0);        // descarte tem 2
        b.comprarCartas(1);         // dispara reciclagem
        Assert.assertEquals("descarte zerado após reciclagem", 0, b.tamanhoDescarte());
    }
}
