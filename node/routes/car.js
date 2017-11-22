var express = require("express")
var router = express.Router()

// ROOT : /user 읽기
router.get("/",function(req, res, callback){
    res.send("request user select")
})
// /user 쓰기
router.post("/", function(req,res){
    res.send("request user write")
})
// /user 수정
router.put("/", function(req,res){
    res.send("request user update")
})
// /user 삭제
router.delete("/", function(req,res){
    res.send("request user delete")
})


// exports를 통해서 외부로부터 위에서 정의된 변수에 접근할 수 있게 해준다
module.exports = router