var express = require("express")
var router = express.Router()

var dao = require("../dao/carDao");

// ROOT : /car 읽기
router.get("/",function(req, res, callback){
    res.send("404 not found!")
})

router.get("/:zone_id",function(req, res, callback){
    var result = {
        code : "",
        msg : "",
        count : "",
        carData : []
    };

    dao.selectArea(req.params.zone_id, function(error, items){
        if(error){
            result.code = "501";
            result.msg = error;
        }else{
            result.code = "200";
            result.msg = "success";
            result.count = items.length;
            result.carData = items;
        }
        res.send(JSON.stringify(result));
    });

})

// exports를 통해서 외부로부터 위에서 정의된 변수에 접근할 수 있게 해준다
module.exports = router