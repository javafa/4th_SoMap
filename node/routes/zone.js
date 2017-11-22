var express = require("express")
var router = express.Router()
var dao = require("../dao/zoneDao");

// ROOT : /zone 읽기
router.get("/", function(req, res, callback){
    var result = {
        code : "",
        msg : "",
        count : "",
        data : []
    };
    // zoneDao 로 쿼리를 실행시킨 결과를 반환
    dao.select(function(error, items){
        if(error){
            result.code = "501";
            result.msg = error;
        }else{
            result.code = "200";
            result.msg = "success";
            result.count = items.length;
            result.data = items;
        }
        res.send(JSON.stringify(result));
    });
});


router.get("/:area",function(req, res, callback){
    
    var area = req.params.area;
    console.log("지역명:"+area);

    var result = {
        code : "",
        msg : "",
        count : "",
        data : []
    };
    // zoneDao 로 쿼리를 실행시킨 결과를 반환
    dao.selectArea(area, function(error, items){
        if(error){
            result.code = "501";
            result.msg = error;
        }else{
            result.code = "200";
            result.msg = "success";
            result.count = items.length;
            result.data = items;
        }
        res.send(JSON.stringify(result));
    });

})


// exports를 통해서 외부로부터 위에서 정의된 변수에 접근할 수 있게 해준다
module.exports = router