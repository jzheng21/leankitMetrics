import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Metric {

    private Board board;
    private HashMap<String, Integer> eachUserWorload;

    public Metric(Board board) throws IOException{
        this.board = board;
        refresh();
    }

    public void refresh() throws IOException {
        board.refresh();
        getEachUserWorkload();
    }

    private void getEachUserWorkload(){
        HashMap<String, ArrayList<Card>> data = board.getData();
        HashMap<Long, String> activeUsers = board.getActiveUsers();
        HashMap<String, Integer> userWorkload = new HashMap<String, Integer>();

        for(Object value : data.values()){
            ArrayList<Card> cards = (ArrayList<Card>) value;
            for(Card card : cards){
                if(!card.getAssignedUser().isEmpty()){
                    for(String userName : card.getAssignedUser()){
                        if(!userWorkload.containsKey(userName)){
                            userWorkload.put(userName, card.getSize());
                        }
                        userWorkload.put(userName, userWorkload.get(userName) + card.getSize());
                    }
                }
            }
        }

        eachUserWorload = userWorkload;
    }

    public HashMap<String, Integer> getEachUserWorload() {
        return eachUserWorload;
    }
}
