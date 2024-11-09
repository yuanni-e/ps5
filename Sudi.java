import java.util.*;

public class Sudi {
    private final int unseenScore = -100;
    String start = "#";

    private Map<String, Double> currScores;
    private Map<String, Double> nextScores;
    private Set<String> currStates;
    private Set<String> nextStates;
    private Map<String, String> track;
    private List<Map<String, String>> trackList;
    private List<String> backtrace;

    private Scanner in = new Scanner(System.in);

    public Sudi(){
        currScores = new HashMap<>();
        currStates = new HashSet<>();
        track = new HashMap<>();
        trackList = new ArrayList<>();
        backtrace = new ArrayList<>();
    }

    public void POSViterbi(String line){
        String[] splitLine = line.split(" ");
//        String[] sentence = new String[splitLine.length + 1];
//        for (int i = 1; i < splitLine.length; i++){
//            sentence[i] = splitLine[i - 1];
//        }
//        sentence[0] = start;

        currStates.add(start);
        currScores.put(start, 0.0);

        for (String word : splitLine){
            for (int i = 0; i < trackList.size() - 1; i++){
                nextScores = new HashMap<>();
                nextStates = new HashSet<>();

                for (String currState : currStates){
                    for (String transition : BuildModel.getTransitions().get(currState).keySet()){
                        nextStates.add(transition);
                        double nextScore = 0;
                        nextScore += BuildModel.getTransitions().get(currState).get(transition);
                        nextScore += currScores.get(currState);
                        if (!BuildModel.getObservations().get(currState).containsKey(word)){
                            nextScore += unseenScore;
                        }
                        else {
                            nextScore += BuildModel.getObservations().get(currState).get(word);
                        }

                        if (!nextScores.containsKey(transition) || nextScore > nextScores.get(transition)){
                            nextScores.put(transition, nextScore);

                        }
                        currStates = nextStates;
                        currScores = nextScores;
                    }
                }
            }
        }



        String[] splitLine = line.split(" ");
        String[] sentence = new String[splitLine.length + 1];
        for (int i = 1; i < splitLine.length; i++){
            sentence[i] = splitLine[i - 1];
        }
        sentence[0] = start;

//        for (String s : sentence){
//            double score = 0;
//                for (String tag : BuildModel.getObservations().keySet()){
//                    if (!BuildModel.getObservations().get(tag).containsKey(s)){
//                        score += unseenScore;
//                    }
//                }
//
//
//
//        }



    }



}
