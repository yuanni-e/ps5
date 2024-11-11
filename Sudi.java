import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Sudi {
    private Scanner in;
    //training materials are default set to brown but you can change them if you want daniel
    private String trainSentences = "texts/brown-train-sentences.txt";
    private String trainTags = "texts/brown-train-tags.txt";
    private Map<String, Map<String, Double>> transitionMap;
    private Map<String, Map<String, Double>> observationMap;
    private Viterbi viterbi;

    public Sudi(){
        try{
            BuildModel viterbiModel = new BuildModel();
            viterbi = new Viterbi();
            viterbiModel.train(trainSentences, trainTags);
            transitionMap = viterbiModel.getTransitions();
            observationMap = viterbiModel.getObservations();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void consoleTest() throws Exception{
        in = new Scanner(System.in);
        System.out.println("Enter a sentence to tag: ");
        String input = in.nextLine(); //gets user's keyboard input
        System.out.println(input);
        List<String> tags = viterbi.POSViterbi(input, transitionMap, observationMap);
        System.out.println(tags);
        String[] words = input.split(" ");
        String taggedSentence = "";
        for(int i = 0; i < words.length; i++){
            taggedSentence += words[i] + "/" + tags.get(i+1) + "  ";
        }
        System.out.println("Here is your tagged sentence: " + taggedSentence);
        in.close();
    }

    public static void main(String[] args) throws Exception{
        Sudi tester = new Sudi();
        try {
            tester.consoleTest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
