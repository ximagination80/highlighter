package com.highlighter;

public class HtmlProcessor {

  private final StringBuilder html;
  private final int endPosition;

  private int currentPosition;

  public HtmlProcessor(final String html, final int position) {
    this.html = new StringBuilder(html);
    this.endPosition = html.length();
    this.currentPosition = position;
  }

  private TagType getTagType(final String lookup) {
    char c = lookup.charAt(0);
    if (c == '<') {
      for (TagType each : TagType.values()) {
        if (lookup.startsWith(each.getValue())) {
          return each;
        }
      }
    } else if (c == '>') {
      return TagType.CLOSE;
    }
    return null;
  }

  public boolean canContinue() {
    return currentPosition < endPosition;
  }

  public void shift() {
    currentPosition++;
  }

  public TagType lookupNext() {
    int end = Math.min(currentPosition + 10, endPosition);
    String lookup = html.subSequence(currentPosition, end).toString();
    return getTagType(lookup);
  }

  public char getChar() {
    return html.charAt(currentPosition);
  }

  public String consumeTill(TagType tagType) {
    return consumeTill(tagType, true);
  }

  public String consumeTill(TagType tagType, boolean inclusive) {
    StringBuilder result = new StringBuilder();
    while (canContinue()) {
      if (lookupNext() == tagType) {
        if (inclusive) {
          for (int index = 0; index < tagType.getValue().length(); index++) {
            result.append(getChar());
            shift();
          }
        }
        break;
      } else {
        result.append(getChar());
        shift();
      }
    }
    return result.toString();
  }
}