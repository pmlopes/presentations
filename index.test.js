import { TestSuite } from "vertx-unit";

const suite = TestSuite.create("my suite");

suite.test("my test", function (ctx) {
    var value = "val";
    ctx.assertEquals("val2", value);
}).run()