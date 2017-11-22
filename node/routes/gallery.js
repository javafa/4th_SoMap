var express = require("express")
var router = express.Router()

// ROOT : /gallery
router.get("/",function(req, res, next){
    res.send("respond from gallery root!!!")
});

module.exports = router