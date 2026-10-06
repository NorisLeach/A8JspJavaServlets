const canvas = document.getElementById("snakeCanvas");
const ctx = canvas.getContext("2d");

const boxSize = 20;
const gridCount = canvas.width / boxSize;

let d = "RIGHT";
const startTime = Date.now();

function collision(head, array) {
    for (let i = 0; i < array.length; i++) {
        if (head.x === array[i].x && head.y === array[i].y) {
            return true;
        }
    }
    return false;
}

let obstacles = [
    { x: 5 * boxSize,  y: 5 * boxSize },
    { x: 5 * boxSize,  y: 6 * boxSize },
    { x: 5 * boxSize,  y: 7 * boxSize },

    { x: 14 * boxSize, y: 14 * boxSize },
    { x: 15 * boxSize, y: 14 * boxSize },
    { x: 16 * boxSize, y: 14 * boxSize }
];

function generateFood() {
    let newFood;
    while (true) {
        newFood = {
            x: Math.floor(Math.random() * gridCount) * boxSize,
            y: Math.floor(Math.random() * gridCount) * boxSize
        };
        if (!collision(newFood, obstacles)) {
            break;
        }
    }
    return newFood;
}

let snake = [
    { x: 10 * boxSize, y: 10 * boxSize },
    { x: 9 * boxSize,  y: 10 * boxSize },
    { x: 8 * boxSize,  y: 10 * boxSize }
];

let food = generateFood();

document.addEventListener("keydown", direction);

function direction(event) {
    if (event.keyCode === 37 && d !== "RIGHT") d = "LEFT";
    else if (event.keyCode === 38 && d !== "DOWN")  d = "UP";
    else if (event.keyCode === 39 && d !== "LEFT")  d = "RIGHT";
    else if (event.keyCode === 40 && d !== "UP")    d = "DOWN";
}

function draw() {
    ctx.fillStyle = "#000000";
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    ctx.strokeStyle = "#111111";
    for (let i = 0; i < gridCount; i++) {
        ctx.beginPath(); ctx.moveTo(i * boxSize, 0); ctx.lineTo(i * boxSize, canvas.height); ctx.stroke();
        ctx.beginPath(); ctx.moveTo(0, i * boxSize); ctx.lineTo(canvas.width, i * boxSize); ctx.stroke();
    }

    ctx.fillStyle = "#ff6b6b";
    ctx.fillRect(food.x, food.y, boxSize, boxSize);

    ctx.fillStyle = "#7f8c8d";
    for (let i = 0; i < obstacles.length; i++) {
        ctx.fillRect(obstacles[i].x, obstacles[i].y, boxSize, boxSize);
        ctx.strokeStyle = "#1a1a1a";
        ctx.strokeRect(obstacles[i].x, obstacles[i].y, boxSize, boxSize);
    }

    for (let i = 0; i < snake.length; i++) {
        ctx.fillStyle = (i === 0) ? "#4CAF50" : "#81C784";
        ctx.fillRect(snake[i].x, snake[i].y, boxSize, boxSize);
        ctx.strokeStyle = "#000";
        ctx.strokeRect(snake[i].x, snake[i].y, boxSize, boxSize);
    }

    let snakeX = snake[0].x;
    let snakeY = snake[0].y;

    if (d === "LEFT")  snakeX -= boxSize;
    if (d === "UP")    snakeY -= boxSize;
    if (d === "RIGHT") snakeX += boxSize;
    if (d === "DOWN")  snakeY += boxSize;

    if (snakeX < 0) {
        snakeX = canvas.width - boxSize;
    } else if (snakeX >= canvas.width) {
        snakeX = 0;
    }

    if (snakeY < 0) {
        snakeY = canvas.height - boxSize;
    } else if (snakeY >= canvas.height) {
        snakeY = 0;
    }

    if (snakeX === food.x && snakeY === food.y) {
        food = generateFood();
    } else {
        snake.pop();
    }

    let newHead = { x: snakeX, y: snakeY };

    if (collision(newHead, snake) || collision(newHead, obstacles)) {
        clearInterval(game);

        const endTime = Date.now();
        const timeSpentSeconds = Math.floor((endTime - startTime) / 1000);
        const finalScore = snake.length - 3;

        alert("Game Over! Scor final: " + finalScore + " | Timp: " + timeSpentSeconds + " secunde.");

        sendGameDataToServer(finalScore, timeSpentSeconds);
        return;
    }

    snake.unshift(newHead);
}

let game = setInterval(draw, 100);

function sendGameDataToServer(score, duration) {
    fetch('saveGame', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `score=${score}&duration=${duration}`
    })
        .then(response => {
            if (response.ok) {
                console.log("Datele jocului au fost salvate cu succes pe server!");
            } else {
                console.error("Eroare la salvarea datelor pe server.");
            }
            window.location.reload();
        })
        .catch(error => {
            console.error("Eroare de rețea:", error);
            window.location.reload();
        });
}