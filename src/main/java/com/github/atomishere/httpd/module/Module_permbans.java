package com.github.atomishere.httpd.module;

import java.io.File;

import com.github.atomishere.banning.PermbanList;
import com.github.atomishere.AtomFreedomMod;
import com.github.atomishere.banning.PermbanList;
import com.github.atomishere.httpd.HTTPDaemon;
import com.github.atomishere.httpd.NanoHTTPD;

public class Module_permbans extends HTTPDModule
{

    public Module_permbans(AtomFreedomMod plugin, NanoHTTPD.HTTPSession session)
    {
        super(plugin, session);
    }

    @Override
    public NanoHTTPD.Response getResponse()
    {
        File permbanFile = new File(plugin.getDataFolder(), PermbanList.CONFIG_FILENAME);
        if (permbanFile.exists())
        {
            return HTTPDaemon.serveFileBasic(new File(plugin.getDataFolder(), PermbanList.CONFIG_FILENAME));
        }
        else
        {
            return new NanoHTTPD.Response(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT,
                    "Error 404: Not Found - The requested resource was not found on this server.");
        }
    }
}
