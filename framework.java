import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Framework {
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            logger.severe("Usage: luh [script]");
            System.exit(64); 
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

private static final Logger logger = Logger.getLogger(SchmalebCode.class.getName());

private static void runFile(String path) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path)); 
    run(new String(bytes, Charset.defaultCharset()));
}

private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in); 
    BufferedReader reader = new BufferedReader(input);

    for (;;) {
        logger.severe("> ");
        String line = reader.readLine();
        if (line == null) break; // If readLine reads null then break the loop
        run(line);
    }
}

private static void run(String value) {
    Scanner scanner = new Scanner(value);
    List<Tokens> tokens = scanner.scanTokens();

    // Print tokens as dummy
    for (Token token: tokens) { 
        logger.severe(token);
    }
}

static void error(int line, String message) {
    report(line, "", message);
}


private static void report(int line, String location, String message) {
    logger.severe(message + "\n[line " + line + "] " + location);
    error = true;
}
}