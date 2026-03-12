public class CartaEscudo {

   // Atributos
   private String nome;
   private int custo;
   private int protecao;


   // Metodos

   public CartaEscudo(String nome, int custo, int protecao) {
      this.nome = nome;
      this.custo = custo;
      this.protecao = protecao;
   }

   public void usar(Heroi alvo) {
      alvo.ganharEscudo(protecao);
   }

   public int getCusto() {
      return custo;
   }
}
