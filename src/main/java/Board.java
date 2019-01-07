import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    private String url, authorizationCode;
    private HashMap<String, ArrayList<Card>> data = new HashMap<String, ArrayList<Card>>();
    private JSONObject jsonObject;
    private Api api;
    private HashMap<Long, String> activeUsers = new HashMap<Long, String>();

    public Board(String url, String authorizationCode) throws IOException {
        this.url = url;
        this.authorizationCode = authorizationCode;
        refresh();
    }

    private void populateBoard(){
        JSONArray ary = (JSONArray) jsonObject.get("ReplyData");
        JSONObject replyData = (JSONObject) ary.get(0);
        JSONArray lanes = (JSONArray) replyData.get("Lanes");
        for(int i = 0; i < lanes.length(); i++){
            JSONObject lane = lanes.getJSONObject(i);
            fillBoard(lane);
        }
    }

    private void fillBoard(JSONObject lane){
        String laneTitle = lane.getString("Title");
        JSONArray cards = lane.getJSONArray("Cards");

        String title, description, externalcardid, prioŕitytext,lastactivity,
                startdate,duedate,lastcomment,typename, prioritytext;
        ArrayList<String> assignedUserNames = new ArrayList<String>();
        int size;

        ArrayList<Card> cardArrayList = new ArrayList<Card>();
        for(int i = 0; i < cards.length(); i++){
            JSONObject obj = cards.getJSONObject(i);

            title			= (String)obj.get("Title");
            description		= (String) obj.get("Description");
            externalcardid	= (String) obj.get("ExternalCardID");
            size			= (Integer)   obj.get("Size");
            prioritytext	= (String) obj.get("PriorityText");
            lastactivity	= (String) obj.get("LastActivity");
            startdate		= (String) obj.get("StartDate");
            duedate			= (String) obj.get("DueDate");
            lastcomment		= (String) obj.get("LastComment");
            typename 		= (String) obj.get("TypeName");
            assignedUserNames = getAssignedUserNames(obj.getJSONArray("AssignedUsers"));
            prioritytext 	= (String) obj.get("PriorityText");
            Card card = new Card(laneTitle, title, description,
                    assignedUserNames, lastactivity, startdate, duedate,
                    lastcomment, typename, prioritytext, size);

            cardArrayList.add(card);
        }

        data.put(laneTitle, cardArrayList);
    }

    private ArrayList<String> getAssignedUserNames(JSONArray assignedUsers){
        ArrayList<String> userNames = new ArrayList<String>();
        for(int i = 0; i < assignedUsers.length(); i++){
            long userId = assignedUsers.getJSONObject(i).getLong("Id");
            String fullName = assignedUsers.getJSONObject(i).getString("FullName");
            if(!activeUsers.containsKey(userId)){
                activeUsers.put(userId, fullName);
            }
            userNames.add(fullName);
        }
        return userNames;
    }

    public void refresh() throws IOException{
        data.clear();
        activeUsers.clear();
        api = new Api(url, authorizationCode);
        this.jsonObject = api.postRequest();
        populateBoard();
    }

    public HashMap<String, ArrayList<Card>> getData() {
        return data;
    }

    public HashMap<Long, String> getActiveUsers() {
        return activeUsers;
    }
}
