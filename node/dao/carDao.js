var cons = require("../modules/database");

var task = {
    selectArea : function(areaCode, callback){
        var query_str = "select * from crawl_car where zone_id = ?"
        var values = [areaCode]
        cons.query(query_str, values, function(error, items, field){
            callback(error, items);
        });
    }
};

module.exports = task;


