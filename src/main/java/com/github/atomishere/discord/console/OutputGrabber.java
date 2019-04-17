package com.github.atomishere.discord.console;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OutputGrabber extends AbstractAppender implements Runnable {
    private final MessageChannel outputChannel;

    private final List<String> queue = new ArrayList<>();

    protected OutputGrabber(MessageChannel outputChannel) {
        super("OutputGrabber", null, null);

        this.outputChannel = outputChannel;
        start();
    }

    @Override
    public void append(LogEvent event) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("HH:mm:ss");

        String message = "[" + sdf.format(new Date(event.getTimeMillis())) + " " + event.getLevel().toString() + "] " + event.getMessage().getFormattedMessage();
        queue.add(message);
    }


    @Override
    public void run() {
        List<String> toRemove = new ArrayList<>();

        List<String> localQueue = new ArrayList<>(this.queue);

        for(String message : localQueue) {
            outputChannel.sendMessage(message).complete();
            toRemove.add(message);
        }

        queue.removeAll(toRemove);
    }
}
