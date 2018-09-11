class Dot {
  // Dot object which will inherit it's position from the parent line
  constructor(x, y, width, color) {
    this.width = width;
    this.height = 10;
    this.opacity = 1;
    this.x = x;
    this.y = y;
    this.vx = 1;
    this.direction = "right";
    this.color = color;
  }

  draw(ctx) {
    ctx.fillStyle = this.color;
    ctx.beginPath();
    ctx.rect(this.x, this.y, this.width, this.height);
    ctx.fill();
    ctx.closePath();
  }

  move() {
    if (this.direction === "left") {
      this.moveLeft();
    } else if (this.direction === "right") {
      this.moveRight();
    }
  }

  moveLeft() {
    this.x += -this.vx;

    if (this.x <= this.vx) {
      this.x += this.vx;
    }
  }

  moveRight() {
    this.x += this.vx;
  }
}


export {
  Dot as default
}
