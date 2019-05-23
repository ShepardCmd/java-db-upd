package com.shepardcmd.javadbupd;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateResult {
    private final int version;
    private final String changeSetName;
    private final Date startTime;
    private final Date endTime;
    private final boolean successful;
}
