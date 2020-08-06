import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;


public class Lox {
    // Attribute to indicate if there was error in the file
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if(args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        // If the file is specified
        } else if (args.length == 1) {
            runFile(args[0]);
        // Open the prompt
        } else {
            runPrompt();
        }
    }

    // Read a file given its filepath
    private static void runFile(String Path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(Path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    // Running the interpreter interactively
    // Running jlox without an interpreter would drops a prompt
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        // Create the prompt where user can run code line-by-line
        for(;;) {
            System.out.print("> ");
            String line = reader.readLine();
            // If 'Control + D' is pressed without any input, exit the prompt
            if(line == null) break;
            run(line);
            hadError = false;
        }
    }

    private static void run(String source) {
        // Indicate an error in exit code
        if(hadError) System.exit(65);

        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // For now just print the tokens
        for(Token token: tokens) {
            System.out.println(token);
        }
    }

    // Adding a error handler
    static void error(int line, String message){
        report(line, "", message);
    }

    // Producing error message
    private static void report(int line, String where, String message) {
        String.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
