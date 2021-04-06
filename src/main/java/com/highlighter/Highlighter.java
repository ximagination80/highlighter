package com.highlighter;

public class Highlighter {

  String transform(final String html, final String highlight) {
    int highlightCharIndex = 0;
    StringBuilder textToHighlight = new StringBuilder();
    StringBuilder out = new StringBuilder();

    HtmlProcessor processor = new HtmlProcessor(html, 0);
    while (processor.canContinue()) {
      TagType tagType = processor.lookupNext();
      if (tagType != null) {
        switch (tagType) {
          case DIV:
            flushWithBold(textToHighlight, out);
            out.append(processor.consumeTill(TagType.CLOSE));
            break;

          case SPAN:
            if (textToHighlight.length() > 0) {
              processor.consumeTill(TagType.CLOSE); // consume start
              String innerText = processor.consumeTill(TagType.SPAN_END, false); // inner text
              processor.consumeTill(TagType.SPAN_END, true); // consume end

              for (int consumeIndex = 0; consumeIndex < innerText.length(); consumeIndex++) {
                char c = innerText.charAt(consumeIndex);
                if (highlight.length() > highlightCharIndex &&
                    c == highlight.charAt(highlightCharIndex)) {
                  textToHighlight.append(c);
                  highlightCharIndex++;
                } else {
                  flushWithBold(textToHighlight, out);
                  flushWithBold(new StringBuilder(innerText.substring(consumeIndex)), out);
                  break;
                }
              }
            } else {
              out.append(processor.consumeTill(TagType.SPAN_END));
            }
            break;

          case LINK:
            if (textToHighlight.length() > 0) {
              textToHighlight.append(processor.consumeTill(TagType.LINK_END));
            } else {
              out.append(processor.consumeTill(TagType.LINK_END));
            }
            break;

          case DIV_END:
          case SPAN_END:
          case CLOSE:
            out.append(processor.consumeTill(tagType));
            break;

          case LINK_END:
            throw new RuntimeException("Should not be thrown");
          default:
            throw new RuntimeException("Unknown tag " + tagType);
        }
      } else {
        char c = processor.getChar();
        if (highlight.length() > highlightCharIndex &&
            c == highlight.charAt(highlightCharIndex)) {
          textToHighlight.append(c);
          highlightCharIndex++;
        } else {
          flushWithBold(textToHighlight, out);
          out.append(c);
        }
        processor.shift();
      }
    }

    return out.toString();
  }

  private void flushWithBold(StringBuilder textToHighlight, StringBuilder out) {
    if (textToHighlight.length() > 0) {
      out.append(bold(textToHighlight.toString()));
      textToHighlight.setLength(0);
    }
  }

  private String bold(final String msg) {
    return "<span class=\"bold\">" + msg + "</span>";
  }

}
