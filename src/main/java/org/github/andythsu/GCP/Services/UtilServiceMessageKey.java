package org.github.andythsu.GCP.Services;

import org.github.andythsu.GCP.Services.Error.MessageKey;

/**
 * @author: Andy Su
 * @Date: 11/6/2018
 */
public class UtilServiceMessageKey extends MessageKey {
    public static String INCOMPATIBLE_INPUT = "Input has incompatible types";
    public static String WRONG_TYPE_INPUT = "Input has wrong types";
    public UtilServiceMessageKey(int status, String tags, String message){
        super(status, tags, message);
    }
}
