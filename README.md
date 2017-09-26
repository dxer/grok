# grok
grok


```
Grok grok = new Grok();
grok.addPattern("TEST", "\\d+");
grok.compile(";%{TEST:a}");
MatchResult result = grok.match(";123456;223456");
result.printResults();
```
