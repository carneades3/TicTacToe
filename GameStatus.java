
public enum GameStatus {
   CONTINUE("indecisive"),
   FIRST_PLAYER_WIN("first player win"),
   SECOND_PLAYER_WIN("second player win"),
   DRAW("draw");
   
   private String name;
   
   private GameStatus(String name) {
      this.name = name;
   }
   
   public String toString() {
      return name;
   }
} 

