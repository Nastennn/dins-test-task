const fs = require("fs");

const names = ["Liam", "Olivia", "Noah", "Emma",
    "Oliver", "Ava", "Elijah", "Charlotte",
    "William", "Sophia", "James", "Amelia", "Benjamin",
    "Isabella", "Lucas", "Mia", "Henry", "Evelyn",
    "Alexander", "Harper"]
for (let i = 0; i < 1000; i++) {
    const randomNumber = Math.round(Math.random() * (names.length-1));
    const name = names[randomNumber];
    fs.appendFileSync("inserts.sql", `insert into usernames(name) values ('${name}'); \n`);
}
