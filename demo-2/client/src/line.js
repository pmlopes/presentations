import Dot from './dot.js'

const lineSpacing = 15;

class Line {
  constructor(canvas, row) {
    this.canvas = canvas;
    this.row = row;
    this.dots = [];
    this.width = 600;
    this.height = 10;
    this.y = row * lineSpacing + 10;
  }

  pushDot(width, color) {
    this.dots.push(new Dot(this.row * -15, this.y, width, color));
  }

  destroyDot(i) {
    if (this.dots[i].x >= this.canvas.width) {
      this.dots.splice(i, 1);
    }
  }

  drawDots(ctx) {
    for (let i = 0; i < this.dots.length; i++) {
      this.dots[i].draw(ctx);
    }
  }

  moveDots() {
    for (let i = 0; i < this.dots.length; i++) {
      this.dots[i].move();
      this.destroyDot(i);
    }
  }
}

export {
  Line as default
}
