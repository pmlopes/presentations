/// <reference types="@vertx/core/runtime" />
// @ts-check
const util = require('util');

// this example shows a high performance implementation
// of a http REST API backed by PostgreSQL

// docker build -t postgres-tbf postgres
// docker run --rm -it -p 5432:5432 --name codeone-postgres postgres-tbf
// npm start

import {Router} from '@vertx/web';

import {PgClient, Tuple} from '@reactiverse/reactive-pg-client';
import {PgPoolOptions} from '@reactiverse/reactive-pg-client/options';

const SELECT_WORLD = "SELECT id, randomnumber from WORLD where id=$1";

const app = Router.router(vertx);

let client = PgClient.pool(
  vertx,
  new PgPoolOptions()
    .setUser('benchmarkdbuser')
    .setPassword('benchmarkdbpass')
    .setDatabase('hello_world'));

app.get('/').handler(
  ctx => {
    client.preparedQuery(SELECT_WORLD, Tuple.of(1), res => {
      if (res.succeeded()) {
        let resultSet = res.result().iterator();

        if (!resultSet.hasNext()) {
          // no result found
          ctx.fail(404);
          return;
        } else {
          let row = resultSet.next();

          ctx.response()
            .putHeader("Content-Type", "application/json")
            .end(JSON.stringify({id: row.getInteger(0), randomNumber: row.getInteger(1)}));
        }
      } else {
        ctx.fail(res.cause());
      }
    });
  }

  // // Step #2
  // ctx => {
  //   // vert.x promise
  //   let postgres = util.promisify(client);
  //
  //   postgres
  //     .preparedQuery(SELECT_WORLD, Tuple.of(1))
  //     .then(rs => {
  //       let resultSet = rs.iterator();
  //
  //       if (!resultSet.hasNext()) {
  //         ctx.fail(404);
  //       } else {
  //         let row = resultSet.next();
  //         ctx.response()
  //           .putHeader("Content-Type", "application/json")
  //           .end(JSON.stringify({id: row.getInteger(0), randomNumber: row.getInteger(1)}));
  //       }
  //     })
  //     .catch(e => ctx.fail(e));
  // }


  // // Step #3
  // ctx => {
  //   (async function () {
  //     // vert.x promise
  //     let postgres = util.promisify(client);
  //
  //     try {
  //       let rs = await postgres.preparedQuery(SELECT_WORLD, Tuple.of(1));
  //       let resultSet = rs.iterator();
  //
  //       if (!resultSet.hasNext()) {
  //         ctx.fail(404);
  //       } else {
  //         let row = resultSet.next();
  //         ctx.response()
  //           .putHeader("Content-Type", "application/json")
  //           .end(JSON.stringify({id: row.getInteger(0), randomNumber: row.getInteger(1)}));
  //       }
  //     } catch (e) {
  //       ctx.fail(e);
  //     }
  //   })();
  // }

  // // Step #4
  // async ctx => {
  //   // vert.x promise
  //   let postgres = util.promisify(client);
  //
  //   try {
  //     let rs = await postgres.preparedQuery(SELECT_WORLD, Tuple.of(1));
  //     let resultSet = rs.iterator();
  //
  //     if (!resultSet.hasNext()) {
  //       ctx.fail(404);
  //     } else {
  //       let row = resultSet.next();
  //       ctx.response()
  //         .putHeader("Content-Type", "application/json")
  //         .end(JSON.stringify({id: row.getInteger(0), randomNumber: row.getInteger(1)}));
  //     }
  //   } catch (e) {
  //     ctx.fail(e);
  //   }
  // }
);


vertx
  .createHttpServer()
  .requestHandler(req => app.accept(req))
  .listen(8080);

console.log('Server listening at: http://localhost:8080/');
