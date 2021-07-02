package com.example.diseaseprediction;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.metadata.MetadataExtractor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.example.diseaseprediction.tokenize.RDRsegmenter;
import com.example.diseaseprediction.tokenize.Vocabulary;

public class TextClassificationClient {
  private static final String TAG = "Interpreter";

  private static final int SENTENCE_LEN = 100; // The maximum length of an input sentence.
  // Simple delimiter to split words.
  private static final String SIMPLE_SPACE_OR_PUNCTUATION = " |\\,|\\.|\\!|\\?|\n";
  private static final String MODEL_PATH = "model-088-metadata.tflite";
  private static final String VOCAL_PATH = "model-088_vocab.txt";
  private static final String LABEL_PATH = "labels.txt";
  /*
   * Reserved values in ImdbDataSet dic:
   * dic["<PAD>"] = 0      used for padding
   * dic["<START>"] = 1    mark for the start of a sentence
   * dic["<UNKNOWN>"] = 2  mark for unknown words (OOV)
   */
  private static final String START = "<START>";
  private static final String PAD = "0";
  private static final String UNKNOWN = "<OOV>";

  /** Number of results to show in the UI. */
  private static final int MAX_RESULTS = 6;

  private final Context context;
  private final Map<String, Integer> dic = new HashMap<>();
  private final List<String> labels = new ArrayList<>();
  private Interpreter tflite;
  private AssetManager am ;
  private InputStream is ;
  private InputStream fis;
  Vocabulary vocabulary ;
  RDRsegmenter rdRsegmenter ;

  public TextClassificationClient(Context context) {
    this.context = context;
    try {
      am = context.getAssets();
      is = am.open("Model.RDR");
      fis = am.open("VnVocab.txt");
      vocabulary = new Vocabulary();
      vocabulary.test(fis);
      rdRsegmenter = new RDRsegmenter(is,fis);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /** Load the TF Lite model and dictionary so that the client can start classifying text. */
  public void load() {
    loadModel();
  }

  /** Load TF Lite model. */
  private synchronized void loadModel() {
    try {
      // Load the TF Lite model
      ByteBuffer buffer = loadModelFile(this.context.getAssets(), MODEL_PATH);
      tflite = new Interpreter(buffer);
      Log.v(TAG, "TFLite model loaded.");

      // Use metadata extractor to extract the dictionary and label files.
      MetadataExtractor metadataExtractor = new MetadataExtractor(buffer);

      // Extract and load the dictionary file.
      InputStream dictionaryFile = metadataExtractor.getAssociatedFile("model-088-vocab.txt");
      System.out.println("check dic :"+ dictionaryFile);
      loadDictionaryFile(dictionaryFile);
      Log.v(TAG, "Dictionary loaded.");

      // Extract and load the label file.
      InputStream labelFile = metadataExtractor.getAssociatedFile("labels.txt");
      loadLabelFile(labelFile);
      Log.v(TAG, "Labels loaded.");

    } catch (IOException ex) {
      Log.e(TAG, "Error loading TF Lite model.\n", ex);
    }
  }

  /** Free up resources as the client is no longer needed. */
  public synchronized void unload() {
    tflite.close();
    dic.clear();
    labels.clear();
  }

  /** Classify an input string and returns the classification results. */
  public synchronized List<Result> classify(String text) throws IOException {
    // Pre-prosessing.
    int[][] input = tokenizeInputText(text);

    // Run inference.
    Log.v(TAG, "Classifying text with TF Lite...");
    float[][] output = new float[1][labels.size()];
    tflite.run(input, output);


    // Find the best classifications.
    PriorityQueue<Result> pq =
        new PriorityQueue<>(
            MAX_RESULTS, (lhs, rhs) -> Float.compare(rhs.getConfidence(), lhs.getConfidence()));
    for (int i = 0; i < labels.size(); i++) {
      pq.add(new Result("" + i, labels.get(i), output[0][i]));
    }
    final ArrayList<Result> results = new ArrayList<>();
    while (!pq.isEmpty()) {
      results.add(pq.poll());
    }

    Collections.sort(results);
    // Return the probability of each class.
    return results;
  }

  /** Load TF Lite model from assets. */
  private static MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath)
      throws IOException {
    try (AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
         FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor())) {
      FileChannel fileChannel = inputStream.getChannel();
      long startOffset = fileDescriptor.getStartOffset();
      long declaredLength = fileDescriptor.getDeclaredLength();
      return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
  }

  /** Load labels from model file. */
  private void loadLabelFile(InputStream ins) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
    // Each line in the label file is a label.
    while (reader.ready()) {
      labels.add(reader.readLine());
    }
  }

  /** Load dictionary from model file. */
  private void loadDictionaryFile(InputStream ins) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
    // Each line in the dictionary has two columns.
    // First column is a word, and the second is the index of this word.
    while (reader.ready()) {
      List<String> line = Arrays.asList(reader.readLine().split(" "));
      if (line.size() < 2) {
        continue;
      }
      dic.put(line.get(0), Integer.parseInt(line.get(1)));
    }
  }

  /** Pre-prosessing: tokenize and map the input words into a float array. */
  int[][] tokenizeInputText(String text) throws IOException {

    text = rdRsegmenter.segmentTokenizedString(text);

    int[] tmp = new int[SENTENCE_LEN];
    List<String> array = Arrays.asList(text.split(SIMPLE_SPACE_OR_PUNCTUATION));
    System.out.println("check ");
    System.out.println(array);
    System.out.println(labels);
    int index = 0;
    // Prepend <START> if it is in vocabulary file.
    if (dic.containsKey(START)) {
      tmp[index++] = dic.get(START);
    }

    for (String word : array) {
      if (index >= SENTENCE_LEN) {
        break;
      }
      tmp[index++] = dic.containsKey(word) ? dic.get(word) : (int) dic.get(UNKNOWN);
    }
    // Padding and wrapping.
    Arrays.fill(tmp, index, SENTENCE_LEN - 1, Integer.parseInt(PAD));
    int[][] ans = {tmp};
    return ans;
  }
  public List<String> tokenize(String text) throws IOException {
    List<String> token = new ArrayList<>();
    text = rdRsegmenter.segmentTokenizedString(text);
    token = Arrays.asList(text.split(" "));
    return token;
  }

  Map<String, Integer> getDic() {
    return this.dic;
  }

  Interpreter getTflite() {
    return this.tflite;
  }

  List<String> getLabels() {
    return this.labels;
  }
}
