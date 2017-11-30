const Yoke = require('./yoke');
const dbCall = require('./fake-logic');

const app = new Yoke();

app.use(async (ctx) => {
  console.log('------------------------');
  // Step 1
  const start = new Date;
  console.log(`Started at: ${start}`);
  // Step 2
  try {
    await ctx.next();    
  } catch (e) {
    // there's a 50% change of failure!
    ctx.response.statusCode = 500;
    ctx.response.end('Oopsy!');
  } finally {
    // Step 7
    const ms = new Date - start;
    console.log(`Elapsed: ${ms}ms`);
  }
});

app.use(async (ctx) => {
  // Step 3
  console.log(`Hey, I'm just another middleware`);
  // Step 4
  await ctx.next();
  // Step 6
  console.log('More stuff after body has been set');
});

app.use(async (ctx) => {
  const name = await dbCall();
  // Step 5
  ctx.response.end(`Hello ${name}`);
  console.log('Body has been set');
});

app.accept(vertx.createHttpServer()).listen(8080);
console.log("Server listening @ http://localhost:8080/");
