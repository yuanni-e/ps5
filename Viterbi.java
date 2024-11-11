import java.util.*;

public class Viterbi {
    private final double unseenScore = -100.0; //set unseen score
    static String start = "#"; //start state that transitions to first word of the line

    private Map<String, Double> currScores; //current state maps to its score
    private Map<String, Double> nextScores; //next state maps to its score
    private Set<String> currStates; //keep track of current states
    private Set<String> nextStates; //keep track of next states (after transitions)
    private Map<String, String> track; //next state maps to prev state; prev state is value since there can be duplicate values in a map
    private List<Map<String, String>> trackList; //list of tracks
    private List<String> backtrace; //backtrace to find most likely tags

    public Viterbi(){
        currScores = new HashMap<>();
        currStates = new HashSet<>();
        track = new HashMap<>();
        trackList = new ArrayList<>();
        backtrace = new ArrayList<>();
    }

    /**
     * Performs Viterbi algorithm on a sentence
     * @param line to be read in
     * @param transitions a map from current tag to a map of next tags and scores
     * @param observations a map from current tag to a map of words corresponding to tag and scores
     * @return backtrace list
     */
    public List<String> POSViterbi(String line, Map<String, Map<String, Double>> transitions, Map<String, Map<String, Double>> observations){

        String[] splitLine = line.split(" "); //take in the line, split into array entries upon 'space'
        currStates.add(start); //add start state to currStates
        currScores.put(start, 0.0); //put a score of 0.0 for start in currScores

        for (int i = 0; i < splitLine.length; i++){ //for each word in line
            nextScores = new HashMap<>();
            nextStates = new HashSet<>();

            for (String currState : currStates){ //for each curr state in currStates set
                if (transitions.get(currState) != null){
                    //for each next state of current state
                    for (String nextState : transitions.get(currState).keySet()){
                        nextStates.add(nextState); //add next state to set
                        double nextScore = 0.0;

                        //add score corresponding to curr state
                        nextScore += currScores.get(currState);

                        //add the transition score between curr state and next state
                        nextScore += transitions.get(currState).get(nextState);

                        //if next state observations do not have an instance of word
                        if (!observations.get(nextState).containsKey(splitLine[i])){
                            nextScore += unseenScore; //add unseen score
                        }
                        else { //word has been observed in next state
                            //add score corresponding to observation of word
                            nextScore += observations.get(nextState).get(splitLine[i]);
                        }

                        //if there is no score already for next state or the score now is greater than the previous one for next state
                        if (!nextScores.containsKey(nextState) || nextScore > nextScores.get(nextState)){
                            nextScores.put(nextState, nextScore); //add next state and its score to nextScores map
                            track.put(nextState, currState); //put next state and curr state in track map
                        }
                    }
                }
            }
            currStates = nextStates; //use nextStates as currStates for next iteration
            currScores = nextScores; //use nextScores as currScores for next iteration
            trackList.add(track);
            track = new HashMap<>(); //reset track for new word
        }

        String state = null;
        //for each state in currScores (scores from the last word in line)
        for (String s : currScores.keySet()){
            //find state with the highest score
            if (state == null){
                state = s;
            }
            else if (currScores.get(s) > currScores.get(state)){
                state = s;
            }
        }
        backtrace.add(state); //add state with the highest score to backtrace
        for (int i = trackList.size() - 1; i >= 0; i--){
            //add the prev state of state
            backtrace.add(0, trackList.get(i).get(state));
            //set state to prev state
            state = trackList.get(i).get(state);
        }

        return backtrace;
    }
}
