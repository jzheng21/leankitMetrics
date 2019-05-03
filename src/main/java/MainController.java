import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

public class MainController {

    private static final Gauge JOBS_IN_QUEUE = Gauge.build()
            .name("jobs_in_queue")
            .help("Current number of jobs in the queue")
            .register();

    private static final Counter myCounter = Counter.build()
            .name("my_counter_total")
            .help("An example counter.").register();



    public static void main(String[] args) throws Exception{

        if(args.length != 2){
            System.out.println("{url} {authorizationCode}");
            return;
        }

        String url = args[0];
        String authorizationCode = args[1];

        Board board = new Board(url, authorizationCode);
        DefaultExports.initialize();
        Metric metric = new Metric(board);


    }

    private static void initializerPrometheus() throws Exception{
        HTTPServer prometheusServer = new HTTPServer(7080);
    }

    public void enqueueJob(){
        JOBS_IN_QUEUE.inc();
    }

    public void runNextJob(){
        JOBS_IN_QUEUE.dec();
    }

}
