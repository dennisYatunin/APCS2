/*
   _____          __         _________                                     __
  /  _  \  __ ___/  |_  ____ \_   ___ \  __________________   ____   _____/  |_
 /  /_\  \|  |  \   __\/  _ \/    \  \/ /  _ \_  __ \_  __ \_/ __ \_/ ___\   __\
/    |    \  |  /|  | (  <_> )     \___(  <_> )  | \/|  | \/\  ___/\  \___|  |
\____|__  /____/ |__|  \____/ \______  /\____/|__|   |__|    \___  >\___  >__|
        \/                           \/                          \/     \/

*/

import java.util.HashMap;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AutoCorrect {

    // The lists have all been modified in some way to encompass more cases.
    // They are loaded from external text files because that allows the user
    // to easily change the auto-correct settings by modifying those files.

    public static final char[] vowels = new char[] {
        'A', 'E', 'I', 'O', 'U',
        'a', 'e', 'i', 'o', 'u'
    };

    public static final char[] consonants = new char[] {
        'B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z',
        'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z'
    };

    public static final HashSet<String> requireA = new HashSet<String>() {{
        // original list from https://github.com/languagetool-org/languagetool/blob/master/languagetool-language-modules/en/src/main/resources/org/languagetool/rules/en/det_a.txt
        try {
            BufferedReader file = new BufferedReader(new FileReader("requireA.txt"));
            while (file.ready()) {
                add(file.readLine());
            }
            file.close();
        }
        catch (IOException e) {
            System.err.println("ATTENTION: Missing file requireA.txt");
        }
    }};

    public static final HashSet<String> requireAn = new HashSet<String>() {{
        // original list from https://github.com/languagetool-org/languagetool/blob/master/languagetool-language-modules/en/src/main/resources/org/languagetool/rules/en/det_an.txt
        try {
            BufferedReader file = new BufferedReader(new FileReader("requireAn.txt"));
            while (file.ready()) {
                add(file.readLine());
            }
            file.close();
        }
        catch (IOException e) {
            System.err.println("ATTENTION: Missing file requireAn.txt");
        }
    }};

    public static final HashSet<String> repeatedWords = new HashSet<String>() {{
        // original list from http://en.wikipedia.org/wiki/Wikipedia:Lists_of_common_misspellings/Repetitions
    	try {
            BufferedReader file = new BufferedReader(new FileReader("repeatedWords.txt"));
            while (file.ready()) {
                add(file.readLine());
            }
            file.close();
        }
        catch (IOException e) {
            System.err.println("ATTENTION: Missing file repeatedWords.txt");
        }
    }};

    public static final HashSet<String> badWords = new HashSet<String>() {{
        // original list from http://fffff.at/googles-official-list-of-bad-words/
        try {
            BufferedReader file = new BufferedReader(new FileReader("badWords.txt"));
            while (file.ready()) {
                add(file.readLine());
            }
            file.close();
        }
        catch (IOException e) {
            System.err.println("ATTENTION: Missing file badWords.txt");
        }
    }};

	public static final HashMap<String, String> commonMisspellings = new HashMap<String, String>() {{
        // original list from http://en.wikipedia.org/wiki/Wikipedia:Lists_of_common_misspellings/For_machines
        try {
            BufferedReader file = new BufferedReader(new FileReader("commonMisspellings.txt"));
            String[] words;
            while (file.ready()) {
                words = file.readLine().split(",");
                put(words[0], words[1]);
            }
            file.close();
        }
        catch (IOException e) {
            System.err.println("ATTENTION: Missing file commonMisspellings.txt");
        }
    }};

}