const canvas = document.querySelector('#plot__canvas')
ctx = canvas.getContext('2d');
canvas.width = 500;
canvas.height = 500;
let savedRad = localStorage.getItem('rad');
let num2 = JSON.parse(savedRad);
let num = Number(num2);
let rval;
if (savedRad != null) {
    rval = num;
}
else {
    rval = 1;
}
document.getElementById('selectedRValue').innerHTML = rval;
let xval = 1;
let yval = 0;
let info = $('.input-form__info');
const canone = 150;

const X_VALUES = [-3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0, 4.0, 5.0];
const Y_MIN = -3, Y_MAX = 5;
const R_VALUES = [1, 1.5, 2, 2.5, 3];
function checkArea(x, y, r) {
    if ((r >= x && x >= 0 && y <= 0 && y >= -r / 2) ||
    (x <= 0 && y >= 0 && (x * x + y * y) <= r * r) ||
    (x >= 0 && y >= 0 && y <= (-2 * x + r))) {
        return true;
    }
    else {
        return false;
    }
}

function drawTablePoint(x, y, r) {
    hitResult = checkArea(x, y, r);
    if (hitResult) {
        ctx.fillStyle = 'green';
    }
    else {
        ctx.fillStyle = 'red';
    }
    ctx.beginPath();
    ctx.arc(
        x / r * canone + canvas.width / 2,
        - y / r * canone + canvas.height / 2,
        4, 0, 2 * Math.PI);
    ctx.fill();
}

function loadTablePoints() {
    let rows = [];
    let headers = $(".result-table th");

    $(".result-table tr").each(function(index) {
      let cells = $(this).find("td");
      rows[index] = {};
      cells.each(function(cellIndex) {
        rows[index][$(headers[cellIndex]).html()] = $(this).html().replace(/\s/g, "");
      });
    });

    for (let i = 0; i < rows.length; i++) {
      if (rows[i]['X'] === "" || rows[i]['Y'] === "") continue;
      drawTablePoint(
          rows[i]['X'],
          rows[i]['Y'],
//          rows[i]['R'],
          rval);
//          rows[i]['Результат']);
    }

}

$('.r-button').on('click', function(event) {
    rval = Number($(this).val());
    localStorage.setItem('rad', JSON.stringify(rval));
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    drawGraph();
    loadTablePoints();
//    location.reload();
});
$('.x-button').on('click', function(event) {
  xval = $(this).val();
});

$('.submit-button').on('click', function(event) {
    if (!validateForm()) {
        event.preventDefault();
    } else {
    localStorage.setItem('rad', JSON.stringify(rval));
    $('.input-form__hidden_x input[type=hidden]').val(xval);
    $('.input-form__hidden_r input[type=hidden]').val(rval);
    }
});
function isNumeric(x) {
    return !isNaN(parseFloat(x)) && isFinite(x);
}
function validateX() {
    if (isNumeric(xval) && (parseFloat(xval)) >= -3 && (parseFloat(xval)) <= 5) {
      info.text('Введите координаты точки')
      return true;
    } else {
      info.text('Выберите значение X!')
      return false;
    }
}

function validateY() {
    yval = $('.text-input').val();

    if (isNumeric(yval) && yval >= Y_MIN && yval <= Y_MAX)
    {
      info.text('Введите координаты точки')
      return true;
    } else {
      info.text(`Введите значение Y от ${Y_MIN} до ${Y_MAX}!`)
      return false;
    }
}

function validateR() {
    if (isNumeric(rval) && R_VALUES.includes(parseFloat(rval))) {
      info.text('Введите координаты точки')
      return true;
    } else {
      info.text('Выберите значение R!')
      return false;
    }
}

function validateForm() {
    return validateX() && validateY() && validateR();
}

