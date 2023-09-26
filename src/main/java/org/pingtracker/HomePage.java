package org.pingtracker;

import fi.iki.elonen.NanoHTTPD;
import org.dynasite.page.HTMLPageTemplate;

import java.util.List;

public class HomePage extends HTMLPageTemplate {

    private final List<PingRecord> pingRecords;

    public HomePage(List<PingRecord> pingRecords) {
        super(loadFromResource("/index.html"));
        this.pingRecords = pingRecords;
    }

    @Override
    protected String formatResponse(String html, NanoHTTPD.IHTTPSession session) {
        return html;
    }
}
