import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try{
            Lexer.getFile();
            Lexer.getResult();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
