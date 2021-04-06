package com.highlighter;

public enum TagType {
  DIV("<div"),
  DIV_END("</div>"),
  LINK("<a"),
  LINK_END("</a>"),
  SPAN("<span"),
  SPAN_END("</span>"),
  OPEN("<"),
  CLOSE(">"),
  ;

  private final String value;

  TagType(String value) {
    this.value = value;
  }

    public String getValue() {
        return value;
    }
}