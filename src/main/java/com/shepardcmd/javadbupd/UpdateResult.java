package com.shepardcmd.javadbupd;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateResult {
    private final int version;
    private final String changeSetType;
    private final Date startDate;
    private final Date endDate;
    private final boolean successful;
}
