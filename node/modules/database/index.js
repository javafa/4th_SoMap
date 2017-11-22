var mysql = require("mysql");
var conInfo = {
    host : '127.0.0.1',
    user: 'root',
    password : 'mysql',
    port : '3306',
    database : 'socar'
};

var connections = mysql.createPool(conInfo);

module.exports = connections;