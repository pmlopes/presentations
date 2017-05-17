import React from 'react';
import { render } from 'react-dom';
import Rx from 'rxjs';
import EventBus from 'vertx3-eventbus-client';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      messages: []
    };
  }

  componentWillMount() {
    Rx.Observable
      .create((observer) => {
        eb.registerHandler('greetings', (err, msg) => {
          observer.next(msg.body.msg);
        });
      })
      .subscribe(message => {
        this.state.messages.unshift(message);
        this.setState({ messages: this.state.messages });
      });
  }

  static sayHello(e) {
    e.preventDefault();
    eb.send('greetings', {msg: 'Hello from React.js!'})
  }

  render() {
    let listItems = this.state.messages.map(message => {
      return (
        <li>{ `${message}` }</li>
      );
    });
    return (
      <div>
        <button onClick={App.sayHello}>Say Hello!</button>
        <ul>
          {listItems}
        </ul>
      </div>
    )
  }
}

const eb = new EventBus(`//${window.location.host}/eventbus`);

eb.onopen = () => {
  render (<App />, document.getElementById('app'));
};
