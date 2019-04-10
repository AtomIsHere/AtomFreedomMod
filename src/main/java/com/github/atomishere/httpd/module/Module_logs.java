package com.github.atomishere.httpd.module;

import java.io.File;

import com.github.atomishere.config.ConfigEntry;
import com.github.atomishere.AtomFreedomMod;
import com.github.atomishere.config.ConfigEntry;
import com.github.atomishere.httpd.NanoHTTPD;

public class Module_logs extends Module_file
{

    public Module_logs(AtomFreedomMod plugin, NanoHTTPD.HTTPSession session)
    {
        super(plugin, session);
    }

    @Override
    public NanoHTTPD.Response getResponse()
    {
        if (ConfigEntry.LOGS_SECRET.getString().equals(params.get("password")))
        {
            return serveFile("latest.log", params, new File("./logs"));
        }
        else
        {
            return new NanoHTTPD.Response(NanoHTTPD.Response.Status.FORBIDDEN, NanoHTTPD.MIME_PLAINTEXT, "Incorrect password.");
        }
    }
}
