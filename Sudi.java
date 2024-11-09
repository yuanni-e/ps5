import java.util.*;

public class Sudi {
    private final int unseenScore = -100;
    String start;

    //list of observations which contain:  list of words to observation #, map of pos to transition # and
    //next pos
//    List<Map<String, List<Map<String, Double>>>>  list; //observations
//    Map<String, Map<Double, String>> map; //transitions

    private Map<String, Double> currScore;
    private Map<String, Double> nextScore;
    private Set<String> currState;
    private Set<String> nextState;
    private Map<String, String> track;
    private List<Map<String, String>> trackList;
    private List<String> backtrace;

    private Scanner in = new Scanner(System.in);

    public Sudi(){
//        list = new ArrayList<>();
//        map = new HashMap<>();
    }

    public void POSViterbi(String line){
        String[] splitLine = line.split(" ");
        String[] sentence = new String[splitLine.length + 1];
        for (int i = 1; i < splitLine.length; i++){
            sentence[i] = splitLine[i - 1];
        }
        sentence[0] = start;

        for (String s : sentence){
            double score = 0;
                for (String tag : BuildModel.getObservations().keySet()){
                    if (!.containsKey(tag)){
                        score += unseenScore;
                    }
                }



        }



    }

    //split strings into lists
    //structures:
    //currScore -> map string to double
    //nextScore -> map string to double
    //currState -> set
    //nextState -> set
    //Map track
    //List of maps (list of track)
    //ArrayList backtrace

}
