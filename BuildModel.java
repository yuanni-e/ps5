import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BuildModel {

    private String start = "#";
    //string1 - current tag; string2 - next tag; double - frequency of transition from current to next
    private static Map<String, Map<String, Double>> transitions;
    //string1 - tag; string2 - word categorized as tag; double - frequency of tag appearance for each word
    private static Map<String, Map<String, Double>> observations;

//    private Map<String, Double> transitionTotals;
//    private Map<String, Double> observationTotals;

    public BuildModel(){
        transitions = new HashMap<String, Map<String, Double>>();
        observations = new HashMap<String, Map<String, Double>>();
    }

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
                for(int i=0; i<states.length-1; i++){
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

    public static Map<String, Map<String, Double>> getObservations() {
        return observations;
    }

    public static Map<String, Map<String, Double>> getTransitions() {
        return transitions;
    }

    public static void main(String[] args) throws IOException{
        BuildModel build = new BuildModel();
        build.count("texts/simple-train-sentences.txt", "texts/simple-train-tags.txt");
        //fuckass.normalize();
        System.out.println(build.transitions);
        //System.out.println(fuckass.transitionTotals);
        System.out.println(build.observations);
        //System.out.println(fuckass.observationTotals);
        //Scanner in = new Scanner(System.in);
        BufferedReader r = new BufferedReader(new FileReader("texts/simple-train-sentences.txt"));
        String line = r.readLine();

        while (line != null){
            Sudi test = new Sudi();
            test.POSViterbi(line, build.transitions, build.observations);
            line = r.readLine();
        }
    }
}
