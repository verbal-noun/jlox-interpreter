import tokens.TokenType;

import java.util.ArrayList;
import java.util.List;
// Static importing all tokens for cleaner code
import static tokens.TokenType.*;

// Scanner class to scan for token from a line
class Scanner {
    // Storing raw source code as simple string
    private final String source;
    // List to hold generated tokens from source
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    // Constructor
    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while(!isAtEnd()) {
            // We are at the beginning of the new lexeme
            start = current;
            scanToken();
        }
        // Adding optional EOF token to make our parser a little cleaner
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    // Function to scan a single token
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            case '!': addToken(match('=') ? BANG_EQUAL : BANG); break;
            case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL); break;
            case '<': addToken(match('=') ? LESS_EQUAL : LESS); break;
            case '>': addToken(match('=') ? GREATER_EQUAL : GREATER);
            case '/':
                if (match('/')) {
                    // A comment goes until the end of line
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;

            case '\n':
                line++;
                break;
            case '"': string(); break;
            default:
                Lox.error(line, "Unexpected character.");
                break;
        }
    }

    // Function to map string
    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }
        // Unterminated String
        if(isAtEnd()) {
            Lox.error(line, "Unterminated String");
            return;
        }

        // the closing "
        advance();

        // Trim the surrounding quotes
        String value  = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    // Helper function match characters
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    // Peek at the next character
    private char peek() {
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    // Helper function to tell whether we have consumed all characters or not
    private boolean isAtEnd() {
        return current >= source.length();
    }

    // Helper function to advance into the end of current token
    private char advance() {
        current++;
        return source.charAt(current-1);
    }

    // Pretty wrapper
    private void addToken(TokenType type) {
        addToken(type, null);
    }
    // Adding token to the tokens list
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
