package com.intuit.karate.junit4.http;

import com.intuit.karate.FileUtils;
import com.intuit.karate.Http;
import com.intuit.karate.Match;
import com.intuit.karate.netty.FeatureServer;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author pthomas3
 */
public class JavaHttpTest {
    
    private static FeatureServer server;

    @BeforeClass
    public static void beforeClass() {
        File file = FileUtils.getFileRelativeTo(JavaHttpTest.class, "server.feature");
        server = FeatureServer.start(file, 0, false, null);     
    }
    
    @Test
    public void testHttp() {
        Http http = Http.forUrl("http://localhost:" + server.getPort());
        http.path("echo").get().response().equals("{ uri: '/echo' }");
        Match response = http.path("chrome").get().response();        
        response.equalsText("#[1]");
        String expected = "ws://127.0.0.1:9222/devtools/page/E54102F8004590484CC9FF85E2ECFCD0";
        Match found = response.get("get[0] $..webSocketDebuggerUrl");
        found.equalsText(expected);
    }
    
    @AfterClass
    public static void afterClass() {
        server.stop();
    }    
    
}
