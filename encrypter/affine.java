import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class affine {

  public static void main(String[] args) throws IOException {
    // TODO Auto-generated method stub
    try {
      if (args[0].equals("decipher")) {

        File cipherTextFile = new File(args[1]);
        File outputFile = new File(args[2]);
        File dictionary = new File(args[3]);
        decipherDictionary(cipherTextFile, outputFile, dictionary);
      } else if (args[0].equals("encrypt")) {

        File plainTextFile = new File(args[1]);
        File outputFile = new File(args[2]);
        int a = Integer.parseInt(args[3]);
        int b = Integer.parseInt(args[4]);

        encryptFile(plainTextFile, outputFile, a, b);
      } else if (args[0].equals("decrypt")) {
        File plainTextFile = new File(args[1]);
        File outputFile = new File(args[2]);
        int a = Integer.parseInt(args[3]);
        int b = Integer.parseInt(args[4]);

        decryptFile(plainTextFile, outputFile, a, b);
      } else {
        System.out.println("Invalid Command. Valid commands are decipher, encrypt, or decrypt");
      }

    } catch (NoSuchFileException e) {
      System.out.println("File not found");

    }

  }

  public static void decipherDictionary(File ciphertext, File outputFile, File dictionaryFile)
      throws IOException {
    Scanner sc = new Scanner(dictionaryFile);
    List<String> words = new ArrayList<String>();
    while (sc.hasNextLine()) {
      words.add(sc.nextLine());

    }
    String curSolution = "";
    int curHighScore = 0;
    int curA = 1;
    int curB = 1;
    for (int a = 1; a < 128; a += 2) {
      for (int b = 1; b < 128; b++) {
        String decrypted = decryptFile(ciphertext, a, b);
        int wordCount = countWords(decrypted, words);
        if (wordCount > curHighScore) {
          curHighScore = wordCount;
          curSolution = decrypted;
          curA = a;
          curB = b;
        }
      }
    }
    FileWriter outputWriter = new FileWriter(outputFile);
    outputWriter.write(curA + " " + curB);
    outputWriter.write("\n");
    outputWriter.write("DECIPHERED MESSAGE:\n");
    outputWriter.write(curSolution);
    outputWriter.close();
    sc.close();
  }

  public static int countWords(String count, List<String> words) {
    String[] potentialWords = count.split("\\s+");
    int wordCount = 0;
    for (int i = 0; i < potentialWords.length; i++) {
      if (potentialWords[i].length() >= 3 && words.contains(potentialWords[i])) {
        wordCount++;

      }

    }
    return wordCount;
  }

  public static String decryptFile(File inputFile, int a, int b) throws IOException {

    if (a % 2 == 0) {
      System.out.printf("The key pair " + "({%d, %d}) is invalid, please select another key.", a,
          b);
      return null;
    }
    byte[] arrayOfBytes = Files.readAllBytes(inputFile.toPath());
    String output = "";
    for (int i = 0; i < arrayOfBytes.length; i++) {
      byte encryptedChar = decryptChar(arrayOfBytes[i], a, b);


      output += (char) encryptedChar;
    }
    return output;
  }

  public static void decryptFile(File inputFile, File outputFile, int a, int b) throws IOException {
    if (a % 2 == 0) {
      System.out.printf("The key pair " + "({%d, %d}) is invalid, please select another key.", a,
          b);
      return;
    }
    byte[] arrayOfBytes = Files.readAllBytes(inputFile.toPath());
    FileWriter outputWriter = new FileWriter(outputFile);
    for (int i = 0; i < arrayOfBytes.length; i++) {
      byte encryptedChar = decryptChar(arrayOfBytes[i], a, b);


      outputWriter.write(encryptedChar);
    }
    outputWriter.close();
  }


  public static byte decryptChar(byte encChar, int a, int b) {
    int charVal = encChar;
    int aInv = modInverse(a);
    int sub = charVal - b;
    if (sub < 0) {
      sub += 128;
    }
    int decryptedValue = aInv * (sub) % 128;

    byte decryptedChar = (byte) decryptedValue;


    return decryptedChar;

  }

  public static int modInverse(int A) {
    int M = 128;
    for (int X = 0; X < M; X++)
      if (((A % M) * (X % M)) % M == 1)
        return X;
    return 1;
  }

  public static void encryptFile(File inputFile, File outputFile, int a, int b) throws IOException {
    if (a % 2 == 0) {
      System.out.printf("The key pair " + "({%d, %d}) is invalid, please select another key.", a,
          b);
      return;
    }
    byte[] arrayOfBytes = Files.readAllBytes(inputFile.toPath());
    FileWriter outputWriter = new FileWriter(outputFile);
    for (int i = 0; i < arrayOfBytes.length; i++) {
      byte encryptedChar = encryptChar(arrayOfBytes[i], a, b);


      outputWriter.write(encryptedChar);
    }
    outputWriter.close();
  }

  public static byte encryptChar(byte encChar, int a, int b) {
    int byteVal = encChar;
    int encryptedInt = ((a * byteVal + b) % 128);
    byte encryptedChar = (byte) encryptedInt;

    return encryptedChar;

  }

}

