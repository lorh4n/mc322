public class TestRunner {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║        SUITE DE TESTES MC322          ║");
        System.out.println("╚══════════════════════════════════════╝");

        TestEntidade.run();
        TestEfeitos.run();
        TestBaralho.run();
        TestCartas.run();
        TestObserver.run();

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║            RESULTADO FINAL            ║");
        System.out.println("╠══════════════════════════════════════╣");
        Assert.resumoFinal();
        System.out.println("╚══════════════════════════════════════╝");
    }
}
