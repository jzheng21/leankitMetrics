import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Metric {

    private Board board;
    private HashMap<String, ArrayList<Card>> data;
    private HashMap<String, Integer> eachUserWorkload;
    private Date currentLeadTime;
    private Date startTime;
    private ArrayList<String> lanes;
    private HashMap<String, Gauge> gauges_cycleTimes = new HashMap<String, Gauge>();
    private HashMap<String, Gauge> gauges_eachUserWorkLoad = new HashMap<String, Gauge>();

    private static final Counter myCounter = Counter.build()
            .name("team_productivity")
            .help("It shows the change of total size of cards.").register();
    private HTTPServer server;

    public Metric(Board board) throws IOException{
        this.board = board;
        server = new HTTPServer(2020);
        data = board.getData();
        lanes = new ArrayList<String>();
        lanes.addAll(Arrays.asList("Planned", "ACTIVE_DEVELOPMENT", "CODE_REVIEW",
                "DEV_COMPLETE", "AVAILABLE_FOR_TESTING", "TESTING", "PASSED_QA", "ARTIFACT_CREATED"));
        update();

    }

    public void update() throws IOException {
        board.refresh();
        data = board.getData();
        getEachUserWorkload();
        setGauges_eachUserWorkLoad();
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
                            userWorkload.put(trimName(userName), card.getSize());
                        }
                        userWorkload.put(trimName(userName), userWorkload.get(trimName(userName)) + card.getSize());
                    }
                }
            }
        }

        eachUserWorkload = userWorkload;
    }

    private void refreshMetrics(){

    }

    private ArrayList<Card> getCardsFromLane(String lane) throws IOException{
        board.refresh();
        HashMap<String, ArrayList<Card>> data = board.getData();
        return data.get(lane);
    }

    private void getLeadTime() {
    }

    private void getCycleTimes(){
        Date newLeadTime = new Date();
        Date currentTime = new Date();
        setLanes(lanes, data);
        for(String lane : data.keySet()){

        }
    }

    private HashMap<String, Card> setLanes(ArrayList<String> lanes, HashMap<String, ArrayList<Card>> data){
        for(String lane : data.keySet()){
            if(!lanes.contains(lane)){
                data.remove(lane);
            }
        }

        throw new NotImplementedException();
    }

    public HashMap<String, Integer> getEachUserWorload() {
        return eachUserWorkload;
    }

    private void setGauges_eachUserWorkLoad(){
        for(String workerName : eachUserWorkload.keySet()){
            Gauge newgauge = Gauge.build().name(workerName).help("Worker's workload").register();
            newgauge.set(eachUserWorkload.get(trimName(workerName)));
            gauges_eachUserWorkLoad.put(workerName, newgauge);
        }
    }

    private String trimName(String name){
        char[] chAry = name.toCharArray();
        for(int i = 0; i < chAry.length; i++){
            String ch = Character.toString(chAry[i]);
            if(ch.equals(" ")){
                chAry[i] = '_';
            }
        }
        return new String(chAry);
    }
}
