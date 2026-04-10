public class Assert {
    private static int passed = 0;
    private static int failed = 0;

    public static void assertEquals(String nome, int esperado, int obtido) {
        if (esperado == obtido) {
            System.out.println("  [PASS] " + nome);
            passed++;
        } else {
            System.out.println("  [FAIL] " + nome + " — esperado: " + esperado + ", obtido: " + obtido);
            failed++;
        }
    }

    public static void assertEquals(String nome, String esperado, String obtido) {
        if (esperado.equals(obtido)) {
            System.out.println("  [PASS] " + nome);
            passed++;
        } else {
            System.out.println("  [FAIL] " + nome + " — esperado: \"" + esperado + "\", obtido: \"" + obtido + "\"");
            failed++;
        }
    }

    public static void assertTrue(String nome, boolean condicao) {
        if (condicao) {
            System.out.println("  [PASS] " + nome);
            passed++;
        } else {
            System.out.println("  [FAIL] " + nome);
            failed++;
        }
    }

    public static void assertFalse(String nome, boolean condicao) {
        assertTrue(nome, !condicao);
    }

    public static void secao(String nome) {
        System.out.println("\n--- " + nome + " ---");
    }

    public static void resumoFinal() {
        int total = passed + failed;
        System.out.printf("║ %-36s ║%n", "Total: " + total + " testes");
        System.out.printf("║ %-36s ║%n", "Passaram: " + passed);
        System.out.printf("║ %-36s ║%n", "Falharam: " + failed);
    }
}
