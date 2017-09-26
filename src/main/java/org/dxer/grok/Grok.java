package org.dxer.grok;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author linghf
 * @create 2017-09-26 8:37
 **/
public class Grok {
    private Pattern pattern;

    private List<String> fields = Lists.newArrayList();

    private String PATTERN_START = "%{";
    private String PATTERN_STOP = "}";
    private String PATTERN_DELIMITER = ":";
    private String EMPTY = "";

    private String compileExpression(String expression) {
        StringBuilder regex = new StringBuilder();

        StringBuilder tmpExpression = new StringBuilder();
        boolean isNewExpressStart = false;
        for (int i = 0; i < expression.length(); i++) {
            tmpExpression.append(expression.charAt(i));

            String subExpression = tmpExpression.toString();
            if (subExpression.endsWith(PATTERN_START)) {
                String ss = subExpression.substring(0, subExpression.indexOf(PATTERN_START));
                regex.append(ss);
                tmpExpression.setLength(0);
                tmpExpression.append(PATTERN_START);
                isNewExpressStart = true;
            }


            if (isNewExpressStart && PATTERN_STOP.equals(expression.charAt(i) + "")) {
                String express = tmpExpression.toString();
                express = express.replace(PATTERN_START, EMPTY);
                express = express.replace(PATTERN_STOP, EMPTY);

                String[] strs = express.split(PATTERN_DELIMITER);
                if (strs != null && strs.length == 2) {
                    String patternRegx = GrokDic.getPatterns().get(strs[0].trim());
                    if (!Strings.isNullOrEmpty(patternRegx)) {
                        String field = strs[1].trim();
                        regex.append("(?<").append(field).append(">").append(patternRegx).append(")");
                        fields.add(field);
                        tmpExpression.setLength(0);
                        isNewExpressStart = false;
                    }
                }
            }
        }

        if (tmpExpression.length() > 0) {
            regex.append(tmpExpression.toString());
        }

        return regex.toString();
    }

    public Pattern compile(String expression) {
        String regex = compileExpression(expression);
        pattern = Pattern.compile(regex);
        return pattern;
    }


    public MatchResult match(CharSequence input) {
        MatchResult matchResult = new MatchResult();

        Matcher matcher = pattern.matcher(input);

        Map<String, String> capture = Maps.newHashMap();
        if (matcher.find()) {
            for (String field : fields) {
                String value = matcher.group(field);
                capture.put(field, value);
            }
        }
        matchResult.setCapture(capture);

        return matchResult;
    }

    public void addPattern(String fieldName, String regex) {
        GrokDic.addPattern(fieldName, regex);
    }

    public String getPattern(String fieldName) {
        return GrokDic.getPatterns().get(fieldName);
    }

    public Map<String, String> getPatterns() {
        return GrokDic.getPatterns();
    }

    public void addPatternFile(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            GrokDic.loadDefaultPatterns(new InputStreamReader(fis, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Grok grok = new Grok();
        grok.addPattern("TEST", "\\d+");
        grok.compile(";%{TEST:a}");
        MatchResult result = grok.match(";123456;223456");
        result.displayResults();
    }
}
