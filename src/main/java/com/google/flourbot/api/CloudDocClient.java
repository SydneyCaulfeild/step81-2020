package com.google.flourbot.api;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface CloudDocClient {
  public CloudSheet getCloudSheet(String documentId) throws IOException, GeneralSecurityException;
}
