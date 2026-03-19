public abstract class Carta {
   // Atributos
   private String nome;
   private String descricao;
   private int custo;
   

   // Metodos

   public Carta(String nome, int custo, String descrição) {
      this.nome = nome;
      this.custo = custo;
      this.descricao = descrição;
   }

   public abstract void usar(Entidade usuario, Entidade alvo);


   public int getCusto() {
      return custo;
   }

   public String getNome() {
      return this.nome;
   }

   public String getDescricao() {
      return this.descricao;
   }
}
