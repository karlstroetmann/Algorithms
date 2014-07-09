import java.math.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyScanner
{
    private ArrayStack<Object> mTokenStack;
    private static final Rule[] RULES = {
            new IntRule("'0|[1-9][0-9]*"),
            new Rule("\\*\\*|[+\\-*/%()]"),
            new SkipRule("\\s+")
    };

    public MyScanner(InputStream stream)
    {
        System.out.println("Enter arithmetic expression:");

        Scanner scanner = new Scanner(stream);
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            input.append(scanner.nextLine()).append('\n');
        }

        Matcher m = Pattern.compile("ignored")
                .matcher(input)
                .useTransparentBounds(true)
                .useAnchoringBounds(false);
        List<Object> tokens = new ArrayList<Object>();
        int offset = 0;
        final int end = input.length();

        while (offset < end) {
            m.region(offset, end);
            for (Rule rule : RULES) {
                if (m.usePattern(rule.pattern).lookingAt()) {
                    if (!(rule instanceof SkipRule)) {
                        tokens.add(rule.convert(input.substring(m.start(), m.end())));
                    }
                    offset = m.end() - 1;
                    break;
                }
            }
            offset++;
        }

        Collections.reverse(tokens);
        this.mTokenStack = new ArrayStack<Object>();
        for (Object o : tokens) {
            this.mTokenStack.push(o);
        }
    }

    public ArrayStack<Object> getTokenStack() 
    {
        return mTokenStack;
    }

    private static class Rule {
        public final Pattern pattern;

        public Rule(String pattern) {
            this.pattern = Pattern.compile(pattern);
        }

        public Object convert(String match) {
            return match;
        }
    }

    private static class SkipRule extends Rule {
        private SkipRule(String pattern) {
            super(pattern);
        }
    }

    private static class IntRule extends Rule {
        private IntRule(String pattern) {
            super(pattern);
        }

        @Override
        public Object convert(String match) {
            return new BigInteger(match);
        }
    }
}

    
        
            
        
