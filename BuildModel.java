import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildModel {

    private String start = "#";
    //string1 - current tag; string2 - next tag; double - frequency of transition from current to next
    private static Map<String, Map<String, Double>> transitions;
    //string1 - tag; string2 - word categorized as tag; double - frequency of tag appearance for each word
    private static Map<String, Map<String, Double>> observations;

    public BuildModel(){
        transitions = new HashMap<String, Map<String, Double>>();
        observations = new HashMap<String, Map<String, Double>>();
    }

    /**
     * Perform training using passed-in files, produces transition and observation data to be used in Viterbi
     * @param sentenceFile a file of training sentences
     * @param tagFile a file of training tags that correspond to each word in sentenceFile
     */
    public void train(String sentenceFile, String tagFile) throws IOException {
        BufferedReader sentences = null;
        BufferedReader tags = null;
        Map<String, Double> transitionTotals = new HashMap<String, Double>();
        Map<String, Double> observationTotals = new HashMap<String, Double>();
        try {
            sentences = new BufferedReader(new FileReader(sentenceFile)); //read in file of training sentences
            tags = new BufferedReader(new FileReader(tagFile)); //read in file of training tags
            String tag = tags.readLine();
            String sentence = sentences.readLine();
            while (tag != null){ //while there are more lines to read
                //fill out transitions
                String[] states = tag.split(" "); //split tags in line into individual array entries

                sentence = sentence.toLowerCase(); //make line all lowercase
                String[] words = sentence.split(" "); //split words in line into individual array entries
                for(int i = 0; i < states.length; i++){
                    String currState;
                    if (i == 0){
                        currState = start; //set first state to start state
                    }
                    else {
                        currState = states[i - 1]; //after start, set curr state to tag
                    }

                    if (!transitions.containsKey(currState)){ //if there are no transitions for curr state
                        transitions.put(currState, new HashMap<>());
                    }
                    if (!transitions.get(currState).containsKey(states[i])){ //if there is no transition from curr state to next state
                        transitions.get(currState).put(states[i], 1.0); //put a score of 1.0 for the transition
                    }
                    else { //transition has been seen; increment score by 1
                        transitions.get(currState).put(states[i], transitions.get(currState).get(states[i]) + 1.0);
                    }

                    //fill out transitionTotals
                    if (!transitionTotals.containsKey(currState)) {
                        transitionTotals.put(currState, 0.0);
                    }
                    transitionTotals.put(currState, transitionTotals.get(currState)+1.0);


                    //fill out observations
                    if (!observations.containsKey(states[i])){ //if there are no observations for tag
                        observations.put(states[i], new HashMap<>());
                    } if (!observations.get(states[i]).containsKey(words[i])){ //if tag's observations do not contain word
                        observations.get(states[i]).put(words[i], 1.0); //put a score of 1.0 for word
                    } else { //word has been observed already; increment score by 1
                        observations.get(states[i]).put(words[i], observations.get(states[i]).get(words[i])+1.0);
                    }

                    //fill out observationTotals
                    if (!observationTotals.containsKey(states[i])) {
                        observationTotals.put(states[i], 0.0);
                    }
                    observationTotals.put(states[i], observationTotals.get(states[i])+1.0);

                }
                tag = tags.readLine(); //read next line of tags
                sentence = sentences.readLine(); //read next line of sentences

            }
            //normalize the counts in each map to frequencies
            //normalize transitions map, which is assumed to have counts of appearance of transitions
            //the double value in the map is updated
            for (String state : transitions.keySet()){
                double total = transitionTotals.get(state);
                for (String next : transitions.get(state).keySet()){
                    double tranFreq = transitions.get(state).get(next)/total;
                    double tranLog = Math.log(tranFreq);
                    transitions.get(state).put(next, tranLog);
                }
            }

            //normalize observations map, which is assumed to have counts of appearance of observations
            for( String state : observations.keySet()){
                double total = observationTotals.get(state);
                for(String word : observations.get(state).keySet()){
                    double obFreq = observations.get(state).get(word)/total;
                    double obLog = Math.log(obFreq);
                    observations.get(state).put(word, obLog);
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                sentences.close();
                tags.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    /**
     * Getter for observations
     * @return observations map
     */
    public Map<String, Map<String, Double>> getObservations() {
        return observations;
    }

    /**
     * Getter for transitions
     * @return transitions map
     */
    public Map<String, Map<String, Double>> getTransitions() {
        return transitions;
    }

}


