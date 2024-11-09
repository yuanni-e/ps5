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

        currStates.add(start);
        currScores.put(start, 0.0);

        for (int i = 0; i < splitLine.length - 1; i++){ //for each word
            nextScores = new HashMap<>();
            nextStates = new HashSet<>();

            for (String currState : currStates){ //for each current state
                //for each next state (current state to each next state: transition)
                for (String nextState : BuildModel.getTransitions().get(currState).keySet()){
                    nextStates.add(nextState); //add next state
                    double nextScore = 0;
                    //add transition score
                    nextScore += BuildModel.getTransitions().get(currState).get(nextState);
                    //add score corresponding to curr state
                    nextScore += currScores.get(currState);

                    //if curr state observations don't have an instance of the word
                    if (!BuildModel.getObservations().get(currState).containsKey(splitLine[i])){
                        nextScore += unseenScore; //add unseen score
                    }
                    else {
                        //add score corresponding to word
                        nextScore += BuildModel.getObservations().get(currState).get(splitLine[i]);
                    }

                    //if there is no score for next state or the score now is greater than the previous one for next state
                    if (!nextScores.containsKey(nextState) || nextScore > nextScores.get(nextState)){
                        //add next state and score
                        nextScores.put(nextState, nextScore);
                        //keep track of next state and state it came from
                        track.put(nextState, currState);
                    }
                    currStates = nextStates; //next states are used for new current
                    currScores = nextScores; //next scores are used for new current
                }
            }
            trackList.add(track); //add observation per word in line
        }

        String state = "";
        double max = -1;
        //for each unique next state for the last word
        for (String s : trackList.get(trackList.size() - 1).keySet()){
            if (currScores.get(s) > max){
                max = currScores.get(s);
                state = s; //state with the highest score
            }
        }
        backtrace.add(state); //add state with highest score
        for (int i = trackList.size() - 1; i >= 0; i--){
            //add the current state that corresponds to next state
            backtrace.add(trackList.get(i).get(state));
            //set state to current state
            state = trackList.get(i).get(state);
        }
    }



}
