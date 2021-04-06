package com.highlighter;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.FileUtils.readFileToString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HighlighterTest {

  private final Path inputPath;
  private final Path expectedPath;
  private final String textToHighlight;
  private final Highlighter highlighter;

  @Parameters(name = " {index}. {0} ")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"no_overlap_1", "Please read first section of"},
        {"no_overlap_2", "Hello, welcome to the text "},
        {"full_overlap_1_with_bordering", "Hello, welcome to the text document.Please read first section of  chart. As soon as you come to the conclusion...This text is located in inner-section, effectively a subsection of super section. This section"},
        {"partially_overlap_with_bordering", "Hello, welcome to the text document.Please read first section of  chart. As soon as you come to the conclusion...This text is located in inner-section, effectively a subsection of super section. T"},
    });
  }

  public HighlighterTest(String name, Object textToHighlight) {
    this.inputPath = Paths.get("src", "test", "resources", name, "input.html");
    this.expectedPath = Paths.get("src", "test", "resources", name, "expected.html");
    this.textToHighlight = (String) textToHighlight;
    this.highlighter = new Highlighter();
  }

  @Test
  public void transform() throws IOException {
    String inputHtml = readFileToString(inputPath.toFile(), UTF_8);
    String expectedHtml = readFileToString(expectedPath.toFile(), UTF_8);
    String actualHtml = highlighter.transform(inputHtml, this.textToHighlight);
    try {
      Assert.assertEquals(expectedHtml, actualHtml);
    } catch (ComparisonFailure comparisonFailure) {
      FileUtils.writeStringToFile(
          new File(inputPath.toFile().getParentFile(), "actual.html"),
          actualHtml, UTF_8);
      throw comparisonFailure;
    }
  }
}