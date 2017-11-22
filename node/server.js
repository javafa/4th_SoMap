var express = require("express")
var app = express()

// gallery, user
var zone = require("./routes/zone")
var car = require("./routes/car");

// uri 요청에 따라 모듈을 분기해준다
app.use("/zone", zone)
app.use("/car", car)

// public 디렉토리 아래에 정적(image, video.. etc)파일을 읽어서 넘겨준다
app.use("/static",express.static("public"))

app.listen(8080,function(){
	console.log("server's running...")
})