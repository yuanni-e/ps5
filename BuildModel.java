import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BuildModel {

    private String start = "#";
    //string1 - current tag; string2 - next tag; double - frequency of transition from current to next
    private static Map<String, Map<String, Double>> transitions;
    //string1 - tag; string2 - word categorized as tag; double - frequency of tag appearance for each word
    private static Map<String, Map<String, Double>> observations;

    // DELETE RANDOM COMMENTED OUT CODE (DEAD CODE)
//    private Map<String, Double> transitionTotals;
//    private Map<String, Double> observationTotals;

    public BuildModel(){
        transitions = new HashMap<String, Map<String, Double>>();
        observations = new HashMap<String, Map<String, Double>>();
    }

    // TODO: add more comments in this method
    // TODO: also add JavaDocs (those nice looking comments above methods)
    public void count(String sentenceFile, String tagFile) throws IOException {
        BufferedReader sentences = null;
        BufferedReader tags = null;
        Map<String, Double> transitionTotals = new HashMap<String, Double>();
        Map<String, Double> observationTotals = new HashMap<String, Double>();
        try {
            sentences = new BufferedReader(new FileReader(sentenceFile));
            tags = new BufferedReader(new FileReader(tagFile));
            String tag = tags.readLine();
            String sentence = sentences.readLine();
            while(tag!=null){
                //fill out transitions
                String[] states = tag.split(" ");
                sentence = sentence.toLowerCase();
                String[] words = sentence.split(" ");
                for(int i=0; i<states.length; i++){
                    String currState;
                    if(i==0){
                        currState = start;
                    }else{
                        currState = states[i-1];
                    }
                    if(!transitions.containsKey(currState)){
                        transitions.put(currState, new HashMap<>());
                    }
                    if(!transitions.get(currState).containsKey(states[i])){
                        transitions.get(currState).put(states[i], 1.0);
                    }else{
                        transitions.get(currState).put(states[i], transitions.get(currState).get(states[i])+1.0);
                    }

                    //fill out transitionTotals
                    if (!transitionTotals.containsKey(currState)) {
                        transitionTotals.put(currState, 0.0);
                    }
                    transitionTotals.put(currState, transitionTotals.get(currState)+1.0);


                    //fill out observations
                    if(!observations.containsKey(states[i])){
                        observations.put(states[i], new HashMap<>());
                    }if(!observations.get(states[i]).containsKey(words[i])){
                        observations.get(states[i]).put(words[i], 1.0);
                    }else{
                        observations.get(states[i]).put(words[i], observations.get(states[i]).get(words[i])+1.0);
                    }

                    //fill out observationTotals
                    if (!observationTotals.containsKey(states[i])) {
                        observationTotals.put(states[i], 0.0);
                    }
                    observationTotals.put(states[i], observationTotals.get(states[i])+1.0);

                }
                tag = tags.readLine();
                sentence = sentences.readLine();

            }
            //normalize the counts in each map to frequencies
            //normalize transitions map, which is assumed to have counts of appearance of transitions
            //the double value in the map is updated
            for(String state : transitions.keySet()){
                double total = transitionTotals.get(state);
                for(String next : transitions.get(state).keySet()){
                    double tranFreq = transitions.get(state).get(next)/total;
                    double tranLog = Math.log(tranFreq);
                    transitions.get(state).put(next, tranLog);
                }
            }

            //normalize observations map, which is assumed to have counts of appearance of observations
            for(String state : observations.keySet()){
                double total = observationTotals.get(state);
                for(String word : observations.get(state).keySet()){
                    double obFreq = observations.get(state).get(word)/total;
                    double obLog = Math.log(obFreq);
                    observations.get(state).put(word, obLog);
                }
            }
            System.out.println(transitions);
            System.out.println(observations);

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

//    public void normalize(){
//        //normalize transitions map, which is assumed to have counts of appearance of transitions
//        //the double value in the map is updated
//        for(String state : transitions.keySet()){
//            double total = transitionTotals.get(state);
//            for(String next : transitions.get(state).keySet()){
//                double tranFreq = transitions.get(state).get(next)/total;
//                double tranLog = Math.log(tranFreq);
//                transitions.get(state).put(next, tranLog);
//            }
//        }
//
//        //normalize observations map, which is assumed to have counts of appearance of observations
//        for(String state : observations.keySet()){
//            double total = observationTotals.get(state);
//            for(String word : observations.get(state).keySet()){
//                double obFreq = observations.get(state).get(word)/total;
//                double obLog = Math.log(obFreq);
//                observations.get(state).put(word, obLog);
//            }
//        }
//    }

    // TODO: you never use this
    public static Map<String, Map<String, Double>> getObservations() {
        return observations;
    }

    public static Map<String, Map<String, Double>> getTransitions() {
        return transitions;
    }

    public static void main(String[] args) throws IOException{
        BuildModel build = new BuildModel();
        build.count("texts/brown-train-sentences.txt", "texts/brown-train-tags.txt");
        int wrong = 0;
        BufferedReader r = new BufferedReader(new FileReader("texts/brown-test-sentences.txt"));
        BufferedReader t = new BufferedReader(new FileReader("texts/brown-test-tags.txt"));
        String line = r.readLine();
        String tagline = t.readLine();

        //TODO: get rid of random print statements
        //System.out.println(tagline);

        while (line != null){
            Sudi test = new Sudi();
            List<String> tags = test.POSViterbi(line, build.transitions, build.observations);
            String[] testTags = tagline.split(" ");
            for (int i = 0; i < tags.size()-1; i++){
                String tag = tags.get(i+1);
                if(!tag.equals(testTags[i])){
                    wrong++;
                    System.out.println("incorrect tag: " + tag + " should be " + testTags[i]);
                }
            }
            //System.out.println(count++);
            line = r.readLine();
            tagline = t.readLine();
        }
        System.out.println("wrong tags: " + wrong);
    }

}

