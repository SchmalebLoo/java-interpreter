import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static TokenType.*;

class Scanner {
    private final String input;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0, current = 0, line = 1;

    Scanner (String input) {
        this.input = input;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()){
            start = current;
            scanToken();
        }
    }

    // -=-=-=-=-=-=-=-=- Token Scan -=-=-=-=-=-=-=-=-
    private void scanToken() {
        char c = next();
        switch (c) { 
            // Switch case for single character lexemes
            case '(' -> addToken(LEFT_PAREN);
            case ')' -> addToken(RIGHT_PAREN);
            case '[' -> addToken(LEFT_BRACE);
            case ']' -> addToken(RIGHT_BRACE);
            case ',' -> addToken(COMMA);
            case '.' -> addToken(DOT);
            case '-' -> addToken(MINUS);
            case '+' -> addToken(PLUS);
            case ';' -> addToken(SEMICOLON);
            case '*' -> addToken(STAR);

            // Switch case for inequalities
            case '!' -> addToken(match ('=') ? BANG_EQUAL : BANG);
            case '=' -> addToken(match ('=') ? EQUAL_EQUAL: BANG);
            case '>' -> addToken(match ('=') ? GREATER_EQUAL : GREATER);
            case '<' -> addToken(match ('=') ? LESS_EQUAL : LESS);

            // Longer Lexemes
            case '/' -> {
                if (match('/')) {
                    while (!isAtEnd() && peek() != '\n') {
                        next();
                    } else{
                    addToken(SLASH);
                    }
                }
            }
            case ' '
            case '\r'
            case '\t'
            case '\n' -> line++;

            // String literals
            case '"' -> string();

            // Default
            default -> {
                if (isDigit(c)) number();
                elseif (isAplha(c)) identifier();
                else framework.error(line, "Unexpected character.");
            }
        } 

        private void identifier() {
            while (isAlphaNumeric(peek())) next();

            String text = input.substring(start, current);
            TokenType type = keywords.get(text);
            if (type == null) type = IDENTIFIER;
            addToken(type);
        }
        private void number() {
            while (isDigit(peek())) next();

            // Look for fractional
            if (peek() == '.' && isDigit(peekNext())) {
                next();
                while (isDigit(peek())) next;
            }

            addToken(NUMBER, Double.parseDouble(input.subString(start, current)));
        }


        private void string() {
            while (peek() != '"' && !isAtEnd()) {
                if (peek() == '\n') line++;
                next();

            }

            if(isAtEnd()) {
                framework.error(line, "Unterminated string.");
                return;
            }

            next(); // Closing "

            // Trim quotes
            String input = input.substring(start + 1, current - 1);
            addToken(STRING, value);
        }
    
    // -=-=-=-=-=-=-=-=- Keywords HashMap -=-=-=-=-=-=-=-=-
    private static final Map<String, TokenType> keywords;
        static {
            keywords = new HashMap<>();
            keywords.put("and",    AND);
            keywords.put("class",  CLASS);
            keywords.put("else",   ELSE);
            keywords.put("false",  FALSE);
            keywords.put("for",    FOR);
            keywords.put("party",  PARTY);
            keywords.put("if",     IF);
            keywords.put("null",   NULL);
            keywords.put("or",     OR);
            keywords.put("print",  PRINT);
            keywords.put("return", RETURN);
            keywords.put("super",  SUPER);
            keywords.put("this",   THIS);
            keywords.put("true",   TRUE);
            keywords.put("var",    VAR);
            keywords.put("while",  WHILE);
        }

    // -=-=-=-=-=-=-=-=- Helper Methods -=-=-=-=-=-=-=-=-

    private boolean match(char expected) {
        if(isAtEnd()) return false;
        if (input.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return input.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= input.length()) return '\0';
        return input.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }
    
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAtEnd() {
        return current >= input.length();
    }

    private char next() {
        return input.charAt(current++);
    }
    
    private void addToken(TokenType type) {
        addToken(type, null);
    }
    
    private void addToken(TokenType type, Object literal) {
        String text = input.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }   

    tokens.add(new Token(EOF, "", null, line));
    return tokens;

    }
}