import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
    public void rootTest() {
      goTo("http://localhost:4567/");
      assertThat(pageSource()).contains("To-Do list builder");
    }

  @Test
  public void categoryIsCreatedTest() {
    goTo("http://localhost:4567");
    click("a", withText("Add a new category"));
    fill("#name").with("Household chores");
    submit("btn");
    assertThat(pageSource()).contains("Household chores");
  }

  @Test
  public void categoryIsDisplayedTest() {
    Category newCategory = new Category("Household chores");
    newCategory.save();
    String categoryPath = String.format("http://localhost:4567/%d", newCategory.getId());
    goTo(categoryPath);
    assertThat(pageSource()).contains("Household chores");
  }
}
