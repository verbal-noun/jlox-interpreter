import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Lox {
    public static void main(String[] args) throws IOException {
        if(args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
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
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // For now just print the tokens
        for(Token token: tokens) {
            System.out.println(token);
        }
    }
}
