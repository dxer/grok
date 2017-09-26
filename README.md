# grok

Grok是一个数据结构化工具。只需要通过简单地变量定义，我们就可以将文本格式的字符串，转换成为具体的结构化的数据。

默认加载了Logstash的Grok变量，同时支持自定义的Grok变量。

```
Grok grok = new Grok();
grok.addPattern("TEST", "\\d+");
grok.compile(";%{TEST:a}");
MatchResult result = grok.match(";123456;223456");
result.printResults();
```
