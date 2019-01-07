import java.io.IOException;

public class MainController {

    public static void main(String[] args) throws IOException{

        if(args.length != 2){
            System.out.println("{url} {authorizationCode}");
            return;
        }

        String url = args[0];
        String authorizationCode = args[1];

        Board board = new Board(url, authorizationCode);
        Metric metric = new Metric(board);
    }

}
