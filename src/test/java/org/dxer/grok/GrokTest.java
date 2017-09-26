package org.dxer.grok;

/**
 * @author linghf
 * @create 2017-09-26 16:29
 **/
public class GrokTest {

    public static void main(String[] args) {
        String log = "Jan  1 06:25:43 mailserver14 postfix/cleanup[21403]: BEF25A72965: message-id=<20130101142543.5828399CCAF@mailserver14.example.com>";

        Grok grok = new Grok();
//        System.out.println(grok.getPattern("HTTPD_COMBINEDLOG"));
//        System.out.println(grok.getPattern("QS"));
//        System.out.println(grok.getPattern("QUOTEDSTRING"));
        // grok.compile("%{COMBINEDAPACHELOG}");

        grok.compile("%{SYSLOGBASE} %{POSTFIX_QUEUEID:queue_id}: %{GREEDYDATA:test}");
        MatchResult result = grok.match(log);
        result.printResults();
    }


}
