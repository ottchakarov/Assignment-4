import java.io.*;

public class PasswordChecker {
    static final int M_CHAINING = 1000;
    static final int M_LINEAR = 20000;

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: java PasswordChecker <dictionaryFile> <password>");
            return;
        }

        String dictionaryFile = args[0];
        String password = args[1];


        SeparateChainingHashTable chainingOld = new SeparateChainingHashTable(M_CHAINING, true);
        SeparateChainingHashTable chainingCurrent = new SeparateChainingHashTable(M_CHAINING, false);
        LinearProbingHashTable linearOld = new LinearProbingHashTable(M_LINEAR, true);
        LinearProbingHashTable linearCurrent = new LinearProbingHashTable(M_LINEAR, false);


        BufferedReader br = new BufferedReader(new FileReader(dictionaryFile));
        String word;
        int lineNumber = 1;
        while ((word = br.readLine()) != null) {
            chainingOld.put(word, lineNumber);
            chainingCurrent.put(word, lineNumber);
            linearOld.put(word, lineNumber);
            linearCurrent.put(word, lineNumber);
            lineNumber++;
        }
        br.close();

 
        boolean isStrong = isStrongPassword(password, chainingOld, chainingCurrent, linearOld, linearCurrent);

        System.out.println("Password: " + password);
        System.out.println("Strong: " + (isStrong ? "YES" : "NO"));

        reportSearchCosts(password, chainingOld, chainingCurrent, linearOld, linearCurrent);
    }

    static boolean isStrongPassword(String pwd,
                                    SeparateChainingHashTable chainingOld,
                                    SeparateChainingHashTable chainingCurrent,
                                    LinearProbingHashTable linearOld,
                                    LinearProbingHashTable linearCurrent) {

        if (pwd.length() < 8) return false;

        if (inDictionary(pwd, chainingOld, chainingCurrent, linearOld, linearCurrent)) return false;


        if (pwd.length() > 1 && Character.isDigit(pwd.charAt(pwd.length()-1))) {
            String base = pwd.substring(0, pwd.length()-1);
            if (inDictionary(base, chainingOld, chainingCurrent, linearOld, linearCurrent)) return false;
        }

        return true;
    }

    static boolean inDictionary(String word,
                                SeparateChainingHashTable chainingOld,
                                SeparateChainingHashTable chainingCurrent,
                                LinearProbingHashTable linearOld,
                                LinearProbingHashTable linearCurrent) {
 
        IntWrapper comp = new IntWrapper();
        int val = chainingCurrent.get(word, comp);
        return val != -1;
    }

    static void reportSearchCosts(String pwd,
                                  SeparateChainingHashTable chainingOld,
                                  SeparateChainingHashTable chainingCurrent,
                                  LinearProbingHashTable linearOld,
                                  LinearProbingHashTable linearCurrent) {

        String base = pwd;
        boolean hasDigitSuffix = pwd.length() > 1 && Character.isDigit(pwd.charAt(pwd.length()-1));
        if (hasDigitSuffix) {
            base = pwd.substring(0, pwd.length()-1);
        }

        
        System.out.println("Search costs for '" + pwd + "':");
        printSearchCosts(pwd, chainingOld, chainingCurrent, linearOld, linearCurrent);

      
        if (hasDigitSuffix) {
            System.out.println("Search costs for base word '" + base + "':");
            printSearchCosts(base, chainingOld, chainingCurrent, linearOld, linearCurrent);
        }
    }

    static void printSearchCosts(String key,
                                 SeparateChainingHashTable chainingOld,
                                 SeparateChainingHashTable chainingCurrent,
                                 LinearProbingHashTable linearOld,
                                 LinearProbingHashTable linearCurrent) {
        IntWrapper comp = new IntWrapper();
        
        comp.value = 0;
        chainingOld.get(key, comp);
        System.out.println("  Separate Chaining (oldHash): " + comp.value + " comparisons");

        comp.value = 0;
        chainingCurrent.get(key, comp);
        System.out.println("  Separate Chaining (currentHash): " + comp.value + " comparisons");

        comp.value = 0;
        linearOld.get(key, comp);
        System.out.println("  Linear Probing (oldHash): " + comp.value + " comparisons");

        comp.value = 0;
        linearCurrent.get(key, comp);
        System.out.println("  Linear Probing (currentHash): " + comp.value + " comparisons");
    }
}
