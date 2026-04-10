import java.util.ArrayList;
import java.util.List;

public class TestObserver {
    // Subscriber simples que conta quantas vezes foi notificado
    static class ContadorSubscriber implements Subscriber {
        int contador = 0;
        TipoEvento ultimoEvento = null;

        @Override
        public void serNotificado(TipoEvento evento) {
            contador++;
            ultimoEvento = evento;
        }
    }

    public static void run() {
        Assert.secao("Observer - inscrever e notificar");
        MockPublisher pub = new MockPublisher();
        ContadorSubscriber s1 = new ContadorSubscriber();
        ContadorSubscriber s2 = new ContadorSubscriber();

        pub.inscrever(s1);
        pub.inscrever(s2);
        pub.notificar(TipoEvento.FIM_TURNO_JOGADOR);
        Assert.assertEquals("s1 notificado 1 vez", 1, s1.contador);
        Assert.assertEquals("s2 notificado 1 vez", 1, s2.contador);
        Assert.assertTrue("s1 recebeu FIM_TURNO_JOGADOR", s1.ultimoEvento == TipoEvento.FIM_TURNO_JOGADOR);

        Assert.secao("Observer - desinscrever para de receber notificações");
        pub.desinscrever(s1);
        pub.notificar(TipoEvento.FIM_TURNO_JOGADOR);
        Assert.assertEquals("s1 não foi notificado novamente", 1, s1.contador);
        Assert.assertEquals("s2 foi notificado novamente", 2, s2.contador);

        Assert.secao("Observer - inscrever o mesmo subscriber duas vezes");
        pub = new MockPublisher();
        ContadorSubscriber s3 = new ContadorSubscriber();
        pub.inscrever(s3);
        pub.inscrever(s3); // segunda vez deve ser ignorada
        pub.notificar(TipoEvento.FIM_TURNO_JOGADOR);
        Assert.assertEquals("notificado apenas 1 vez mesmo inscrito 2x", 1, s3.contador);

        Assert.secao("Observer - TipoEvento correto é passado");
        pub = new MockPublisher();
        ContadorSubscriber s4 = new ContadorSubscriber();
        pub.inscrever(s4);
        pub.notificar(TipoEvento.INICIO_TURNO_INIMIGO);
        Assert.assertTrue("evento correto recebido", s4.ultimoEvento == TipoEvento.INICIO_TURNO_INIMIGO);

        Assert.secao("Observer - desinscrever durante notificação não quebra");
        MockPublisher pub2 = new MockPublisher();
        // Subscriber que se desinscreve ao ser notificado
        Subscriber autoRemove = new Subscriber() {
            @Override
            public void serNotificado(TipoEvento evento) {
                pub2.desinscrever(this);
            }
        };
        pub2.inscrever(autoRemove);
        pub2.inscrever(s4);
        try {
            pub2.notificar(TipoEvento.FIM_TURNO_JOGADOR);
            Assert.assertTrue("notificar com auto-desinscrição não lança exceção", true);
        } catch (Exception e) {
            Assert.assertTrue("ERRO: exceção lançada - " + e.getMessage(), false);
        }
        Assert.assertFalse("autoRemove foi desinscrito", pub2.subscribers.contains(autoRemove));
    }
}
