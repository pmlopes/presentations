var animate = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || function (callback) {
  window.setTimeout(callback, 1000 / 60)
};

var canvas = document.getElementById('canvas');
var context = canvas.getContext('2d');

var eventBus = new EventBus('//' + window.location.host + '/eventbus', {
  vertxbus_reconnect_attempts_max: Infinity, // Max reconnect attempts
  vertxbus_reconnect_delay_min: 500, // Initial delay (in ms) before first reconnect attempt
  vertxbus_reconnect_delay_max: 2500, // Max delay (in ms) between reconnect attempts
  vertxbus_reconnect_exponent: 2, // Exponential backoff factor
  vertxbus_randomization_factor: 0.5 // Randomization factor between 0 and 1
});

eventBus.enableReconnect(true);

eventBus.onopen = function () {

  var player = new Player();
  var ball = new Ball(200, 300);
  var computer = new Computer(ball);
  var slowDown = 2;

  var render = function () {
    context.fillStyle = "#FF00FF";
    context.fillRect(0, 0, canvas.width, canvas.height);
    player.render();
    computer.render();
    ball.render();
  };

  var frameCounter = 0;

  var update = function () {
    player.update();
    ball.update(player.paddle, computer.paddle);
  };

  var step = function () {
    if (++frameCounter%slowDown===0) {
      update();
    }
    render();
    animate(step);
  };

  function Paddle(x, y, width, height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.x_speed = 0;
    this.y_speed = 0;
  }

  Paddle.prototype.render = function () {
    context.fillStyle = "#0000FF";
    context.fillRect(this.x, this.y, this.width, this.height);
  };

  Paddle.prototype.move = function (x, y) {
    this.x += x;
    this.y += y;
    this.x_speed = x;
    this.y_speed = y;
    if (this.x < 0) {
      this.x = 0;
      this.x_speed = 0;
    } else if (this.x + this.width > 400) {
      this.x = 400 - this.width;
      this.x_speed = 0;
    }
  };

  function Computer(ball) {
    this.paddle = new Paddle(175, 10, 50, 10);
    // keep track of the ball
    this.ball = ball;
    var self = this;

    var update = function () {
      eventBus.send('paas.ai', {ball: ball, paddle: self.paddle}, function (err, reply) {
        if (!err) {
          slowDown = 2;
          self.paddle.move(reply.body.diff, 0);
          if (reply.body.x != null) {
            self.paddle.x = reply.body.x;
          }
        } else {
          slowDown = 8;
        }
        // re-enqueue the update
        setTimeout(function () {
          update();
        }, 30 * slowDown);
      });
    };
    // start the AI
    update();
  }

  Computer.prototype.render = function () {
    this.paddle.render();
  };

  function Player() {
    this.paddle = new Paddle(175, 580, 50, 10);
  }

  Player.prototype.render = function () {
    this.paddle.render();
  };

  Player.prototype.update = function () {
    for (var key in keysDown) {
      var value = Number(key);
      if (value === 37) {
        this.paddle.move(-4, 0);
      } else if (value === 39) {
        this.paddle.move(4, 0);
      } else {
        this.paddle.move(0, 0);
      }
    }
  };

  function Ball(x, y) {
    this.x = x;
    this.y = y;
    this.x_speed = 0;
    this.y_speed = 3;
  }

  Ball.prototype.render = function () {
    context.beginPath();
    context.arc(this.x, this.y, 5, 2 * Math.PI, false);
    context.fillStyle = "#000000";
    context.fill();
  };

  Ball.prototype.update = function (paddle1, paddle2) {
    this.x += this.x_speed;
    this.y += this.y_speed;
    var top_x = this.x - 5;
    var top_y = this.y - 5;
    var bottom_x = this.x + 5;
    var bottom_y = this.y + 5;

    if (this.x - 5 < 0) {
      this.x = 5;
      this.x_speed = -this.x_speed;
    } else if (this.x + 5 > 400) {
      this.x = 395;
      this.x_speed = -this.x_speed;
    }

    if (this.y < 0 || this.y > 600) {
      this.x_speed = 0;
      this.y_speed = 3;
      this.x = 200;
      this.y = 300;
    }

    if (top_y > 300) {
      if (top_y < (paddle1.y + paddle1.height) && bottom_y > paddle1.y && top_x < (paddle1.x + paddle1.width) && bottom_x > paddle1.x) {
        this.y_speed = -3;
        this.x_speed += (paddle1.x_speed / 2);
        this.y += this.y_speed;
      }
    } else {
      if (top_y < (paddle2.y + paddle2.height) && bottom_y > paddle2.y && top_x < (paddle2.x + paddle2.width) && bottom_x > paddle2.x) {
        this.y_speed = 3;
        this.x_speed += (paddle2.x_speed / 2);
        this.y += this.y_speed;
      }
    }
  };

  animate(step);
};

var keysDown = {};

window.addEventListener("keydown", function (event) {
  keysDown[event.keyCode] = true;
});

window.addEventListener("keyup", function (event) {
  delete keysDown[event.keyCode];
});
