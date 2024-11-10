import java.util.*;

public class Sudi {
    private final double unseenScore = -10.0;
    private double max = -1000000.0;
    String start = "#";

    private Map<String, Double> currScores;
    private Map<String, Double> nextScores;
    private Set<String> currStates;
    private Set<String> nextStates;
    private Map<String, String> track;
    private List<Map<String, String>> trackList;
    private List<String> backtrace;

    public Sudi(){
        currScores = new HashMap<>();
        currStates = new HashSet<>();
        track = new HashMap<>();
        trackList = new ArrayList<>();
        backtrace = new ArrayList<>();
    }

    public List<String> POSViterbi(String line, Map<String, Map<String, Double>> transitions, Map<String, Map<String, Double>> observations){

        String[] splitLine = line.split(" ");
        currStates.add(start);
        currScores.put(start, 0.0);

        if (splitLine.length == 1){
            nextScores = new HashMap<>();
            nextStates = new HashSet<>();

            for (String nextState : transitions.get("#").keySet()){
                nextStates.add(nextState); //add next state
                double nextScore = 0.0;

                //System.out.println("nextState: " + nextState);
                //add score corresponding to curr state
                nextScore += currScores.get("#");

                //add transition score
                nextScore += transitions.get("#").get(nextState);
                //System.out.print(" + " + transitions.get(currState).get(nextState) + "(tran)");
                //if curr state observations don't have an instance of the word
                if (!observations.get(nextState).containsKey(splitLine[0])){
                    nextScore += unseenScore; //add unseen score
                    //System.out.print(" + " + unseenScore + "(obs)");
                }
                else {
                    //add score corresponding to word
                    nextScore += observations.get(nextState).get(splitLine[0]);
                }
                //System.out.println(nextState + ": " + nextScore);

                //if there is no score for next state or the score now is greater than the previous one for next state
                //add next state and score
                nextScores.put(nextState, nextScore);
                //keep track of next state and state it came from
                track.put(nextState, "#");
                //System.out.println(track);
            }
            currStates = nextStates; //next states are used for new current
            currScores = nextScores; //next scores are used for new current
            trackList.add(track); //add observation per word in line
            String state = "";
            //for each unique next state for the last word
            for (String s : trackList.get(0).keySet()){
                if (currScores.get(s) > max){
                    max = currScores.get(s);
                    state = s; //state with the highest score
                }
            }
            backtrace.add(state); //add state with highest score
            backtrace.add(0, trackList.get(0).get(state));
//            for (int i = trackList.size() - 1; i >= 0; i--){
//                //add the current state that corresponds to next state
//                backtrace.add(0, trackList.get(i).get(state));
//                //set state to current state
//                state = trackList.get(i).get(state);
//            }
            System.out.println("backtrace: " + backtrace);
            return backtrace;
        }


        else {
//            System.out.println("splitLine length: " + splitLine.length);
//            System.out.println("splitLine: " + Arrays.toString(splitLine));
            for (int i = 0; i < splitLine.length - 1; i++){ //for each word
                //System.out.println("splitLineIndex: " + splitLine[i]);
                nextScores = new HashMap<>();
                nextStates = new HashSet<>();

                for (String currState : currStates){ //for each current state
                    //for each next state (current state to each next state: transition)
//                    System.out.println(splitLine[i]);
//                    System.out.println("currState: " + currState);
                    if (transitions.get(currState) != null){
                        for (String nextState : transitions.get(currState).keySet()){
                            nextStates.add(nextState); //add next state
                            double nextScore = 0.0;

                            //System.out.println("nextState: " + nextState);
                            //add score corresponding to curr state
                            nextScore += currScores.get(currState);
                            //System.out.print(currScores.get(currState) + "(curr)");

                            //add transition score
                            nextScore += transitions.get(currState).get(nextState);
                            //System.out.print(" + " + transitions.get(currState).get(nextState) + "(tran)");
                            //if curr state observations don't have an instance of the word
                            //if (observations.get(currState) != null){
                            if (!observations.get(nextState).containsKey(splitLine[i])){
                                nextScore += unseenScore; //add unseen score
                                //System.out.print(" + " + unseenScore + "(obs)");
                            }
                            else {
                                //add score corresponding to word
                                nextScore += observations.get(nextState).get(splitLine[i]);
                                //System.out.print(" + " + observations.get(nextState).get(splitLine[i]) + "(obs)");
                            }
                            //}
                            //System.out.println(nextState + ": " + nextScore);

                            //if there is no score for next state or the score now is greater than the previous one for next state
                            if (!nextScores.containsKey(nextState) || nextScore > nextScores.get(nextState)){
                                //add next state and score
                                nextScores.put(nextState, nextScore);
                                //keep track of next state and state it came from
                                track.put(nextState, currState);
//                                System.out.println(currState);
//                                System.out.println(track);
                            }
                        }
                        //System.out.println("done");
                    }
                }
                //System.out.println("done2");
                currStates = nextStates; //next states are used for new current
                currScores = nextScores; //next scores are used for new current
                //System.out.println("currScores: " + currScores);
                trackList.add(track); //add observation per word in line
                track = new HashMap<>();
            }
            //System.out.println("trackList: " + trackList);

            String state = "";
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
                backtrace.add(0, trackList.get(i).get(state));
                //set state to current state
                state = trackList.get(i).get(state);
            }
            System.out.println("backtrace: " + backtrace);
        }
        return backtrace;

    }

    public static void main(String[] args) {
        //Sudi test = new Sudi();
    }



}
