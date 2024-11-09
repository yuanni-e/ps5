import java.util.*;

public class Sudi {
    private final int unseenScore = -100;
    String start;

    //list of observations which contain:  list of words to observation #, map of pos to transition # and
    //next pos
//    List<Map<String, List<Map<String, Double>>>>  list; //observations
//    Map<String, Map<Double, String>> map; //transitions


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



}
