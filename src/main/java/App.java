import java.util.Map;
import java.util.HashMap;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/home.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/newCategory", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/newCategory.vtl");
      model.put("categories", Category.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/categoryList", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/categoryList.vtl");

      Category category = new Category(request.queryParams("name"));
      category.save();

      model.put("category", category);
      model.put("categories", Category.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/categoryDetail.vtl");

      model.put("category", Category.find(Integer.parseInt(request.params("id"))));

      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:id/newTask", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/newTask.vtl");
      model.put("category", Category.find(Integer.parseInt(request.params("id"))));
      model.put("tasks", Task.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/:id/categoryTasks", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      System.out.println(request.queryParams("categoryId"));
      Category category = Category.find(Integer.parseInt(request.params("id")));
      Integer categoryId = category.getId();

      String description = request.queryParams("newTask");

      Task newTask = new Task(description, categoryId);
      newTask.save();

      model.put("task", newTask);
      model.put("template", "templates/categoryTasks.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }


}
