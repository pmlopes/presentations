const posts = require('../shared/posts');

import React from 'react';

import {renderToString} from 'react-dom/server';
import {match, RouterContext} from 'react-router';

import routes from '../shared/components/routes';

module.exports = function(location, handler) {
  match({routes: routes, location: location}, (err, redirect, props) => {

    if (err) {
      handler.handle(err);
    } else if (redirect) {
      handler.handle(null, redirect);
    } else if (props) {
      const routerContextWithData = (
        <RouterContext
          {...props}
          createElement={(Component, props) => {
            return <Component posts={posts} {...props} />
          }}
        />
      );
      handler.handle(null, null, renderToString(routerContextWithData));
    } else {
      handler.handle(null, null, null);
    }
  });
};
