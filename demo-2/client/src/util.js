function hexToRgb(hex, opacity) {
  const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
  return result ? "rgba("
  + parseInt(result[1], 16) + ","
  + parseInt(result[2], 16) + ","
  + parseInt(result[3], 16) + "," + opacity + ")"
    : null;
}


// Connect to the socket and display data
// Value in satoshis
function getColorForTxValue(value) {
  const btc = value / 100000000;
  if (btc < 0.1) return hexToRgb("#A7FCA9", 1);
  else if (btc < 0.25) return hexToRgb("#85FF89", 1);
  else if (btc < 0.5) return hexToRgb("#4FFF55", 1);
  else if (btc < 1) return hexToRgb("#AAFF4F", 1);
  else if (btc < 3) return hexToRgb("#F6FF4F", 1);
  else if (btc < 5) return hexToRgb("#FFBE4F", 1);
  else return hexToRgb("#FF4F4F", 1);
}

function getRandomInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

export {
  getColorForTxValue,
  getRandomInt
}
