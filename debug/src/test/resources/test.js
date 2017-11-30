var TestSuite = require("vertx-unit").TestSuite;
var TestOptions = require('vertx-unit/options').TestOptions;

TestSuite.create("the_test_suite")
  .test("my_test_case_1", function (context) {
    // Test 1
    var s = "value";
    context.assertEquals("value", s);
  })
  .test("my_test_case_2", function (context) {
    // Test 2
    var s = "value";
    context.assertEquals("value", s);
  })
  .test("my_test_case_3", function (context) {
    // Test 3
    var s = "value";
    context.assertEquals("value", s);
  })
  .run(new TestOptions(JSON.native({
    "reporters" : [
      { "to" : "console" }
    ]
  })))
  .handler(function (ar) {
    if (ar.succeeded()) {
      exit(0);
    } else {
      exit(1);
    }
  });
