package drools;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.compiler.PackageBuilder;
import org.drools.core.RuleBase;
import org.drools.core.RuleBaseFactory;
import org.drools.core.WorkingMemory;

import java.io.IOException;
import java.io.StringReader;

public final class DroolsHelper {

  public static WorkingMemory load(Buffer drl) throws IOException, DroolsParserException {
    PackageBuilder packageBuilder = new PackageBuilder();
    packageBuilder.addPackageFromDrl(new StringReader(drl.toString()));
    RuleBase ruleBase = RuleBaseFactory.newRuleBase();
    ruleBase.addPackage(packageBuilder.getPackage());
    return ruleBase.newStatefulSession();
  }

  public static Greeting createGreeting(String message, Handler<Void> andThen) {
    return new Greeting() {
      @Override
      public String getMessage() {
        return message;
      }

      @Override
      public void greet() {
        andThen.handle(null);
      }
    };
  }
}
