package com.myretail.catalog.utils;

import java.util.Date;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utilities {

  private static final Logger log = LoggerFactory.getLogger(Utilities.class);

  /** generate unique product id. */
  public static Long getSeqTrackString() {
    String seqChars = "1234567890";
    Long currtime = new Date().getTime();
    StringBuilder trackstring = new StringBuilder();
    Random rnd = new Random();
    while (trackstring.length() < 9) { // length of the random string.
      int index = (int) (rnd.nextFloat() * seqChars.length());
      trackstring.append(seqChars.charAt(index));
    }
    Long trackLong = Long.parseLong(trackstring.toString()) + currtime;
    log.debug("Generated unique product ID is {}", trackLong);
    return trackLong;
  }
}
