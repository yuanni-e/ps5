import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Sudi {
    private Scanner in;
    private Map<String, Map<String, Double>> transitionMap;
    private Map<String, Map<String, Double>> observationMap;
    private Viterbi viterbi;

    public Sudi(String trainSentences, String trainTags){
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

    /**
     * Allows user to input a sentence to be tagged
     * @throws Exception
     */
    public void consoleTest() throws Exception {
        in = new Scanner(System.in);
        System.out.println("Enter a sentence to tag:");
        String input = in.nextLine(); //gets user's keyboard input
        while(!input.equals("no")) { //enter "No" or "NO" or "nO" if you want to tag it on its own
            System.out.println(input);
            viterbi = new Viterbi();
            List<String> tags = viterbi.POSViterbi(input, transitionMap, observationMap);

            String[] words = input.split(" ");
            String taggedSentence = "";
            for (int i = 0; i < words.length; i++) {
                taggedSentence += words[i] + "/" + tags.get(i + 1) + "  ";
            }
            System.out.println("Here is your tagged sentence: " + taggedSentence);
            System.out.println("Would you like to tag another sentence? (enter \"no\" to stop, enter anything else to continue tagging)");
            input = in.nextLine();
            if(!input.equals("no")) {
                System.out.println("Enter a sentence to tag:");
                input = in.nextLine();
            }
        }
        in.close();
    }

    public void fileTest(String sentenceFile, String tagFile) throws IOException{
        int wrong = 0;
        BufferedReader sentences = new BufferedReader(new FileReader(sentenceFile));
        BufferedReader tags = new BufferedReader(new FileReader(tagFile));
        String sentence = sentences.readLine();
        String tagline = tags.readLine();

        while (sentence != null){
            Viterbi test = new Viterbi();
            List<String> testTagLine = test.POSViterbi(sentence, transitionMap, observationMap);
            String[] testTags = tagline.split(" ");
            String[] words = sentence.split(" ");
            for (int i = 0; i < testTagLine.size()-1; i++){
                String tag = testTagLine.get(i+1);
                if(!tag.equals(testTags[i])){
                    wrong++;
                }
                System.out.print(words[i] + "/" + testTagLine.get(i + 1) + "  ");
            }
            System.out.println();
            sentence = sentences.readLine();
            tagline = tags.readLine();
        }
        System.out.println("Wrong tags: " + wrong);
    }

    public static void main(String[] args) throws Exception{
        Sudi simpleTester = new Sudi("texts/simple-train-sentences.txt", "texts/simple-train-tags.txt");
        simpleTester.fileTest("texts/simple-test-sentences.txt", "texts/simple-test-tags.txt");

        Sudi brownTester = new Sudi("texts/brown-train-sentences.txt", "texts/brown-train-tags.txt");
        brownTester.fileTest("texts/brown-test-sentences.txt", "texts/brown-test-tags.txt");
        try {
            brownTester.consoleTest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Sudi fileTester = new Sudi("texts/simple-train-sentences.txt", "texts/simple-train-tags.txt");
        fileTester.fileTest("texts/simple-test-sentences.txt", "texts/simple-test-tags.txt");

        brownTester.fileTest("texts/example-sentences.txt", "texts/example-tags.txt");

        Sudi boomTester = new Sudi("texts/boom-train-sentences.txt", "texts/boom-train-tags.txt");
        try {
            boomTester.consoleTest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
