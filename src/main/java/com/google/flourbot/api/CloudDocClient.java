package com.google.flourbot.api;

import java.io.IOException;
import java.security.GeneralSecurityException;

interface CloudDocClient {

  // TODO: change exception handling to try catch. throw illegal state exception
  public CloudSheet getCloudSheet(String documentId) throws IOException, GeneralSecurityException ;
}
