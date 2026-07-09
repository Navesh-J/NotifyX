package com.navesh.notifyx.dto;

public record ChannelResult(
        String provider,
        boolean success,
        String message
) {}