$(canvas).on('click', function(event) {
    let rect = canvas.getBoundingClientRect();
    let x = (event.clientX - rect.left);
    let y = (event.clientY - rect.top);
    let r = rval;
    x = ((x - 250) / 150 * r).toFixed(5);
    y = ((-(y - 250)) / 150 * r).toFixed(5);
    localStorage.setItem('rad', JSON.stringify(r));
    xval = x;
    $('.text-input').val(y);
    $('.submit-button').click();
});

drawGraph();

function drawGraph() {
    let x = 50;
    let y = 50;
    ctx.beginPath();
    ctx.fillStyle = '#09f';
    ctx.arc(x * 5, y * 5, 150, Math.PI, Math.PI / 2 * 3);
    ctx.fill();
    ctx.beginPath();
    ctx.moveTo(x * 5, y * 5);
    ctx.lineTo(x * 2, y * 5);
    ctx.lineTo(x * 5, y * 2);
    ctx.closePath();
    ctx.fill();
    ctx.beginPath();
    ctx.strokeStyle = '#09f';
    ctx.lineTo(x * 2, y * 5);
    ctx.lineTo(x * 5, y * 2);
    ctx.stroke();
    //Прямоугольник
    ctx.fillRect(x * 5, y * 5, 150, 75);
    //Треугольник
    ctx.beginPath();
    ctx.moveTo(x * 6.5, y * 5);
    ctx.lineTo(x * 5, y * 5);
    ctx.lineTo(x * 5, y * 2);
    ctx.closePath();
    ctx.fill();

    ctx.strokeStyle = '#000';
    ctx.fillStyle = '#000';
    ctx.beginPath();
    ctx.moveTo(x * 5, y);
    ctx.lineTo(x * 5, y * 9);
    ctx.moveTo(x * 5, y);
    ctx.lineTo(x * 5.2, y * 1.2)
    ctx.moveTo(x * 5, y);
    ctx.lineTo(x * 4.8, y * 1.2)
    ctx.moveTo(x, y * 5);
    ctx.lineTo(x * 9, y * 5);
    ctx.lineTo(x * 8.8, y * 5.2);
    ctx.moveTo(x * 9, y * 5);
    ctx.lineTo(x * 8.8, y * 4.8);
    ctx.moveTo(x * 4.9, y * 2);
    ctx.lineTo(x * 5.1, y * 2);
    ctx.moveTo(x * 4.9, y * 3.5);
    ctx.lineTo(x * 5.1, y * 3.5);
    ctx.moveTo(x * 4.9, y * 6.5);
    ctx.lineTo(x * 5.1, y * 6.5);
    ctx.moveTo(x * 4.9, y * 8);
    ctx.lineTo(x * 5.1, y * 8);
    ctx.moveTo(x * 2, y * 4.9);
    ctx.lineTo(x * 2, y * 5.1);
    ctx.moveTo(x * 3.5, y * 4.9);
    ctx.lineTo(x * 3.5, y * 5.1);
    ctx.moveTo(x * 6.5, y * 4.9);
    ctx.lineTo(x * 6.5, y * 5.1);
    ctx.moveTo(x * 8, y * 4.9);
    ctx.lineTo(x * 8, y * 5.1);
    ctx.font = "20px Arial";
    ctx.fillText("y", x * 5.1, y);
    ctx.fillText("x", x * 9, y * 4.9);
    ctx.fillText("R", x * 5.2, y * 2.1);
    ctx.fillText("R/2", x * 5.2, y * 3.6);
    ctx.fillText("-R/2", x * 5.2, y * 6.6);
    ctx.fillText("-R", x * 5.2, y * 8.1);
    ctx.fillText("-R", x * 2.1, y * 4.8);
    ctx.fillText("-R/2", x * 3.6, y * 4.8);
    ctx.fillText("R/2", x * 6.6, y * 4.8);
    ctx.fillText("R", x * 8.1, y * 4.8);
    ctx.stroke();
}

function updateXValue(value) {
    document.getElementById('selectedXValue').innerHTML = value;
}
function updateRValue(value) {
   document.getElementById('selectedRValue').innerHTML = value;
}

loadTablePoints();



